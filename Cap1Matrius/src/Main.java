import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;

/**
 * @author ferri
 */
public class Main implements Comunicar {

    private FinestraMatriu finestra;
    private ArrayList<Comunicar> procesos = null;
    private Dades registre = null;
    private ExecutorService executorService;

    public static void main(String[] args) {
        (new Main()).inicio();
    }

    private void inicio() {
        registre = new Dades();
        procesos = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(2);

        preparar();
        JFrame frame = new JFrame("Gràfic Suma vs Mult Matrius");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra = new FinestraMatriu(this);
        frame.setContentPane(finestra);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void preparar() {
        if(procesos != null && !procesos.isEmpty()) {
            for(Comunicar comunicar : procesos) {
                comunicar.comunicar("aturar");
            }
        }
        procesos = new ArrayList<>();
        registre.buidarTot();

    }

    @Override
    public synchronized void comunicar(String s) {
        int n = 0;
        if (s.split(":").length > 1) {
            n = Integer.parseInt(s.split(":")[1]);
        }

        if (s.startsWith("comencar:")) {

            for (Comunicar enmarxa : procesos) {
                enmarxa.comunicar("aturar");
            }

            // Fer net dades
            procesos.clear();
            registre.buidarTot();

            registre.setMatriuN(n);

            SumaM sumaTask = new SumaM(this);
            MultM multTask = new MultM(this);

            procesos.add(sumaTask);
            procesos.add(multTask);

            // Enviar al executor service
            executorService.submit(sumaTask);
            executorService.submit(multTask);
        } else if (s.startsWith("suma:")) {
            for (Comunicar enmarxa : procesos) {
                if (enmarxa instanceof SumaM){
                    enmarxa.comunicar("aturar");
                }
            }
            procesos.removeIf(comunicar -> comunicar instanceof SumaM);
            registre.buidarSumar();
            registre.setMatriuN(n);

            SumaM sumar = new SumaM(this);
            procesos.add(sumar);
            executorService.submit(sumar);

        } else if (s.startsWith("multiplicar:")) {
            for(Comunicar enmarxa : procesos) {
                if(enmarxa instanceof MultM){
                    enmarxa.comunicar("aturar");
                }
            }
            procesos.removeIf(comunicar -> comunicar instanceof MultM);
            registre.buidarMult();
            registre.setMatriuN(n);

            MultM multiplicar = new MultM(this);
            procesos.add(multiplicar);
            executorService.submit(multiplicar);

        } else if (s.contentEquals("pintar")) {
            finestra.comunicar("pintar");
        }else if (s.startsWith("aturar:")){
            for (int i = 0; i < procesos.size(); i++) {
                procesos.get(i).comunicar("aturar");
            }
        }


    }

    public Dades getDades() {
        return registre;
    }

    //preguntar
    public FinestraMatriu getFinestra() {
        return finestra;
    }
}
