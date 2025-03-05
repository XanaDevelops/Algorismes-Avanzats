import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * @author ferri
 */
public class Main implements Comunicar {

    private FinestraMatriu finestra;
    private ArrayList<Comunicar> procesos = null;
    private Dades registre = null;

    public static void main(String[] args) {
        (new Main()).inicio();
    }

    private void inicio() {
        registre = new Dades();
        procesos = new ArrayList<>();
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
    /**
     * MIRAR ARRAY DE PRESOS, crec que falta boto per aturar
     */
    @Override
    public synchronized void comunicar(String s) {
        int n = 0;
        if (s.split(":").length > 1) {
            n = Integer.parseInt(s.split(":")[1]);
        }

        if (s.startsWith("comencar:")) {
            int vius = 0;
            for (Comunicar proceso : procesos) {
                if (((Thread) proceso).isAlive()) {
                    vius++;
                }
            }
            if (vius == 0) {
                registre.setMatriuN(n);

                procesos.add(new SumaM(this));
                procesos.add(new MultM(this));
                for (Comunicar proces : procesos) {
                    ((Thread) proces).start();
                }
            }
        } else if (s.startsWith("suma:")) {
            registre.buidarSumar();

            registre.setMatriuN(n);
            procesos.add(new SumaM(this));

            ((Thread) procesos.get(procesos.size() - 1)).start();
        } else if (s.startsWith("multiplicar:")) {
            registre.buidarMult();

            registre.setMatriuN(n);
            Comunicar multiplicar = new MultM(this);
            procesos.add(multiplicar);
            ((Thread) procesos.get(procesos.size() - 1)).start();

        } else if (s.contentEquals("pintar")) {
            finestra.comunicar("pintar");
        }else if (s.startsWith("aturar:")){
            for (Comunicar proceso : procesos) {
                proceso.comunicar("aturar");
            }
        }else if(s.startsWith("net:")){
            for (Comunicar proceso : procesos) {
                proceso.comunicar("aturar");
            }
            registre.buidarTot();
            finestra.comunicar("pintar");
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
