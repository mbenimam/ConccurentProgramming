package Matrix;

import jdk.jshell.spi.ExecutionControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Matrix2d{
    // dimensions de la matrice
    private int dimX;
    private int dimY;
    int[][] values;


    Matrix2d(int dimX, int dimY){
        this.dimX = dimX;
        this.dimY = dimY;

        this.values =  new int[this.dimX][this.dimY];
    }
    Matrix2d(String filename) throws Exception{
        Scanner matrixReader = new Scanner(new BufferedReader(new FileReader(new File(filename))));

        this.dimX = matrixReader.nextInt();
        this.dimY = matrixReader.nextInt();
        this.values =  new int[this.dimX][this.dimY];

        for(int i=0; i<this.dimX; i++){
            for(int j=0; j<this.dimY; j++){
                values[i][j] = matrixReader.nextInt();
            }
        }
    }
    public Matrix2d add(Matrix2d m2)throws Exception{
        if(m2.dimX != this.dimX && m2.dimY != this.dimY){
            throw new ShapeMismatchException(this.dimX, this.dimY, m2.dimX, m2.dimY);
        }
        Matrix2d m3 = new Matrix2d(this.dimX, this.dimY);

        for(int i=0; i<this.dimX; i++){
            for(int j=0; j<this.dimY; j++) {
                m3.values[i][j] += m2.values[i][j] + this.values[i][j];
            }
        }
        return m3;
    }
    public Matrix2d dot(Matrix2d m2)throws Exception{
        if(m2.dimX != this.dimY){
            throw new ShapeMismatchException(this.dimX, this.dimY, m2.dimX, m2.dimY);
        }
        Matrix2d m3 = new Matrix2d(this.dimX, m2.dimY);

        for(int i=0; i<this.dimX; i++){
            for(int k=0; k<m2.dimY; k++) {
                for (int j = 0; j < this.dimY; j++) {
                    m3.values[i][k] += this.values[i][j]*m2.values[j][k];
                }
            }
        }
        return m3;
    }
    public int getDimX() {
        return dimX;
    }
    public int getDimY() {
        return dimY;
    }

    public static int produitLigneColone(Matrix2d m1, Matrix2d m2, int i, int j) throws Exception{
        int somme = 0;
        for(int k=0; k<m1.getDimY(); k++){
            somme += m1.values[i][k]*m2.values[k][j];
        }
        return somme;
    }
    @Override
    public String toString() {
        String s = "";
        for(int i=0; i<this.dimX; i++){
            for(int j=0; j<this.dimY; j++){
                s += values[i][j] + " ";
            }
            s += "\n";
        }
        return s;
    }
}
