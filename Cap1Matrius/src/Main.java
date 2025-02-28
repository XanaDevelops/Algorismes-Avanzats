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
        //
        registre.setTamanyN(10);
    }
    /**
     * MIRAR ARRAY DE PRESOS, crec que falta boto per aturar
     */
    @Override
    public synchronized void comunicar(String s){
        if (s.split(":").length>1) {
            int n = Integer.parseInt(s.split(":")[1]);
        }

        if (s.startsWith("comencar:")) {
            int vius = 0;
            for(int i=0;i<procesos.size();i++) {
                if (((Thread)procesos.get(i)).isAlive()) {
                    vius++;
                }
            }
            if (vius == 0) {
                preparar();
                procesos.add(new SumaM(this));
//                procesos.add(new MultM(this));
                for (int i = 0; i < procesos.size(); i++) {
                    ((Thread) procesos.get(i)).start();
                }
            }

        } else if (s.startsWith("suma:")) {
            preparar();
            procesos.add(new SumaM(this));
            ((Thread) procesos.get(procesos.size()-1)).start();


        } else if (s.startsWith("multiplicar:")) {
//            System.out.println("M" + n);
        }else if (s.contentEquals("pintar")){
            finestra.comunicar("pintar");
        }

    }

    public Dades getDades() {
        return registre;
    }
}
