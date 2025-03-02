import java.util.Arrays;
import java.util.Random;

public class Matriu {
    private int[][] matriu;

    public Matriu(int m, int n) {
        this.matriu = new int[m][n];
    }


    public Matriu sumar(Matriu matriu) {
        int n = this.matriu.length;
       Matriu result = new Matriu(n, n);
       for (int i = 0; i < n; i++) {
           for (int j = 0; j < n; j++) {
               result.set(i, j, matriu.get(i, j));
           }
       }
        return result;
    }

    public Matriu multiplicar(Matriu b) {
        //suposam matrius quadrades NxN
        final int n = this.matriu.length;

        Matriu ret = new Matriu(n, n);

        //multiplicar
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int temp = 0;
                for (int k = 0; k < n; k++) {
                    temp += this.matriu[i][k] * b.matriu[k][j];
                }
                ret.matriu[i][j] = temp;
            }
        }

        return ret;
    }
    public void set(int i, int j, int value) {
        matriu[i][j] = value;
    }

    public int get(int i, int j) {
        return matriu[i][j];
    }


    public Matriu generarMatriu(int n){
        Matriu matrix = new Matriu(n, n);
        Random rn = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.set(i, j, rn.nextInt(1000));
            }
        }
        return matrix;
    }

    @Override
    public String toString() {
        return "Matriu{" +
                "matriu=" + Arrays.deepToString(this.matriu) +
                '}';
    }
}


