package Reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class SalleTest {

    // reserver() pushes onto the front of the list, so compare seats sorted, not in list order
    private static List<Integer> sorted(LinkedList<Integer> seats) {
        List<Integer> copy = new ArrayList<>(seats);
        Collections.sort(copy);
        return copy;
    }

    @Test
    void reservesContiguousSeatsInOneRow() {
        Salle salle = new Salle(2, 5);
        LinkedList<Integer> seats = new LinkedList<>();

        assertTrue(salle.reserver(3, 1, seats));

        List<Integer> s = sorted(seats);
        assertEquals(3, s.size());
        // seats are row * 5 + column: they must sit in one row and be consecutive
        assertEquals(s.get(0) / 5, s.get(2) / 5);
        assertEquals(s.get(0) + 2, s.get(2));
    }

    @Test
    void refusesWhenRoomIsFull() {
        Salle salle = new Salle(1, 3);
        LinkedList<Integer> first = new LinkedList<>();
        LinkedList<Integer> second = new LinkedList<>();

        assertTrue(salle.reserver(3, 1, first));
        assertFalse(salle.reserver(1, 2, second));
        assertTrue(second.isEmpty());
    }

    @Test
    void annulerFreesTheSeats() {
        Salle salle = new Salle(1, 3);
        LinkedList<Integer> seats = new LinkedList<>();
        assertTrue(salle.reserver(3, 1, seats));

        salle.annuler(3, 1, seats);

        assertTrue(salle.reserver(3, 2, new LinkedList<>()));
    }

    @Test
    void contiguousSearchDoesNotReuseAnOccupiedSeat() {
        // Row is [taken, free, free, free]. A pair must start at seat 1, not reuse the taken seat 0.
        Salle salle = new Salle(1, 4);
        LinkedList<Integer> taken = new LinkedList<>();
        LinkedList<Integer> pair = new LinkedList<>();
        assertTrue(salle.reserver(1, 1, taken));
        assertEquals(List.of(0), sorted(taken));

        assertTrue(salle.reserver(2, 2, pair));

        assertEquals(List.of(1, 2), sorted(pair), "second group must take the free seats 1 and 2");
    }

    @Test
    void scatteredSeatsNeverOverlap() {
        // 2 rows of 3. Seats 0 and 1 are taken, so a group of 4 is split across the rest.
        Salle salle = new Salle(2, 3);
        assertTrue(salle.reserver(1, 1, new LinkedList<>()));
        assertTrue(salle.reserver(1, 2, new LinkedList<>()));

        LinkedList<Integer> rest = new LinkedList<>();
        assertTrue(salle.reserver(4, 3, rest));

        assertEquals(4, new HashSet<>(rest).size(), "a seat was given out twice: " + rest);
        assertFalse(rest.contains(0));
        assertFalse(rest.contains(1));
    }

    @Test
    void concurrentGroupsNeverShareSeatsAndLeaveTheRoomFree() throws InterruptedException {
        int rows = 5;
        int cols = 5;
        Salle salle = new Salle(rows, cols);
        int threadCount = 8;
        int rounds = 100;
        // worker threads cannot fail the test themselves: they record the problem, the test asserts after join
        AtomicReference<String> problem = new AtomicReference<>();

        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            final int id = t + 1;
            threads[t] = new Thread(() -> {
                LinkedList<Integer> mine = new LinkedList<>();
                for (int r = 0; r < rounds; r++) {
                    if (salle.reserver(2, id, mine)) {
                        if (new HashSet<>(mine).size() != 2) {
                            problem.compareAndSet(null, "group " + id + " holds a duplicate seat: " + mine);
                        }
                        salle.annuler(2, id, mine);
                    }
                }
            });
            threads[t].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        assertNull(problem.get());
        // every group released its seats, so the whole room must be free again
        assertTrue(salle.reserver(rows * cols, 99, new LinkedList<>()));
    }
}
