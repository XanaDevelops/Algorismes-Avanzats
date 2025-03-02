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
        procesos = new ArrayList<>();
        registre.buidar();
        //
        registre.setTamanyN(1000);
//        for (int i = 2000; i < 10000; i = i + 1000) {
//            registre.setTamanyN(i);
//        }
        registre.setTamanyN(2000);
//        registre.setTamanyN(3000);
//        registre.setTamanyN(4000);
//        registre.setTamanyN(5000);

//        registre.setTamanyN(100);
//        registre.setTamanyN(200);
//        registre.setTamanyN(3000);
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
                preparar();
                procesos.add(new SumaM(this));
                procesos.add(new MultM(this));
                for (Comunicar proces : procesos) {
                    ((Thread) proces).start();
                }
            }
        } else if (s.startsWith("suma:")) {
            preparar();

            registre.setN(n);
            procesos.add(new SumaM(this));

            ((Thread) procesos.get(procesos.size() - 1)).start();
        } else if (s.startsWith("multiplicar:")) {

            registre.setN(n);
            Comunicar multiplicar = new MultM(this);
            procesos.add(multiplicar);
            ((Thread) procesos.get(procesos.size() - 1)).start();

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
}
