package br.ufrgs.fci.lcm.algorithm;

import br.ufrgs.fci.lcm.model.Dataset;
import br.ufrgs.fci.lcm.model.Itemset;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

/**
 * This is the test class for the LCM (linear time closed itemset miner) algorithm
 *
 * @author Alan Souza <apsouza@inf.ufrgs.br>
 */
public class LCMTest {

    @Test
    public void testLCMExecution() throws IOException {

        LCM lcm = new LCM(10, Dataset.getInstance(LCMTest.class.getResource("/test.dat").getFile()));

        List<Itemset> closedFrequentItems = lcm.run();

        assertNotNull("The closed frequent itemsets cannot be null",closedFrequentItems);
        assertEquals("The expected number of frequent closed itemsets should be 11", 11, closedFrequentItems.size());
        assertEquals("The first frequent closed itemset should be 1 4 6", "1 4 6", closedFrequentItems.get(0).toString());
        assertEquals("The last frequent closed itemset should be 4 6", "4 6", closedFrequentItems.get(closedFrequentItems.size() - 1).toString());
        System.out.println("=====================================");
        System.out.println(closedFrequentItems);
        System.out.println("Total count: "+closedFrequentItems.size());
        System.out.println("=====================================");

        lcm = new LCM(Dataset.getInstance(LCMTest.class.getResource("/test2.dat").getFile()));

        closedFrequentItems = lcm.run();

        assertNotNull("The closed frequent itemsets cannot be null",closedFrequentItems);
        assertEquals("The expected number of frequent closed itemsets should be 29", 29, closedFrequentItems.size());
        assertEquals("The first frequent closed itemset should be 1", "1", closedFrequentItems.get(0).toString());
        assertEquals("The last frequent closed itemset should be 6", "6", closedFrequentItems.get(closedFrequentItems.size() - 1).toString());
        System.out.println("=====================================");
        System.out.println(closedFrequentItems);
        System.out.println("Total count: "+closedFrequentItems.size());
        System.out.println("=====================================");

        lcm = new LCM(Dataset.getInstance(LCMTest.class.getResource("/test3.dat").getFile()));

        closedFrequentItems = lcm.run();

        assertNotNull("The closed frequent itemsets cannot be null",closedFrequentItems);
        assertEquals("The expected number of frequent closed itemsets should be 9", 9, closedFrequentItems.size());
        assertEquals("The first frequent closed itemset should be 0 4 9", "0 4 9", closedFrequentItems.get(0).toString());
        assertEquals("The last frequent closed itemset should be 9", "9", closedFrequentItems.get(closedFrequentItems.size() - 1).toString());
        System.out.println("=====================================");
        System.out.println(closedFrequentItems);
        System.out.println("Total count: "+closedFrequentItems.size());
        System.out.println("=====================================");
    }
}
