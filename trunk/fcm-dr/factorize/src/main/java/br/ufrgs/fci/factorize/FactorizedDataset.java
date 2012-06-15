package br.ufrgs.fci.factorize;

import br.ufrgs.fci.factorize.model.FactorItem;
import br.ufrgs.fci.lcm.algorithm.LCM;
import br.ufrgs.fci.lcm.model.Dataset;
import br.ufrgs.fci.lcm.model.Item;
import br.ufrgs.fci.lcm.model.Itemset;
import br.ufrgs.fci.lcm.model.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the class that implements the factorization problem for reducing the data dimensionality using
 * frequent closed itemsets and set cover problem.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class FactorizedDataset {

    private static List<Itemset> factorItems;

    public static Dataset factorize(double approximationDegree, String originalDataset, double minSupport) throws IOException {

        Dataset dataset = Dataset.getInstance(originalDataset);

        System.out.println("Running LCM to find frequent closed itemsets");
        List<Itemset> frequentClosedItemset = new LCM(minSupport,dataset).run();
        System.out.println("LCM has finished and found "+frequentClosedItemset.size()+" closed itemsets with support bound at "+minSupport);

        System.out.println("Running Factorization algorithm");
        List<Transaction> coveredUniverse = dataset.getClonedTransactions();

        int n = getItemCount(coveredUniverse);

        factorItems = new ArrayList<Itemset>();

        while(!frequentClosedItemset.isEmpty() && 1 - ((double)getItemCount(coveredUniverse)/n) < approximationDegree) {
            System.out.println("Number of factor items fci: "+ factorItems.size() + ". Aproximation degree "+(1 - ((double)getItemCount(coveredUniverse)/n)));
            System.out.println("Started find best");
            Itemset bestItemset = findBest(frequentClosedItemset);
            if(bestItemset!=null) {
                System.out.println("Started remove rectangle");
                removeRectangle(coveredUniverse,bestItemset);
                System.out.println("Started update list");
                frequentClosedItemset = updateList(frequentClosedItemset, coveredUniverse);

                //store B
                factorItems.add(bestItemset);
            }

        }

        System.out.println("Incremental join");
        while(1 - ((double)getItemCount(coveredUniverse)/n) < approximationDegree) {
            System.out.println("Number of factor items: "+ factorItems.size() + ". Aproximation degree "+(1 - ((double)getItemCount(coveredUniverse)/n)));
            Itemset bestRemaining = bestRemaining(coveredUniverse);

            removeRectangle(coveredUniverse,bestRemaining);

            //store B
            factorItems.add(bestRemaining);

        }

        System.out.println("Factorization algorithm has finished and it has found "+factorItems.size()+" factor items");
        return  buildFactorizedDataset(factorItems,dataset.getTransactions());
    }

    private static Itemset bestRemaining(List<Transaction> coveredUniverse) {

        List<Item> remainingItems = getRemainingItems(coveredUniverse);
        Itemset bestRemaining = new Itemset();

        Item biggestOccurrence = getItemBiggestOccurence(remainingItems,coveredUniverse);
        bestRemaining.getItems().add(biggestOccurrence.getItem());
        remainingItems.remove(biggestOccurrence);

        for(Item item : remainingItems) {

           bestRemaining.getItems().add(item.getItem());
           int itemsetOccurrence = getItemsetOccurence(coveredUniverse,bestRemaining);

           if(itemsetOccurrence < biggestOccurrence.getOccurrence()) {
               bestRemaining.getItems().remove(item.getItem());
           }
        }

        updateTransactionCount(bestRemaining,coveredUniverse);

        return bestRemaining;
    }

    private static void updateTransactionCount(Itemset itemset, List<Transaction> coveredUniverse) {
        for(Transaction transaction : coveredUniverse) {
            if(transaction.getItems().containsAll(itemset.getItemsInstances())) {
                itemset.getTransactions().add(transaction);
            }
        }
    }

    private static int getItemsetOccurence(List<Transaction> coveredUniverse, Itemset itemset) {
        int occurrence = 0;

        for(Transaction transaction : coveredUniverse) {
           if(transaction.getItems().containsAll(itemset.getItemsInstances())) {
               occurrence++;
           }
        }

        return occurrence;
    }

    private static Item getItemBiggestOccurence(List<Item> remainingItems, List<Transaction> coveredUniverse) {

        int biggestOccurrence = 0;

        Item biggestItemOccurrence = null;
        for(Item item : remainingItems) {
            int currentOccurrence = 0;
            for(Transaction transaction : coveredUniverse) {
               if(transaction.getItems().contains(item)) {
                 currentOccurrence++;
               }
            }

            if(currentOccurrence > biggestOccurrence) {
                biggestOccurrence = currentOccurrence;
                biggestItemOccurrence = item;
                biggestItemOccurrence.setOccurrence(currentOccurrence);
            }
        }

        return biggestItemOccurrence;
    }

    private static List<Item> getRemainingItems(List<Transaction> coveredUniverse) {

        List<Item> remainingItems = new ArrayList<Item>();
        for(Transaction transaction : coveredUniverse) {
            for(Item item : transaction.getItems()) {
                if(!remainingItems.contains(item)) {
                   remainingItems.add(item);
                }
            }
        }

        return remainingItems;
    }

    private static Dataset buildFactorizedDataset(List<Itemset> factorItems, List<Transaction> transactions) {
        Dataset factorizedDataset = new Dataset();

        for(Transaction transaction : transactions) {
            Transaction factorizedTransaction = new Transaction();

            int index = 1;
            for(Itemset itemset : factorItems) {
                if(transaction.getIntItems().containsAll(itemset.getItems())) {
                    FactorItem factorItem = new FactorItem(index,itemset.getItemsInstances());
                    factorizedTransaction.getItems().add(factorItem);
                }
                index++;
            }

            factorizedDataset.getTransactions().add(factorizedTransaction);

        }

        return factorizedDataset;
    }

    private static List<Itemset> updateList(List<Itemset> frequentClosedItemset, List<Transaction> coveredUniverse) {

        List<Itemset> itemsets = new ArrayList<Itemset>();

        for(Itemset itemset : frequentClosedItemset) {

          boolean isItemsetCovered = false;
          for(Transaction transaction : coveredUniverse) {

                for(Integer item : itemset.getItems()) {
                    if(transaction.getIntItems().contains(item)) {
                        isItemsetCovered = true;
                        break;
                    }
                }

                if(isItemsetCovered) {
                    break;
                }


          }

          if(isItemsetCovered) {
              itemsets.add(itemset);
          }
        }

        return itemsets;
    }

    private static void removeRectangle(List<Transaction> coveredUniverse, Itemset bestItemset) {

        for(Transaction transaction : coveredUniverse) {

           transaction.getItems().removeAll(bestItemset.getItemsInstances());

        }
    }

    private static Itemset findBest(List<Itemset> frequentClosedItemset) {

        Itemset bestItemset = null;
        int maximumCover = 0;

        for(Itemset item : frequentClosedItemset) {

            if(item.getOccurrence() >= maximumCover) {

               List<Integer> newAddedItems = getNewlyAddedItems(item.getItems());
               if(bestItemset==null || newAddedItems.size() >= bestItemset.getItems().size()) {
                   maximumCover = item.getOccurrence();
                   bestItemset = item;
               }

            }

        }

        frequentClosedItemset.remove(bestItemset);
        return bestItemset;
    }

    private static List<Integer> getNewlyAddedItems(List<Integer> items) {

        List<Integer> newAddedItems = new ArrayList<Integer>();

        for(Integer item : items) {
           boolean isNew = true;
           for(Itemset itemset : factorItems) {
               if(itemset.getItems().contains(item)) {
                   isNew = false;
                   break;
               }
           }

           if(isNew) {
             newAddedItems.add(item);
           }
        }

        return newAddedItems;
    }

    private static int getItemCount(List<Transaction> transactions) {
        int n = 0;

        for(Transaction transaction : transactions) {
            n += transaction.getItems().size();
        }

        return n;
    }

    private static List<FactorItem> getFactorItems(List<Transaction> transactions) {
        List<FactorItem> factorItems = new ArrayList<FactorItem>();

        for(Transaction transaction : transactions) {
          for(Item item : transaction.getItems()) {
              FactorItem factorItem = (FactorItem) item;
              if(!factorItems.contains(factorItem)) {
                  factorItems.add(factorItem);
              }
          }


        }

        Collections.sort(factorItems);

        return factorItems;
    }

    public static void main(String[] args) throws IOException {

        if(args.length == 0) {
           System.err.println("The dataset path is mandatory.");
           System.exit(0);
        }

        Double approximationDegree = 1.0;
        if(args.length > 1) {
           approximationDegree = Double.valueOf(args[1]);
        }

        Double minimumSupport = 1.0;
        if(args.length > 2) {
            minimumSupport = Double.valueOf(args[2]);
        }

        Dataset dataset = FactorizedDataset.factorize(approximationDegree,args[0],minimumSupport);

        File originalDatasetFile = new File(args[0]);
        String originalDatasetName = originalDatasetFile.getName().split("\\.")[0];
        File originalFolder = originalDatasetFile.getParentFile();

        File factorizedDataset = new File(originalFolder,originalDatasetName+"_factorized.dat");

        if(factorizedDataset.exists()) {
           if(!factorizedDataset.delete()){
               System.err.println("No permission to delete existing dataset file into "+originalFolder.getAbsolutePath()+". " +
                       "Please check permissions.");
           }
        }

        if(!factorizedDataset.createNewFile()){
          System.err.println("No permission to write the file into "+originalFolder.getAbsolutePath()+". " +
                  "Please check permissions.");
        }else {

            FileWriter fstream = new FileWriter(factorizedDataset);
            BufferedWriter out = new BufferedWriter(fstream);
            try {
                for(Transaction transaction : dataset.getTransactions()) {
                    if(transaction.toString()!=null && !transaction.toString().isEmpty()) {
                        out.write(transaction.toString());
                        out.newLine();
                    }

                }

            } finally {
                //Close the output stream
                out.close();
            }


        }

        System.out.println("#####################################\n");
        System.out.println("Factor-items:\n");
        for(FactorItem factorItem : getFactorItems(dataset.getTransactions())) {
            System.out.println(factorItem.getId()+" = {"+factorItem+"}\n");
        }
        System.out.println("#####################################\n");
    }
}
