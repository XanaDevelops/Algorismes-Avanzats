import java.util.*;

/**
 * Classe que manipula les dades del programa; les mides, el temps de les operacions
 * de suma i multiplicació, i les constants multiplicatives de les matrius.
 */
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

        public double getLogTemps() {
            return Math.log10(temps);
        }

        public int getN() {
            return n;
        }

        public String getTempsPrint() {
            double num =n*Math.pow(10, -3);
            return (Math.round(num * 10000.0) / 10000.0) + "us";
        }
    }

    private final List<Resultat> resultatsSumar;
    private final List<Resultat> resultatsMult;

    //N màxima
    private int N;
    /**
     * Constant multplicativa de suma
     */

    private Double CSuma;
    /**
     * Constant multplicativa de multiplicació
     */
    private Double CMult;
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

    public Double getCSuma() {
        return CSuma;
    }

    public void setCSuma(Double CSuma) {
        this.CSuma = CSuma;
    }

    public Double getCMult() {
        return CMult;
    }

    public void setCMult(Double CMult) {
        this.CMult = CMult;
    }


    public boolean jaInicialitzada(Double C) throws  NullPointerException{
        return (!String.valueOf(C).equals("null"));
    }

    public void setMatriuN(int N) {
        this.N = N;
    }
}