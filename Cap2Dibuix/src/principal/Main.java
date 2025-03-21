package principal;
import model.Dades;
import model.Tipus;
import model.solvers.SierpinskiSolver;
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
            case "pintar", "tempsReal", "tempsEsperat":
                finestra.comunicar(s);
                break;
            case "executar":
                switch (params[1]){
                    case "tromino":
                        for (Comunicar enmarxa : processos) {
                            enmarxa.comunicar("aturar");
                        }

                        processos.clear();
                        int midaT = Integer.parseInt(params[2]);
                        dades.setProfunditat(midaT);

                        TrominoSolver tS = new TrominoSolver(this, dades);

                        processos.add(tS);
                        executor.execute(tS);
                        break;
                    case "triangles":
                        for (Comunicar enmarxa : processos) {
                            enmarxa.comunicar("aturar");
                        }

                        processos.clear();
                        int midaS = (int) Math.pow(2, Integer.parseInt(params[2]));
                        dades.setProfunditat(midaS);

                        SierpinskiSolver sS = new SierpinskiSolver(this, dades);

                        processos.add(sS);
                        executor.execute(sS);
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