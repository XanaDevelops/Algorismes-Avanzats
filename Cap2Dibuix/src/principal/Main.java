package principal;

import model.Dades;

import model.solvers.SierpinskiCarpetSolver;
import model.solvers.SierpinskiTriangleSolver;
import model.solvers.TreeSolver;
import model.solvers.TrominoSolver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {

    private Comunicar finestra;

    private Dades dades;
    private ArrayList<Comunicar> processos = null;

    private final ExecutorService executor = Executors.newFixedThreadPool(16);

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
            case "N":
                int n = Integer.parseInt(params[1]);
                dades.createMatrix(n);
                finestra.comunicar("pintar");
                break;
            case "inici":
                dades.setForatTromino(Integer.parseInt(params[1]),Integer.parseInt(params[2]));
                //volem recalcular els trominos
                this.comunicar("executar:tromino:"+(int)(Math.log(dades.getProfunditat()) / Math.log(2)));
                break;
            case "executar":
                switch (params[1]) {
                    case "tromino":
                        try {
                            executar(TrominoSolver.class, (int) Math.pow(2, Integer.parseInt(params[2])));
                        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                                 InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "triangles":
                        dades.setInici(0,0);
                        try {
                            executar(SierpinskiTriangleSolver.class,(int) Math.pow(2, (double)Integer.parseInt(params[2])-1));
                        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                                 IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "quadrat":
                        try {
                            executar(SierpinskiCarpetSolver.class, (int) Math.pow(3, Integer.parseInt(params[2])));
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "arbre":
                        try {
                            executar(TreeSolver.class, (int) Integer.parseInt(params[2]));
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        System.err.println("MAIN: execucio "+s+" desconegut");
                        break;
                }
            case "aturar":
                for (Comunicar proces : processos) {
                    proces.comunicar("aturar");
                }
                break;
            case "borrar":
                this.comunicar("aturar");
                dades.clean();
                finestra.comunicar("pintar");
                break;
            default:
                System.err.println("MAIN: missatge "+s+" desconegut");
                break;
        }
    }

    private void executar(Class<? extends Comunicar> clase, int profunditat) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Comunicar enmarxa : processos) {
            enmarxa.comunicar("aturar");
        }

        processos.clear();

        dades.setProfunditat(profunditat);
        Comunicar proces = (Comunicar) clase.getConstructor(Main.class, Dades.class).newInstance(this, dades);
        processos.add(proces);
        executor.execute((Runnable) proces);
    }

    public Dades getDades() {
        return dades;
    }

    public void setDades(Dades dades) {
        this.dades = dades;
    }
}