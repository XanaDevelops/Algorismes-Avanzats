import java.util.ArrayList;


public class Dades {

    private ArrayList<Long> tempsSuma;
    private ArrayList<Long> tempsMult;
    private ArrayList<Integer> tamanyN;

    private int N;

    public Dades() {
        tempsSuma = new ArrayList<>();
        tempsMult = new ArrayList<>();
        tamanyN = new ArrayList<>();

        N = 1;
    }

    public void buidar() {
        tempsSuma.clear();
        tempsMult.clear();
        tamanyN.clear();
    }

    public int getTamTempsSuma() {
        return tempsSuma.size();
    }

    public int getTamTempsMult() {
        return tempsMult.size();
    }

    public int getTamN() {
        return tamanyN.size();
    }

    public long getTempsSuma(int i) {
        return(tempsSuma.get(i));
    }

    public long getTempsMult(int i) {
        return(tempsMult.get(i));
    }

    public int getTamanyN(int i) {
        return tamanyN.get(i);
    }

    public void setTempsSuma(long t) {
        tempsSuma.add(t);
    }

    public void setTempsMult(long t) {
        tempsMult.add(t);
    }

    public void setTamanyN(int n) {
        tamanyN.add(n);
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }
}
