package Matrix;

public class MatrixTest {
    public static void main(String[] args){
        try {
            Matrix2d a = new Matrix2d(100, 1000);
            Matrix2d b = new Matrix2d(1000, 100);
            Matrix2d c = new Matrix2d(a.getDimX(), b.getDimY());
            long start = System.currentTimeMillis();
            for(int i=0; i<a.getDimX();i++){
                for(int j=0; j<b.getDimY(); j++){
                    Thread t = new Thread(new CalculElem(a, b, c, i, j));
                    t.start();
                }
            }

            long finish = System.currentTimeMillis() - start;
            System.out.println("finished in : " + finish + " threads : " + Thread.activeCount());
            start = System.currentTimeMillis();
            c = a.dot(b);
            finish = System.currentTimeMillis() - start;
            System.out.println("finished in : " + finish);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
