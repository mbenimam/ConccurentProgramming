package Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * One thread per output cell, compared with the sequential product.
 *
 * The earlier version timed only the loop that starts the threads, never waited for them,
 * and never checked the result. Here every thread is joined before the clock stops, and the
 * parallel matrix is compared with the sequential one.
 */
public class MatrixDemo {
    public static void main(String[] args) throws Exception {
        Matrix2d a = new Matrix2d(100, 1000);
        Matrix2d b = new Matrix2d(1000, 100);
        fillRandomly(a);
        fillRandomly(b);

        long start = System.nanoTime();
        Matrix2d sequential = a.dot(b);
        long sequentialMs = (System.nanoTime() - start) / 1_000_000;

        Matrix2d parallel = new Matrix2d(a.getDimX(), b.getDimY());
        start = System.nanoTime();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < a.getDimX(); i++) {
            for (int j = 0; j < b.getDimY(); j++) {
                Thread t = new Thread(new CalculElem(a, b, parallel, i, j));
                threads.add(t);
                t.start();
            }
        }
        for (Thread t : threads) {
            t.join();
        }
        long parallelMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("sequential : " + sequentialMs + " ms");
        System.out.println("one thread per cell (" + threads.size() + " threads) : " + parallelMs + " ms");
        System.out.println("same result : " + sameValues(sequential, parallel));
    }

    private static void fillRandomly(Matrix2d m) {
        for (int i = 0; i < m.getDimX(); i++) {
            for (int j = 0; j < m.getDimY(); j++) {
                m.values[i][j] = (int) (Math.random() * 10);
            }
        }
    }

    private static boolean sameValues(Matrix2d x, Matrix2d y) {
        for (int i = 0; i < x.getDimX(); i++) {
            for (int j = 0; j < x.getDimY(); j++) {
                if (x.values[i][j] != y.values[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
