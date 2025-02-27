import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
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
        procesos = new ArrayList <> ();
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
        procesos = new ArrayList <> ();
        registre.buidar();
    }
    /**
     * MIRAR ARRAY DE PRESOS, crec que falta boto per aturar
     */
    @Override
    public synchronized void comunicar(String s){
        int n = Integer.parseInt(s.split(":")[1]);

        if (s.startsWith("comencar:")) {
            System.out.println("C" + n);
        } else if (s.startsWith("suma:")) {
            System.out.println("S" + n);
        } else if (s.startsWith("multiplicar:")) {
            System.out.println("M" + n);
        }

    }

    public Dades getDades() {
        return registre;
    }
}
