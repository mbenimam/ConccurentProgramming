package Matrix;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class Matrix2dTest {

    // Expected product of test-data/matrice1 (5x4) and test-data/matrice2 (4x8), copied from test-data/res.
    private static final int[][] EXPECTED_PRODUCT = {
        {-171, 64, -367, -196, 9, 73, -36, 72},
        {-359, 160, -763, -360, -15, 201, -116, 200},
        {-547, 256, -1159, -524, -39, 329, -196, 328},
        {-735, 352, -1555, -688, -63, 457, -276, 456},
        {-923, 448, -1951, -852, -87, 585, -356, 584},
    };

    private static Matrix2d of(int[][] rows) {
        Matrix2d m = new Matrix2d(rows.length, rows[0].length);
        for (int i = 0; i < rows.length; i++) {
            m.values[i] = rows[i].clone();
        }
        return m;
    }

    @Test
    void dotMatchesHandComputedProduct() throws Exception {
        Matrix2d a = of(new int[][] {{1, 2, 3}, {4, 5, 6}});
        Matrix2d b = of(new int[][] {{7, 8}, {9, 10}, {11, 12}});

        Matrix2d c = a.dot(b);

        assertEquals(2, c.getDimX());
        assertEquals(2, c.getDimY());
        assertArrayEquals(new int[] {58, 64}, c.values[0]);
        assertArrayEquals(new int[] {139, 154}, c.values[1]);
    }

    @Test
    void dotMatchesReferenceProductFromTestData() throws Exception {
        Matrix2d a = new Matrix2d("test-data/matrice1");
        Matrix2d b = new Matrix2d("test-data/matrice2");

        Matrix2d c = a.dot(b);

        assertEquals(EXPECTED_PRODUCT.length, c.getDimX());
        for (int i = 0; i < EXPECTED_PRODUCT.length; i++) {
            assertArrayEquals(EXPECTED_PRODUCT[i], c.values[i], "row " + i);
        }
    }

    @Test
    void addIsElementWise() throws Exception {
        Matrix2d a = of(new int[][] {{1, 2}, {3, 4}});
        Matrix2d b = of(new int[][] {{10, 20}, {30, 40}});

        Matrix2d sum = a.add(b);

        assertArrayEquals(new int[] {11, 22}, sum.values[0]);
        assertArrayEquals(new int[] {33, 44}, sum.values[1]);
    }

    @Test
    void addRejectsShapeMismatch() {
        Matrix2d a = new Matrix2d(2, 3);
        Matrix2d b = new Matrix2d(2, 2);

        assertThrows(ShapeMismatchException.class, () -> a.add(b));
    }

    @Test
    void dotRejectsShapeMismatch() {
        Matrix2d a = new Matrix2d(2, 3);
        Matrix2d b = new Matrix2d(2, 3);

        assertThrows(ShapeMismatchException.class, () -> a.dot(b));
    }

    @Test
    void oneThreadPerCellGivesSameResultAsSequential() throws Exception {
        Matrix2d a = of(new int[][] {{1, -2, 3, 4}, {5, 6, -7, 8}, {9, 10, 11, -12}});
        Matrix2d b = of(new int[][] {{2, 0}, {-1, 3}, {4, 4}, {0, -5}});
        Matrix2d expected = a.dot(b);
        Matrix2d parallel = new Matrix2d(a.getDimX(), b.getDimY());

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

        for (int i = 0; i < expected.getDimX(); i++) {
            assertArrayEquals(expected.values[i], parallel.values[i], "row " + i);
        }
    }
}
