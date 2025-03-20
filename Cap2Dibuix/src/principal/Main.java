package principal;
import model.Dades;
import vista.Finestra;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {

    private Comunicar finestra;
    private Dades dades;
    private Comunicar solver;

    private final ExecutorService executor = Executors.newFixedThreadPool(16);;

    public int[][] getMatriu() {
        return dades.getTauler();
    }

    private void init(){
        dades = new Dades();

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
                        System.out.println("-------------------------------");
                        int[][] matrix = {
                            {  1,  1,  5,  5 },
                            {  1, -1,  4,  5 },
                            {  2,  4,  4,  3 },
                            {  2,  2,  3,  3 }
                    };;
                     dades.setTauler(matrix);
                        finestra.comunicar(s);
                        break;
                    default:
                        System.out.println("main, comunicar no implementat");
                }
        }
    }
}