package Vista;

import Model.Idioma;
import controlador.Comunicar;
import controlador.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class Finestra extends JFrame implements Comunicar {
    private final Comunicar comunicar;

    private JComboBox<String> origen;
    private JComboBox<String> desti;
    private JTable taulaDistancies;
    private JPanel panellCalcul;
    private JPanel panellMatriu;
    private JPanel panellGrafics;

    private JSplitPane splitSuperior;
    private JSplitPane splitInferior;
    private JSplitPane splitGeneral;

    private Set<Idioma> idiomes;
    private DefaultTableModel modelDistancies;



    public Finestra() {
        super();
        this.comunicar = Main.getInstance();

        setTitle("Diferenciador d'idiomes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        idiomes = Main.getInstance().getDades().getIdiomes();

        splitSuperior = crearSplitSuperior();
        splitInferior = crearSplitInferior();

        splitGeneral = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitSuperior, splitInferior);

        getContentPane().add(splitSuperior);

        this.setVisible(true);
    }

    private JSplitPane crearSplitSuperior() {
        panellCalcul = new JPanel();
        panellCalcul.setLayout(new FlowLayout());
        panellCalcul.setBorder(BorderFactory.createTitledBorder("Opcions"));

        origen = new JComboBox<>();
        desti = new JComboBox<>();

        int n = idiomes.size();
        String[] columnes = new String[n + 1];
        columnes[0] = "";
        String[][] dades = new String[n][n + 1];
        int fila = 0;

        for (Idioma idiom : idiomes) {
            origen.addItem(idiom.toString());
            desti.addItem(idiom.toString());
            columnes[fila + 1] = idiom.toString();
            dades[fila][0] = idiom.toString();

            for (int col = 1; col <= n; col++) {
                dades[fila][col] = "-";
            }

            fila++;
        }

        origen.addItem("Tots");
        desti.addItem("Tots");

        panellCalcul.add(new JLabel("Idioma origen:"));
        panellCalcul.add(origen);
        panellCalcul.add(new JLabel("Idioma destí:"));
        panellCalcul.add(desti);

        JButton botoCalcular = new JButton("Calcular");
        botoCalcular.addActionListener(e -> {
            String idiomaOrigen = (String) origen.getSelectedItem();
            String idiomaDest = (String) desti.getSelectedItem();
            comunicar.comunicar("calcular:" + idiomaOrigen + ":" + idiomaDest);
        });
        panellCalcul.add(botoCalcular);

        panellMatriu = new JPanel(new BorderLayout());
        modelDistancies = new DefaultTableModel(dades, columnes);
        taulaDistancies = new JTable(modelDistancies);
        JScrollPane scroll = new JScrollPane(taulaDistancies);
        scroll.setBorder(BorderFactory.createTitledBorder("Matriu de distàncies"));
        panellMatriu.add(scroll, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panellCalcul, panellMatriu);
        split.setResizeWeight(0.3); // 30% per l'esquerra
        split.setDividerSize(6);

        return split;
    }


    private JSplitPane crearSplitInferior() {
        JPanel grafic1 = new JPanel();
        JPanel grafic2 = new JPanel();
        JPanel grafic3 = new JPanel();

        JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, grafic2, grafic3);
        JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, grafic1, split1);


        return split2;
    }

    @Override
    public void comunicar(String s) {

    }

    @Override
    public void actualitzar() {
        actualitzarMatriu();
    }

    private void actualitzarMatriu() {
        double[][] matriu = Main.getInstance().getDades().getDistancies();
        if (matriu == null) return;

        int n = matriu.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(matriu[i][j] != 0) {
                    modelDistancies.setValueAt(String.format("%.2f", matriu[i][j]), i, j + 1);
                }
            }
        }
    }
}
