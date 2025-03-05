import java.util.*;


public class Dades {

    public static class Resultat implements Comparable<Resultat> {
        private long temps;
        private int n;

        public Resultat(int n, long temps) {
            this.n = n;
            this.temps = temps;
        }

        @Override
        public int compareTo(Resultat o) {
            return Long.compare(n, o.n);
        }

        public long getTemps() {
            return temps;
        }
        public int getN() {
            return n;
        }
    }

    private final List<Resultat> resultatsSumar;
    private final List<Resultat> resultatsMult;

    //N màxima
    private int N;

    public static final int N_STEP = 10;

    public Dades() {
        resultatsSumar = new ArrayList<>();
        resultatsMult = new ArrayList<>();
    }

    public void buidarTot() {
        buidarSumar();
        buidarMult();
    }

    public void buidarSumar(){
        resultatsSumar.clear();
    }
    public void buidarMult(){
        resultatsMult.clear();
    }

    public synchronized void addResultatMultiplicar(long temps, int n){
        resultatsMult.add(new Resultat(n, temps));
    }

    public synchronized void addResultatSumar(long temps, int n){
        resultatsSumar.add(new Resultat(n, temps));
    }

    public synchronized List<Resultat> getSumes(){
        return resultatsSumar;
    }

    public synchronized List<Resultat> getMult(){
        return resultatsMult;
    }

    public int getMatriuN(){
        return N;
    }

    public void setMatriuN(int N) {
        this.N = N;
    }
}