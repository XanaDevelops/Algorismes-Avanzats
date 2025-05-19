package Vista;

import Model.Dades;
import Model.Idioma;
import controlador.Comunicar;
import controlador.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public class Finestra extends JFrame implements Comunicar {
    private enum Grafiques{
        ARBRE_DIST("Arbre distàncies", ArbreDistancies.class),
        ARBRE_FLEXIC("Arbre FiloLexic", ArbreFiloLexic.class),
        DIA_BARRES("Diagrama Barres", DiagramaBarres.class);


        String titol;
        Class<? extends JPanel> c;
        Grafiques(String title, Class<? extends JPanel> clazz){
            this.titol = title;
            this.c = clazz;
        }
    }
    private final Comunicar comunicar;
    private final Dades dades;
    private final Set<Idioma> idiomes;


    private DefaultTableModel modelDistancies;


    public Finestra() {
        super();
        this.comunicar = Main.getInstance();
        this.dades = Main.getInstance().getDades();
        this.idiomes = dades.getIdiomes();

        setTitle("Diferenciador d'idiomes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JSplitPane splitSuperior = crearSplitSuperior();
        JSplitPane splitInferior = null;
        try {
            splitInferior = crearSplitInferior();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        JSplitPane splitGeneral = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitSuperior, splitInferior);
        splitGeneral.setResizeWeight(0.3);

        this.getContentPane().add(splitGeneral);
        this.setVisible(true);
    }

    private JSplitPane crearSplitSuperior() {
        JPanel panellCalcul = new JPanel();
        panellCalcul.setLayout(new FlowLayout());
        panellCalcul.setBorder(BorderFactory.createTitledBorder("Opcions"));

        JComboBox<String> origen = new JComboBox<>();
        JComboBox<String> desti = new JComboBox<>();

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
                dades[fila][col] = fila==(col-1)? "": "-";
            }

            fila++;
        }

        origen.addItem("TOTS");
        desti.addItem("TOTS");

        panellCalcul.add(new JLabel("Idioma origen:"));
        panellCalcul.add(origen);
        panellCalcul.add(new JLabel("Idioma destí:"));
        panellCalcul.add(desti);

        JButton botoCalcular = new JButton("Calcular");
        botoCalcular.addActionListener(e -> {
            String idiomaOrigen = (String) origen.getSelectedItem();
            String idiomaDest = (String) desti.getSelectedItem();
            Idioma a = Idioma.valueOf(idiomaOrigen);
            Idioma b = Idioma.valueOf(idiomaDest);
            if(a==Idioma.TOTS && b==Idioma.TOTS) {
                comunicar.calcularTot();
            }else if(a==b){
                JOptionPane.showMessageDialog(this, "Has intentat calcular la distància entre el mateix idioma.\nAquesta es 0.", "Avís", JOptionPane.WARNING_MESSAGE);
            }
            else{
                comunicar.calcular(a,b);
            }
        });
        panellCalcul.add(botoCalcular);


        modelDistancies = new DefaultTableModel(dades, columnes){
            //Impedeix editar els valors
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable taulaDistancies = new JTable(modelDistancies){
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int column) {
                Component c = super.prepareRenderer(r, row, column);
                if ((row+1) == column){
                    c.setBackground(Color.BLACK);
                }else if(row %2 == 0){
                    c.setBackground(Color.LIGHT_GRAY);
                }
                else{
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        taulaDistancies.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(taulaDistancies);
        scroll.setBorder(BorderFactory.createTitledBorder("Matriu de distàncies"));

        JPanel panellMatriu = new JPanel(new BorderLayout());
        panellMatriu.add(scroll, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panellCalcul, panellMatriu);
        split.setResizeWeight(0.3);
        split.setDividerSize(6);

        return split;
    }

    private JSplitPane crearSplitInferior() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ArrayList<JPanel> panells = new ArrayList<>();

        for(Grafiques g : Grafiques.values()) {
            JPanel panellGrafic = new JPanel(new BorderLayout());
            panellGrafic.setBorder(BorderFactory.createTitledBorder(g.titol));
            panellGrafic.setPreferredSize(new Dimension(300, 200));

            panellGrafic.add(g.c.getConstructor().newInstance(), BorderLayout.NORTH);

            JButton boto = new JButton("Actualitzar");
            boto.addActionListener(e -> {
               comunicar("actualitzar:" + g.titol);
            });
            panellGrafic.add(boto, BorderLayout.SOUTH);

            panells.add(panellGrafic);
        }

        if (panells.isEmpty()) {
            return new JSplitPane();
        }

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panells.get(0), panells.get(1));
        split.setResizeWeight(1.0 / panells.size());

        for (int i = 2; i < panells.size(); i++) {
            split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split, panells.get(i));
            split.setResizeWeight(1.0 / (i + 1));
        }

        return split;
    }

    @Override
    public void comunicar(String s) {
        switch (s) {
            case "actualitzar":
                // actualitzar gràfics
                break;
            default:
                System.err.println("Finestra missatge? :" + s);
                break;
        }
    }

    @Override
    public void actualitzar() {
        actualitzarMatriu();
    }

    private void actualitzarMatriu() {
        double[][] matriu = dades.getDistancies();
        if (matriu == null) return;

        int n = matriu.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(matriu[i][j] != 0) {
                    modelDistancies.setValueAt(String.format("%.4f", matriu[i][j]), i, j);
                }
            }
        }
    }
}
