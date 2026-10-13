package Convoyeur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AleaStockTest {

    @Test
    void randomWeightsStayInsideTheRequestedRange() {
        AleaStock stock = new AleaStock(50);
        for (int i = 0; i < 50; i++) {
            stock.addAlea(3, 7);
        }

        for (int i = 0; i < 50; i++) {
            int poids = stock.retirer().getPoid();
            assertTrue(poids >= 3 && poids <= 7, "weight out of range: " + poids);
        }
        assertTrue(stock.empty());
    }

    @Test
    void retiresItemsInFifoOrder() {
        AleaStock stock = new AleaStock(3);
        stock.ajouter(new AleaObj(1, 1));
        stock.ajouter(new AleaObj(2, 2));
        stock.ajouter(new AleaObj(3, 3));

        assertEquals(1, stock.retirer().getPoid());
        assertEquals(2, stock.retirer().getPoid());
        assertEquals(3, stock.retirer().getPoid());
    }

    @Test
    void countsItemsAddedThroughAddAlea() {
        AleaStock stock = new AleaStock(5);
        stock.addAlea(1, 2);
        stock.addAlea(1, 2);

        assertEquals(2, stock.getCpt());
        assertFalse(stock.empty());
    }
}
