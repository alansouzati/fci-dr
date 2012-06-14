package br.ufrgs.fci.lcm.algorithm;

import br.ufrgs.fci.lcm.model.Dataset;
import br.ufrgs.fci.lcm.model.Item;
import br.ufrgs.fci.lcm.model.Itemset;
import br.ufrgs.fci.lcm.model.Transaction;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class for the LCM algorithm
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class LCM {

    private double minimumSupport;
    private Dataset dataset;
    private ArrayList<Itemset> closedFrequentItemsets;
    private OccurrenceDeliver occ;

    public LCM(Dataset dataset) {
        this.minimumSupport = 0.1;
        this.dataset = dataset;
        this.closedFrequentItemsets = new ArrayList<Itemset>();
    }

    public LCM(double minimumSupport, Dataset dataset) {
        this.minimumSupport = minimumSupport;
        this.dataset = dataset;
        this.closedFrequentItemsets = new ArrayList<Itemset>();
    }

    public List<Itemset> run() {

        occ = new OccurrenceDeliver(dataset);

        // as the itemset is empty, initially we will have all transactions and no frequency count
        backtracking(new Itemset(), dataset.getTransactions(), new ArrayList<Integer>());

        return closedFrequentItemsets;
    }

    private void backtracking(Itemset p, List<Transaction> transactionsOfP, List<Integer> frequencyCount) {

        output(p);

        int tailOfP = tail(p,frequencyCount);

        List<Integer> frequentItems = getFrequentItems(p, tailOfP);

        List<Transaction> intersectTransactions;
        List<Integer> newFrequencyCount;
        for(Integer e : frequentItems) {
            intersectTransactions = getIntersectTransactions(transactionsOfP,e);
            // to be closed it should be a ppc extension
            if(isPPCExtension(p,e,intersectTransactions)) {
              Itemset itemset = frequentClosedItemset(p, e, intersectTransactions);
              anyTimeDatasetReduction(intersectTransactions);
              newFrequencyCount = frequencyCount(itemset, e, transactionsOfP, frequencyCount);
              itemset.setOccurrence(newFrequencyCount.get(newFrequencyCount.size()-1));
              backtracking(itemset, intersectTransactions, newFrequencyCount);
           }
        }

    }

    private void anyTimeDatasetReduction(List<Transaction> intersectTransactions) {

       occ.clear();

       for(Transaction transaction : intersectTransactions) {
           for(Item item : transaction.getItems()) {
                occ.getBuckets()[item.getItem()].getTransactions().add(transaction);
            }
        }

    }

    private List<Integer> frequencyCount(Itemset p, Integer e, List<Transaction> transactionsOfP, List<Integer> frequencyCount) {
        List<Integer> frequencyCountList = new ArrayList<Integer>();
        int iter = 0;

        if(!frequencyCount.isEmpty()) {

            iter = addPreviousFrequencies(p, e, frequencyCount, frequencyCountList, iter);
        }

        List<Transaction> transactions = transactionsOfP;
        List<Transaction> visitedTransactions = new ArrayList<Transaction>();

        for(Integer item : p.getItems().subList(iter,p.getItems().size())) {

            int fCount = 0;

            for(Transaction transaction : transactions) { //only iterates over the visited transactions
               if(transaction.getItems().contains(new Item(item))) {
                  fCount++;
                  visitedTransactions.add(transaction);
               }
            }

            frequencyCountList.add(fCount);
            transactions = new ArrayList<Transaction>(visitedTransactions);
            visitedTransactions.clear();
        }

        return frequencyCountList;
    }

    private int addPreviousFrequencies(Itemset p, Integer e, List<Integer> frequencyCount, List<Integer> frequencyCountList, int iter) {
        for(Integer item : p.getItems()) {
          if(item >= e) {
             break;
          }
          if(item < frequencyCount.size()) {
              frequencyCountList.add(frequencyCount.get(item));
              iter++;
          }


       }
        return iter;
    }

    private Itemset frequentClosedItemset(Itemset p, Integer e, List<Transaction> intersectTransactions) {

        Itemset fci = new Itemset();

        //add every item i of p which is smaller then e to the closed itemsets
        for (int i = 0; i < p.getItems().size() &&  p.getItems().get(i) < e; i++) {
            fci.getItems().add(p.getItems().get(i));
        }

        // add e to them closed itemset
        fci.getItems().add(e);

        List<Integer> items = dataset.getTransactionsItems().subList(dataset.getTransactionsItems().indexOf(e),dataset.getTransactionsItems().size());

        for(Integer i : items) {
            // for every item i > e add if it is in all transactions of T(P U e)
            if(isItemInAllTransactions(intersectTransactions, i) && !fci.getItems().contains(i)) {
                fci.getItems().add(i);
            }
        }

        return fci;
    }

    private boolean isPPCExtension(Itemset p, Integer e, List<Transaction> intersectTransactions) {

        for (int i = dataset.getTransactionsItems().get(0); i < e; i++) {

            // p does not contain item i < e and item i is present in all transactions, then it is not a ppc
            if(!p.getItems().contains(i)
                    && isItemInAllTransactions(intersectTransactions, i)) {
                return false;
            }
        }

        return true;
    }

    private boolean isItemInAllTransactions(List<Transaction> transactions, Integer item) {

        for(Transaction transaction : transactions) {
            if(!transaction.getItems().contains(new Item(item))) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> getFrequentItems(Itemset p, int tailOfP) {
        List<Integer> frequentItems = new ArrayList<Integer>();

        // from items > tailOfP
        int fromIndex = tailOfP;
        if(dataset.getTransactionsItems().indexOf(tailOfP) >=0) {
            fromIndex = dataset.getTransactionsItems().indexOf(tailOfP);
        }
        for(Integer currentItem : dataset.getTransactionsItems().subList(fromIndex,dataset.getTransactionsItems().size())) {
            double support = (double) occ.getBuckets()[currentItem].getTransactions().size() / dataset.getTransactions().size();
            DecimalFormat format = new DecimalFormat("#.##");
            support = Double.valueOf(format.format(support));
            if(support >= (minimumSupport / 100)
                    &&  !p.getItems().contains(currentItem)) {
                frequentItems.add(currentItem);
            }
        }
        return frequentItems;
    }

    private int tail(Itemset itemset, List<Integer> frequencyCount) {

        if(!itemset.getItems().isEmpty()) {

            //go from the last till it finds a different frequency count
            int lastItemFrequencyCount = frequencyCount.get(frequencyCount.size() - 1);
            for (int i = frequencyCount.size() - 2; i >= 0; i--) {
                if (lastItemFrequencyCount != frequencyCount.get(i)) {
                   return itemset.getItems().get(i+1); //the new tail is the item with different freq count
                }
            }

            //if all frequency count are the same then the first item will be the tail
            return itemset.getItems().get(0);
        }

        // if there is no item then the first index will be the tail
        return 0;
    }

    private void output(Itemset itemset) {
        if(!itemset.getItems().isEmpty()) {
            closedFrequentItemsets.add(itemset);
        }
    }

    public List<Transaction> getIntersectTransactions(List<Transaction> transactionsOfP, Integer e) {
        List<Transaction> intersectTransactions = new ArrayList<Transaction>();

        // transactions of P U e
        for(Transaction transaction : transactionsOfP) {
            if (transaction.getItems().contains(new Item(e))) { // T(P U e)
                intersectTransactions.add(transaction);
            }

        }

        return intersectTransactions;
    }

    public static void main(String[] args) throws IOException {
        LCM lcm = new LCM(Double.valueOf(args[1]), Dataset.getInstance(args[0]));

        List<Itemset> closedFrequentItems = lcm.run();

        System.out.println("=====================================");
        System.out.println("Total count: "+closedFrequentItems.size());
        System.out.println("=====================================");
    }
}
