# Concurrent programming in Java: four systems, real threads, real bugs

**Four concurrency problems solved with threads, locks and conditions, each with a visual explainer, 22 tests, and measurements.**

[![build](https://github.com/mbeni12333/ConccurentProgramming/actions/workflows/build.yml/badge.svg)](https://github.com/mbeni12333/ConccurentProgramming/actions/workflows/build.yml)
![Java 25](https://img.shields.io/badge/Java-25-orange)
![JUnit 6](https://img.shields.io/badge/tests-JUnit%206-brightgreen)
![Maven](https://img.shields.io/badge/build-Maven%20wrapper-blue)

| | |
| --- | --- |
| **4** | concurrent systems: a turntable protocol, a producer/consumer conveyor, shared seat allocation, multi-threaded matrix product |
| **22** | JUnit 5 tests, run on every push in CI |
| **1** | real bug in the seat allocator, found by a test that now guards it |
| **100×** | slowdown measured for one thread per cell, and why it happens |

---

## The turntable: five threads, two locks, one protocol

A locomotive has to pass through a turntable that only one train can use at a time. The turntable is a server thread. Each locomotive drives it through `appeler`, `attendrePositionOk`, `entrer` and `sortir`, while a separate lock guards the entry track, so the next train can queue up while the first is still turning.

![One locomotive's trip through the turntable, in five frames](docs/assets/diagrams/turntable-trip.png)

**The hard part:** two locks that cover different spans. The entry is free as soon as the locomotive is on the table, but the table stays locked until it leaves. Getting the release order wrong is how systems like this deadlock.

---

## The conveyor: a bounded buffer with two conditions

Five loaders fill a chariot, five unloaders empty it. The chariot has a weight limit and an item limit. A loader that would break a limit gets refused and puts its item back. Loaders sleep on an `empty` condition, unloaders on `full`.

![The chariot filling to 30 kg, refusing an item, then unloading](docs/assets/diagrams/conveyor-weight.png)

**The hard part:** every waiting thread has to be woken by the right signal, at the right moment, or the run stalls. A `static` flag in the loader still makes a second chariot in the same process misbehave. That limitation is documented below.

---

## The seat allocator: a bug that a test caught

A group asks for `n` seats. The room looks for a contiguous run in one row, then falls back to scattered free seats.

![Booking two seats next to a taken seat: old code vs fixed code](docs/assets/diagrams/seat-overlap.png)

The original scan returned the index of the last taken seat, not the first free one. A new group could be given a seat that was already booked. A regression test (`SalleTest.contiguousSearchDoesNotReuseAnOccupiedSeat`) now fails if that comes back, and a stress test runs 8 threads reserving and releasing seats at the same time.

---

## The matrix product: why one thread per cell is a trap

`MatrixDemo` multiplies a 100×1000 matrix by a 1000×100 matrix two ways. The sequential product takes 23 ms. Starting one thread per output cell (10,000 threads) takes 2294 ms, with the same result.

![One thread per cell: the thread costs more than the arithmetic](docs/assets/diagrams/matrix-threads.png)

Each cell's arithmetic takes 2.3 µs. Creating and joining its thread costs about 227 µs, so 99% of the run is thread overhead. The demo keeps the naive version on purpose, so the cost is visible. The fix is to split the rows across a few threads, not to give every cell its own.

---

## Proof, not promises

- **22 JUnit 5 tests**, in `src/test/java`. They cover matrix arithmetic against a reference result in `test-data/res`, seat allocation under contention, the chariot's limits and ordering, and a full five-locomotive run with a timeout, so a deadlock fails the test instead of hanging the build.
- **Tests fail on the original bugs.** I reintroduced each bug in a scratch copy: the seat allocator fails two tests, the shape check fails one, and the shared-counter bug fails the chariot-independence test.
- **CI on every push:** `./mvnw verify` on JDK 25, with the Maven wrapper pinning the build tool.

| Bug found | Symptom | Fix | Guarded by |
| --- | --- | --- | --- |
| Seat scan off by one | a booked seat is given out twice | `index = j + 1` | `contiguousSearchDoesNotReuseAnOccupiedSeat` |
| Shape check used `&&` | mismatched matrices were accepted | `\|\|` | `addRejectsShapeMismatch` |
| Chariot counters `static` | every chariot shared one load | per-instance fields | `chariotsKeepTheirOwnCounts` |
| Matrix timing | measured thread start-up, not work | join threads before stopping the clock | the demo checks the parallel result; `oneThreadPerCellGivesSameResultAsSequential` checks correctness, not timing |

---

## Run it

Requires JDK 25 or newer. Maven isn't needed, the wrapper downloads the pinned version.

```bash
./mvnw verify                                             # build and run all 22 tests
java -cp target/classes Locomotive.LocomotiveDemo         # watch the turntable run
java -cp target/classes Convoyeur.ConvoyeurDemo           # the conveyor, with real threads
java -cp target/classes Reservation.ReservationDemo       # shared seat allocation
java -cp target/classes Matrix.MatrixDemo                 # sequential vs one thread per cell
java -cp target/classes ThreadedWindow.WindowThread       # draws lines in parallel, needs a display
```

## Stack

Java 25 · JUnit 5 · Maven wrapper · GitHub Actions · `ReentrantLock`, `Condition`, `synchronized`, `Thread.join`

## Known limitations

These are real, and I left them as they are on purpose so the history stays readable.

- **A `static` flag** in `Chargeur` decides which loader closes the chariot. A second chariot in the same process would never be closed.
- **Busy waiting** in `Dechargeur.run` and in the turntable's `awaitNanos` polling. Both work, but both burn CPU. A blocking hand-off would be cleaner.
- **No clean turntable shutdown.** `SegTournant.run` loops forever; the demos run it as a daemon thread.
- **The turntable trace** can print "entered hangar" before the table's second one-second move has finished. The diagram shows the intended order.

## Origin

Coursework from a Java concurrency course (LU3IN001), rebuilt as a Maven project with tests and CI. The original history is preserved in git. The course statements are in `doc/`.
