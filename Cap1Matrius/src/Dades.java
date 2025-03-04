import java.util.ArrayList;


public class Dades {

    private ArrayList<Long> tempsSuma;
    private ArrayList<Long> tempsMult;
    private ArrayList<Integer> tamanysN;

    private int N;

    public Dades() {
        tempsSuma = new ArrayList<>();
        tempsMult = new ArrayList<>();
        tamanysN = new ArrayList<>();

//        N = 1;
    }

    public void buidar() {
        tempsSuma.clear();
        tempsMult.clear();
        tamanysN.clear();
    }

    public int getTamTempsSuma() {
        return tempsSuma.size();
    }

    public int getTamTempsMult() {
        return tempsMult.size();
    }

    public int getSizeTamN() {
        return tamanysN.size();
    }

    public long getTempsSuma(int i) {
        return(tempsSuma.get(i));
    }

    public long getTempsMult(int i) {
        return(tempsMult.get(i));
    }

    public int getTamanyN(int i) {
        return tamanysN.get(i);
    }

    public void setTempsSuma(long t) {
        tempsSuma.add(t);
    }

    public void setTempsMult(long t) {
        tempsMult.add(t);
    }

    public void setTamanysN(int n) {
        tamanysN.add(n);
    }

    public int getMatriuN() {
        return N;
    }

    public void setMatriuN(int n) {
        N = n;
    }
}