import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Classe que dibuixa una finestra per mostrar els temps esperats i reals
 * de les operacions de suma i multiplicació de matrius.
 */
public class dibuixConstantMult extends JFrame implements Comunicar{

    /**
     * Models per a les taules de suma i multiplicació.
     */
    private DefaultTableModel [] models = new DefaultTableModel[2];
    /**
     * Array de les taules de suma i multiplicació de matrius.
     */
    private JTable [] taules = new JTable[2];

    private final static int INDEX_SUMA = 0;
    private final static int INDEX_MULT = 1;

    /**
     * Constructor de la classe. Inicialitza la finestra i configura les taules.
     *
     */
    public dibuixConstantMult() {
        setTitle("TEMPS ESPERAT VS REAL");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(2, 1));

        JPanel[] panels = new JPanel[2];
        String[] titols = {"Suma de Matrius", "Multiplicació de Matrius"};
        for (int i = 0; i < taules.length; i++) {
            models[i] = new DefaultTableModel(new Object[]{"n", "Temps Real (ms)", "Temps Esperat (ms)"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; //cel·les no editables
                }
            };
            taules[i] = new JTable(models[i]);
            panels[i]=new JPanel(new BorderLayout());
            panels[i].setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titols[i], TitledBorder.CENTER, TitledBorder.TOP));
            panels[i].add(new JScrollPane(taules[i]), BorderLayout.CENTER);
            this.add(panels[i]);

        }

        setResizable(false);
        // centrada i a la part més esquerra de la pantalla
        setLocation(0, getToolkit().getScreenSize().height / 2 - getHeight() / 2);
    }

    public synchronized void afegirFilaSuma(int n, String real, String esperat){
        models[INDEX_SUMA].addRow(new Object[]{n, real, esperat});
        taules[INDEX_SUMA].setVisible(true);
        revalidate();
        repaint();
    }

    public synchronized void afegirFilaMutl(int n, String real, String esperat){
        models[INDEX_MULT].addRow(new Object[]{n, real, esperat});
        taules[INDEX_MULT].setVisible(true);
        revalidate();
        repaint();
    }

    public void cleanTables(){
        for (DefaultTableModel model : models) {
            model.setRowCount(0);
        }

    }


    @Override
    public void comunicar(String s) {
        if (s.contentEquals("netejaTaules")){
            cleanTables();
        }
    }
}
