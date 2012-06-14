package br.ufrgs.fci.factorize.model;

import br.ufrgs.fci.lcm.model.Item;

import java.util.List;

/**
 * This class is a sub type of item representing a factor-item, which is a union of common frequent closed itemsets.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class FactorItem extends Item implements Comparable<FactorItem> {

    private Integer id;
    private List<Item> originalItems;

    public FactorItem(Integer item, List<Item> originalItems) {
        super(item);
        this.id = item;
        this.originalItems = originalItems;
    }

    @Override
    public String toString() {
      StringBuilder factorItemString = new StringBuilder();
      int index = 0;
      for(Item item : originalItems) {
          factorItemString.append(item);
          if(++index <= originalItems.size() - 1) {
              factorItemString.append(" ");
          }

      }
      return factorItemString.toString();
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(FactorItem factorItem) {
        return this.getId().compareTo(factorItem.getId());
    }

}
