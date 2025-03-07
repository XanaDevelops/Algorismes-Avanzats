import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;

/**
 * Classe principal del programa
 * Crea i gestiona la comunicació entre els diferents elements del programa
 *
 * @authors Josep Ferriol, Daniel García Vázquez, Biel Perelló, Khaoula Ikkene.
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
        //interfície en un altre thread
        executorService.submit(this::crearInterficie);

    }

    private void crearInterficie() {
        JFrame frame = new JFrame("Gràfic Suma vs Mult Matrius");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra = new FinestraMatriu(this);
        frame.setContentPane(finestra);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //centrat i a la part més dreta de la pantalla
        frame.setLocation(
                frame.getToolkit().getScreenSize().width - frame.getWidth(),
                frame.getToolkit().getScreenSize().height / 2 - frame.getHeight() / 2
        );

        //Conté les taules comparatives del temps real vs esperat
        finestraCM = new dibuixConstantMult(this);
        finestraCM.setVisible(true);

    }

    /**
     * Prepara els processos a la seva execució.
     */
    private void preparar() {
        if (procesos != null && !procesos.isEmpty()) {
            for (Comunicar comunicar : procesos) {
                comunicar.comunicar("aturar");
            }
        }
        procesos = new ArrayList<>();
        registre.buidarTot();

    }

    @Override
    public synchronized void comunicar(String s) {
        //Mida de la matriu
        int n = 0;
        if (s.split(":").length > 1) {
            n = Integer.parseInt(s.split(":")[1]);
        }
        String split = s.split(":")[0];
        switch (split) {
            case "comencar":
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
                break;

            case "suma":

                try {
                    executaClass(SumaM.class, n);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "multiplicar":

                try {
                    executaClass(MultM.class, n);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "atura":
                for (Comunicar process : procesos) {
                    process.comunicar("aturar");
                }
                break;

            case "net":
                for (Comunicar proceso : procesos) {
                    proceso.comunicar("aturar");
                }
                registre.buidarTot();
            case "pintar":
                finestra.comunicar("pintar");
                break;

            default:
                break;

        }

    }

    /**
     * Executa un procés de la classe SumaM o MultM
     * @param classe nom de la classe del fil a crear i executar
     * @param n mida de la matriu
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void executaClass(Class<?> classe, int n) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Comunicar enmarxa : procesos) {
            if (enmarxa instanceof MultM) {
                enmarxa.comunicar("aturar");
            }
        }

        procesos.removeIf(comunicar -> {
            if (classe.isInstance(comunicar)) {
                comunicar.comunicar("aturar");
                return true;
            }
            return false;
        });
        if (classe.equals(MultM.class)) {
            registre.buidarMult();

        } else {
            registre.buidarSumar();
        }

        finestraCM.comunicar("netejaTaules");

        registre.setMatriuN(n);
        Comunicar proces = (Comunicar) classe.getConstructor(Main.class).newInstance(this);
        procesos.add(proces);

        executorService.submit((Runnable) proces);

    }

    public Dades getDades() {
        return registre;
    }

    public dibuixConstantMult getFinestraCM() {
        return finestraCM;
    }

    public FinestraMatriu getFinestra() {
        return finestra;
    }
}
