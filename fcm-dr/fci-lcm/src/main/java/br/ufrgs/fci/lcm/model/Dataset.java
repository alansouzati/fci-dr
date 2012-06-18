package br.ufrgs.fci.lcm.model;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This is the parser class for the dataset.
 * It has actions related to parse a txt based file to a Dataset class.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class Dataset {

    private List<Transaction> transactions;
    private List<Integer> transactionsItems;

    public Dataset() {
        transactions = new ArrayList<Transaction>();
    }

    public static Dataset getInstance(String datasetPath) throws IOException {

        datasetPath = datasetPath.replaceAll("%20", " ");

        File datasetFile = new File(datasetPath);

        Dataset dataset = new Dataset();

        if(datasetFile.exists()) {

            Transaction transaction;

            BufferedReader br = new BufferedReader(new FileReader(datasetPath));
            String items;
            while((items = br.readLine()) != null) { // iterate over the lines to build the transaction

                transaction = new Transaction();

                addItemToTransaction(transaction, items);

                dataset.getTransactions().add(transaction);
            }

            return dataset;
        } else {
            return null;
        }
    }

    private static void addItemToTransaction(Transaction transaction, String line) {

        //build the items
        Pattern splitPattern = Pattern.compile(" ");
        String[] items = splitPattern.split(line);

        ArrayList<Integer> itemsSorted = new ArrayList<Integer>();

        for (String item : items) {
            try {
                itemsSorted.add(Integer.valueOf(item));
            } catch (NumberFormatException ignored) {}
        }

        Collections.sort(itemsSorted);

        for(Integer item : itemsSorted) {
            transaction.getItems().add(new Item(item));
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Integer> getTransactionsItems() {

        if(transactionsItems == null) {
            Set<Integer> uniqueItems = new HashSet<Integer>();

            for(Transaction transaction : transactions) {
                for(Item item : transaction.getItems()) {
                    uniqueItems.add(item.getItem());
                }

            }

            transactionsItems = new ArrayList<Integer>(uniqueItems);
        }


        return transactionsItems;
    }

    public int getMaxItem() {
        int maxItem = 0;

        for(Transaction transaction : transactions) {
            for(Item item : transaction.getItems()) {
                if(item.getItem() > maxItem) {
                    maxItem = item.getItem();
                }
            }
        }

        return maxItem;
    }

    @Override
    public String toString() {
        StringBuilder datasetContent = new StringBuilder();

        for(Transaction transaction : transactions) {
            datasetContent.append(transaction);
            datasetContent.append("\n");
        }
        return datasetContent.toString();
    }

    public List<Transaction> getClonedTransactions() {
        List<Transaction> transactionList = new ArrayList<Transaction>();

        for(Transaction transaction : transactions) {
           Transaction cloneTransaction = new Transaction();
           for(Item item : transaction.getItems()) {
             cloneTransaction.getItems().add(new Item(item.getItem(), item.getOccurrence()));
           }

           transactionList.add(cloneTransaction);
        }
        return transactionList;
    }
}
