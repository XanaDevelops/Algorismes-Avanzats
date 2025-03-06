import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JFrame;

/**
 * @author ferri
 */
public class Main implements Comunicar {

    private FinestraMatriu finestra;
    private ArrayList<Comunicar> procesos = null;
    private Dades registre = null;
    private ExecutorService executorService;

    private dibuixConstantMult finestraCM;
    public static void main(String[] args) {
        (new Main()).inicio();
    }

    private void inicio() {
        registre = new Dades();
        procesos = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(3);

        preparar();

        //interficie en un altre thread
        executorService.submit(this::crearInterficie);

    }

    private void crearInterficie(){
        JFrame frame = new JFrame("Gràfic Suma vs Mult Matrius");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra = new FinestraMatriu(this);
        frame.setContentPane(finestra);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //centrat i a la part més dreta
        frame.setLocation(
                frame.getToolkit().getScreenSize().width-frame.getWidth() ,
                frame.getToolkit().getScreenSize().height / 2 - frame.getHeight() / 2
        );


        finestraCM = new dibuixConstantMult(this);
        finestraCM.setVisible(true);

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
            finestraCM.comunicar("netejaTaules");

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
            executorService.execute(sumaTask);
            executorService.execute(multTask);

        } else if (s.startsWith("suma:")) {
            for (Comunicar enmarxa : procesos) {
                if (enmarxa instanceof SumaM){
                    enmarxa.comunicar("aturar");
                }
            }
            procesos.removeIf(comunicar -> comunicar instanceof SumaM);
            registre.buidarSumar();
            finestraCM.comunicar("netejaTaules");

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
            finestraCM.comunicar("netejaTaules");


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
        }else if (s.contentEquals("netejaTaules")){
            finestraCM.comunicar("netejaTaules");
        }
//afegir netejarTaules

    }

    public Dades getDades() {
        return registre;
    }

    public dibuixConstantMult getFinestraCM() {
        return finestraCM;
    }

    //preguntar
    public FinestraMatriu getFinestra() {
        return finestra;
    }
}
