package principal;

import vista.Finestra;

public class Main implements Comunicar {
    Finestra finestra;

    public Main(){
        finestra = new Finestra(this);
    }

    public static void main(String[] args) {
        new Main();
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}