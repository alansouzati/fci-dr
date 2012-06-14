package br.ufrgs.fci.factorize;

import br.ufrgs.fci.lcm.model.Dataset;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This is the test class for Factorize problem.
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class FactorizeTest {

    @Test
    public void testExactFactorization() throws IOException {

        Dataset dataset = FactorizedDataset.factorize(1.0,FactorizeTest.class.getResource("/test.dat").getFile(),0.1);

        assertNotNull("The dataset was not found.",dataset);
        assertEquals("The number of factorized-transactions should be 6", 6, dataset.getTransactions().size());
        assertEquals("The number of factor-items should be 4", 4, dataset.getTransactionsItems().size());
    }

    @Test
    public void testApproximateFactorization() throws IOException {

        Dataset dataset = FactorizedDataset.factorize(0.83,FactorizeTest.class.getResource("/test.dat").getFile(),50);

        assertNotNull("The dataset was not found.",dataset);
        assertEquals("The number of factorized-transactions should be 6", 6, dataset.getTransactions().size());
        assertEquals("The number of factor-items should be 5", 5, dataset.getTransactionsItems().size());
    }
}
