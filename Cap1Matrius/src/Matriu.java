public class Matriu {
    private final int[][] matriu;

    public Matriu(int m, int n) {
        this.matriu = new int[m][n];
    }


    public Matriu sumar(Matriu matriu) {
        return null;
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

    public String toString() {
        String ret = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ret += matriu[i][j] + "\t";
            }
            ret += "\n";
        }
        return ret;
    }

}


