package br.ufrgs.fci.lcm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the bucket e, used to store the transactions of e.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class Bucket {

    private List<Transaction> transactions;

    public Bucket() {
        transactions = new ArrayList<Transaction>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
