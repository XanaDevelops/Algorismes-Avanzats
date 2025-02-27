public class Matriu {
    private final int[][] matriu;

    public Matriu(int m, int n) {
        this.matriu = new int[m][n];
    }


    public Matriu sumar(Matriu matriu) {
        int n = this.matriu.length;
       Matriu result = new Matriu(n, n );
       for (int i = 0; i < n; i++) {
           for (int j = 0; j < n; j++) {
               result.set(i, j, matriu.get(i, j));
           }
       }
        return result;
    }

    public Matriu multiplicar(Matriu matriu) {
        return null;
    }

    public void set(int i, int j, int value) {
        matriu[i][j] = value;
    }

    public int get(int i, int j) {
        return matriu[i][j];
    }



}


