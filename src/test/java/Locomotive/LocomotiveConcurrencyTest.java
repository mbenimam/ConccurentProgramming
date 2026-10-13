package Locomotive;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class LocomotiveConcurrencyTest {

    @Test
    void everyLocomotiveEntersADistinctHangarAndTheSystemStops() {
        int locomotives = 5;

        // The whole run must finish. A deadlock or a missing wake-up shows up as the timeout.
        assertTimeoutPreemptively(Duration.ofSeconds(90), () -> {
            SegAcceuil accueil = new SegAcceuil();
            HangarPool hangars = new HangarPool(locomotives);
            SegTournant tournant = new SegTournant(hangars);

            // the turntable loops forever: daemon, so it cannot keep the test JVM alive
            Thread server = new Thread(tournant);
            server.setDaemon(true);
            server.start();

            Thread[] threads = new Thread[locomotives];
            for (int i = 0; i < locomotives; i++) {
                threads[i] = new Thread(new Loco(accueil, tournant, hangars));
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }

            for (int h = 1; h <= locomotives; h++) {
                assertFalse(hangars.getHangar(h).isEmpty(), "hangar " + h + " was never entered");
            }
        });
    }
}
