package principal;
import model.Dades;
import model.Tipus;
import model.solvers.TrominoSolver;
import vista.Finestra;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {

    private Comunicar finestra;
    private Dades dades;
    private ArrayList<Comunicar> processos = null;

    private final ExecutorService executor = Executors.newFixedThreadPool(16);;

    public int[][] getMatriu() {
        return dades.getTauler();
    }

    private void init(){
        dades = new Dades();
        processos = new ArrayList<>();

        //generar finestra
        executor.execute(() -> {
            finestra = new Finestra(this, dades);
        });

    }

    public static void main(String[] args) {
        (new Main()).init();
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {
        String[] params = s.split(":");

        switch (params[0]) {
            case "pintar":
                finestra.comunicar(s);
                break;
            case "executar":
                switch (params[1]){
                    case "tromino":
                        for (Comunicar enmarxa : processos) {
                            enmarxa.comunicar("aturar");
                        }

                        processos.clear();
                        int mida = Integer.parseInt(params[2]);
                        dades.setTauler(new int[mida][mida]);
                        dades.setProfunditat(mida);
                        dades.setTipus(Tipus.TROMINO);

                        TrominoSolver t = new TrominoSolver(this, dades);

                        processos.add(t);
                        executor.execute(t);
                        break;
                    default:
                        break;
                }
            case "aturar":
                for (Comunicar proces : processos) {
                    proces.comunicar("aturar");
                }
                break;
            default:
                break;
        }
    }
}