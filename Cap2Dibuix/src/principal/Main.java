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
            case "N":
                dades.createMatrix( Integer.parseInt(params[1]));
                break;
            case "pintar":
                finestra.comunicar(s);
                break;
            case "executar":
                switch (params[1]){
                    case "tromino":
                        System.out.println("-------------------------------");

                        int[][] matrix = {
                            {  1,  1,  0,  0 },
                            {  1, -1,  0,  0 },
                            {  0,  0,  0,  0 },
                            {  0,  0,  0,  0 }
                    };
                     dades.setTauler(matrix);
                     esperar(1000);
                        matrix = new int[][]{
                                {  1,  1,  0,  0 },
                                {  1, -1,  0,  0 },
                                {  2,  0,  0,  0 },
                                {  2,  2,  0,  0 }
                        };
                        dades.setTauler(matrix);
                        esperar(1000);

                        matrix = new int[][]{
                                {  1,  1,  0,  0 },
                                {  1, -1,  0,  0 },
                                {  2,  0,  0,  3 },
                                {  2,  2,  3,  3 }
                        };
                        dades.setTauler(matrix);
                        esperar(1000);
                        matrix = new int[][]{
                                {  1,  1,  0,  0 },
                                {  1, -1,  4,  0 },
                                {  2,  4,  4,  3 },
                                {  2,  2,  3,  3 }
                        };
                        dades.setTauler(matrix);
                        esperar(1000);






                        //finestra.comunicar(s);
                        break;
                    default:
                        System.out.println("main, comunicar no implementat");
                }
        }
    }

    private void esperar(int i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}