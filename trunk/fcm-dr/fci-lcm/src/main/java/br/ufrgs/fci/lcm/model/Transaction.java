package br.ufrgs.fci.lcm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has an abstraction of a dataset transaction.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class Transaction {

    private List<Item> items;

    public Transaction() {
        items = new ArrayList<Item>();
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Integer> getIntItems() {
        List<Integer> intItemList = new ArrayList<Integer>();
        for(Item item : items) {
            intItemList.add(item.getItem());
        }
        return intItemList;
    }

    @Override
    public String toString() {
        StringBuilder datasetContent = new StringBuilder();

        for(Item item : items) {
            datasetContent.append(item.getDisplayName());
            datasetContent.append(" ");
        }
        return datasetContent.toString();
    }

}
