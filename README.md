# Concurrent programming in Java

Four small systems, each built to show one idea from concurrent programming: threads, locks, conditions and fork-join style parallelism. Each one is a runnable demo and has tests that check its behaviour.

| Exercise | Idea it shows | Key classes |
| --- | --- | --- |
| Locomotive hangar | Mutual exclusion and a hand-made handoff protocol between threads | `Locomotive.Loco`, `SegTournant`, `SegAcceuil`, `HangarPool` |
| Conveyor | Producers and consumers around a bounded buffer, with two conditions | `Convoyeur.Chargeur`, `Chariot`, `Dechargeur`, `AleaStock` |
| Room reservation | Shared state guarded by `synchronized`, and seat allocation | `Reservation.Salle`, `Groupe` |
| Matrix product | Splitting work across threads and joining the results | `Matrix.Matrix2d`, `CalculElem` |

## The locomotive hangar

One turntable serves a hangar pool. Only one locomotive may use the turntable at a time.

![A locomotive's trip: the turntable turns, the loco follows](docs/assets/diagrams/turntable-trip.png)

The turntable is a server thread. Each locomotive thread drives it through a sequence of conditions: `appeler`, `attendrePositionOk`, `entrer`, `sortir`. The entry track is a separate `ReentrantLock`, so a second locomotive cannot start its approach until the first has cleared the entry.

## The conveyor

A stock of random-weight items is loaded onto one chariot and unloaded again.

![A chariot filling up to its 30 kg limit, with a refused item](docs/assets/diagrams/conveyor-weight.png)

`Chariot` owns one `ReentrantLock` and two conditions. Loaders wait on `empty`, unloaders wait on `full`. An item that would break the weight or count limit is refused and goes back to the stock.

## Room reservation

`Salle` is a grid of seats. A group asks for `n` seats, and the room tries a contiguous run in one row first, then falls back to scattered free seats.

![Why the seat fix matters: old pair vs fixed pair](docs/assets/diagrams/seat-overlap.png)

The first version of `nContiguesAuRangI` returned the index of the last taken seat instead of the first free one, so a group could be given a seat that was already booked. A regression test now covers this case (`SalleTest.contiguousSearchDoesNotReuseAnOccupiedSeat`).

## Matrix product

`Matrix2d.dot` is the sequential product. `MatrixDemo` also starts one thread per output cell and joins them all, then checks the parallel result against the sequential one.

On the demo's 100×1000 by 1000×100 product, the sequential version took about 23 ms and the one-thread-per-cell version (10,000 threads) about 2.3 s. Thread creation costs more than the arithmetic in each cell. The lesson is to split work into chunks, not one task per cell. The demo keeps the naive version on purpose, so the cost is visible.

## Running

Requirements: JDK 25 or newer. Maven is not needed, because the wrapper downloads the pinned version.

```bash
./mvnw verify                                  # compile and run all tests
./mvnw -q compile && java -cp target/classes Matrix.MatrixDemo
java -cp target/classes Convoyeur.ConvoyeurDemo
java -cp target/classes Reservation.ReservationDemo
java -cp target/classes Locomotive.LocomotiveDemo
java -cp target/classes ThreadedWindow.WindowThread   # opens a window, needs a display
```

## Tests

```bash
./mvnw test
```

22 JUnit 5 tests cover:

- matrix arithmetic, shape checks, the product against a reference result in `test-data/res`, and the one-thread-per-cell version against the sequential one;
- seat allocation, refusal when full, freeing seats, and a stress test with 8 threads reserving and releasing seats at the same time;
- the chariot's weight and count limits, independent state across chariots, and first-in-first-out unloading;
- the stock's weight range and ordering;
- the hangar pool;
- a full run of five locomotives through the turntable, with a timeout so a deadlock fails the test instead of hanging.

The concurrency tests check final states and invariants, not timing, so they should not be flaky.

## Layout

```text
src/main/java/   sources, one folder per exercise (package = folder name)
src/test/java/   JUnit tests, same package layout
test-data/       matrix inputs and the reference product
doc/             course statements (PDF)
pom.xml          Maven build, JDK 25, JUnit 6
.github/         CI: ./mvnw verify on every push
```

Demos are named `*Demo`, not `*Test`, so Maven does not pick them up as tests.

## Known limitations

These are real, not hypothetical. They are left as they are on purpose, to keep the history readable, and they are good starting points for further work.

- **`Chargeur` uses a `static` flag** (`setted`) to decide which loader closes the chariot. It works for one chariot per run, but a second chariot in the same JVM would never be closed.
- **Busy waiting.** `Dechargeur.run` spins while the stock is not empty, and the locomotive protocol polls with `awaitNanos` in several places. Both work, but both burn CPU. A blocking hand-off would be cleaner.
- **Turntable shutdown.** `SegTournant.run` loops forever. The demos start it as a daemon thread so the JVM can exit; there is no clean stop signal yet.
- **The matrix demo** creates one thread per cell. That is intentional, for the lesson above, but it is not how you would parallelise this in production.

## Origin

Coursework from a Java concurrency course (LU3IN001). The original code is preserved in the git history. The course statements are in `doc/`.
