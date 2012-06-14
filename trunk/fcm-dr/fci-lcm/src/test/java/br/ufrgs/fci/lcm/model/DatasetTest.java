package br.ufrgs.fci.lcm.model;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * This is the model test class for the dataset parser.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 *
 *
 */
public class DatasetTest {

    @Test
    public void testDatasetParser() throws IOException {

        Dataset dataset = Dataset.getInstance(DatasetTest.class.getResource("/test.dat").getFile());

        assertNotNull("The dataset was not found.",dataset);
        assertEquals("The number of transactions should be 7", 7, dataset.getTransactions().size());
        assertEquals("The number of items should be 6", 6, dataset.getTransactionsItems().size());
    }
}
