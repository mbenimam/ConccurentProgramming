package Locomotive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class HangarPoolTest {

    @Test
    void findsTheFirstFreeHangarOneBased() {
        HangarPool pool = new HangarPool(3);

        assertEquals(1, pool.hangarVide());

        pool.getHangar(1).entrer(7);
        assertEquals(2, pool.hangarVide());
    }

    @Test
    void reportsNoFreeHangarWhenAllAreOccupied() {
        HangarPool pool = new HangarPool(2);
        pool.getHangar(1).entrer(1);
        pool.getHangar(2).entrer(2);

        assertEquals(-1, pool.hangarVide());
        assertFalse(pool.getHangar(1).isEmpty());
        assertFalse(pool.getHangar(2).isEmpty());
    }
}
