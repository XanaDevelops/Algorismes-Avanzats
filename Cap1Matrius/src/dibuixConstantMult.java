import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Titrit
 */
public class dibuixConstantMult extends JFrame implements Comunicar{

    private Main main;
    // Crear los modelos para las tablas

    private DefaultTableModel modelSuma;
    private DefaultTableModel modelMult;

    // Crear las tablas
    private JTable taulaSuma;
    private JTable taulaMult;

    // Constructor
    public dibuixConstantMult(Main main) {
        this.main = main;
        setTitle("TEMPS ESPERAT VS REAL");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(2, 1));

        // Crear los modelos para las tablas
        modelSuma = new DefaultTableModel(new Object[]{"n", "Temps Real (ms)", "Temps Esperat (ms)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; ///cel·les no editables
            }
        };

        modelMult = new DefaultTableModel(new Object[]{"n", "Temps Real (ms)", "Temps Esperat (ms)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //cel·les no editables
            }
        };

        setResizable(false);


        // Crear las tablas
        taulaSuma = new JTable(modelSuma);
        taulaMult = new JTable(modelMult);

        // Envolver las tablas con paneles con título
        JPanel panelSuma = new JPanel(new BorderLayout());
        panelSuma.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Suma de Matrius", TitledBorder.CENTER, TitledBorder.TOP));
        panelSuma.add(new JScrollPane(taulaSuma), BorderLayout.CENTER);

        JPanel panelMult = new JPanel(new BorderLayout());
        panelMult.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Multiplicació de Matrius", TitledBorder.CENTER, TitledBorder.TOP));
        panelMult.add(new JScrollPane(taulaMult), BorderLayout.CENTER);

        // Añadir los paneles a la ventana
        add(panelSuma);
        add(panelMult);
        setLocation(0, getToolkit().getScreenSize().height / 2 - getHeight() / 2);

    }

    public synchronized void afegirFilaSuma(int n, String real, String esperat){
        modelSuma.addRow(new Object[]{n, real, esperat});
        taulaSuma.setVisible(true);
        revalidate();
        repaint();
    }

    public synchronized void afegirFilaMutl(int n, String real, String esperat){
        modelMult.addRow(new Object[]{n, real, esperat});
        taulaMult.setVisible(true);
        revalidate();
        repaint();
    }

    public void cleanTables(){
        modelSuma.setRowCount(0);
        modelMult.setRowCount(0);
    }


    @Override
    public void comunicar(String s) {
        if (s.contentEquals("netejaTaules")){
            cleanTables();
        }
    }
}
