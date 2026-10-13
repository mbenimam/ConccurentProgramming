package Convoyeur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ChariotTest {

    // AleaObj(min, max) with min == max always has that weight
    private static AleaObj weighing(int kg) {
        return new AleaObj(kg, kg);
    }

    @Test
    void loadsItemsUpToTheWeightLimit() throws Exception {
        Chariot chariot = new Chariot(10, 3);

        chariot.charger(weighing(5));
        chariot.charger(weighing(4));

        assertEquals(2, chariot.getCpt());
    }

    @Test
    void refusesAnItemThatWouldExceedTheWeightLimit() throws Exception {
        Chariot chariot = new Chariot(10, 3);
        chariot.charger(weighing(5));
        chariot.charger(weighing(4));

        assertThrows(Exception.class, () -> chariot.charger(weighing(5)));

        assertEquals(2, chariot.getCpt(), "a refused item must not be counted");
    }

    @Test
    void chariotsKeepTheirOwnCounts() throws Exception {
        // regression: count and weight used to be static, so loading one chariot loaded all of them
        Chariot first = new Chariot(30, 10);
        Chariot second = new Chariot(30, 10);

        first.charger(weighing(3));
        first.charger(weighing(3));

        assertEquals(2, first.getCpt());
        assertEquals(0, second.getCpt());
    }

    @Test
    void unloadsInTheOrderItWasLoaded() throws Exception {
        Chariot chariot = new Chariot(100, 10);
        AleaObj a = weighing(1);
        AleaObj b = weighing(2);
        AleaObj c = weighing(3);
        chariot.charger(a);
        chariot.charger(b);
        chariot.charger(c);
        // unloading waits until the chariot is marked full, as the loader does at the end of a run
        chariot.setFull();

        assertSame(a, chariot.decharger());
        assertSame(b, chariot.decharger());
        assertSame(c, chariot.decharger());
    }
}
