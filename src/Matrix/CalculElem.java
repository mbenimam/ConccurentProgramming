package Matrix;

public class CalculElem implements Runnable{
    int i;
    int j;
    Matrix2d m1;
    Matrix2d m2;
    Matrix2d m3;
    CalculElem(Matrix2d m1, Matrix2d m2, Matrix2d m3, int i, int j){
        this.i = i;
        this.j = j;
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
    }

    public void run(){
        try {
            m3.values[i][j] = Matrix2d.produitLigneColone(m1, m2, i, j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
