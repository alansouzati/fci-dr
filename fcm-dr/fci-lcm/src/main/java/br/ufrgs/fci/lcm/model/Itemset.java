package br.ufrgs.fci.lcm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the model class used by the lcm algorithm to add itemsets
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class Itemset {

    private List<Integer> items;
    private List<Transaction> transactions;
    private int occurrence;

    public Itemset() {
        items = new ArrayList<Integer>();
        transactions = new ArrayList<Transaction>();
    }

    public List<Integer> getItems() {
        return items;
    }

    public List<Item> getItemsInstances() {
        List<Item> itemsInstances = new ArrayList<Item>();

        for(Integer item : items) {
            itemsInstances.add(new Item(item));
        }

        return itemsInstances;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int index = 0;
        for(Integer item : items) {
            sb.append(item);

            if(index++ < items.size()-1) {
               sb.append(" ");
            }
        }

        return sb.toString();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }
}
