package br.ufrgs.fci.lcm.model;

/**
 * This is the model class that represent transaction items
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class Item {

    private Integer item;
    private Integer occurrence;

    public Item(Integer item) {
        this.item = item;
    }

    public Item(Integer item, Integer occurrence) {
       this.item = item;
       this.occurrence = occurrence;
    }

    public Integer getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item1 = (Item) o;

        return !(item != null ? !item.equals(item1.item) : item1.item != null);

    }

    @Override
    public int hashCode() {
        return item != null ? item.hashCode() : 0;
    }

    @Override
    public String toString() {
        return item.toString();
    }

    public Integer getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Integer occurrence) {
        this.occurrence = occurrence;
    }

    public String getDisplayName() {
        return item.toString();
    }

}
