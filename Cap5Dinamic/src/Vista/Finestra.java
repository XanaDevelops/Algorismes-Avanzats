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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;

public class Finestra extends JFrame implements Comunicar {
    protected enum Grafiques{
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
    private ArbreDistancies arbreDistancies;
    private DiagramaBarres diagramaBarres;
    private ArbreFiloLexic arbreFiloLexic;
    private final Comunicar comunicar;
    private final Dades dades;
    private final Set<Idioma> idiomes;

    private Map<Integer, BarraCarrega> barresMap = new TreeMap<>();
    private JPanel barresCarrega;
    private DefaultTableModel modelDistancies;
    protected final static int HEIGHT_PANELL = 200;
    protected final static int WIDTH_PANELL = 300;

    public Finestra() {
        super();
        this.comunicar =  Main.getInstance();

        this.dades = Main.getInstance().getDades();
        this.idiomes = dades.getIdiomes();

        setTitle("Diferenciador d'idiomes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
        setSize(1280, 720);
        setPreferredSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);

        JSplitPane splitSuperior = crearSplitSuperior();
        JSplitPane splitInferior = null;
        try {
            splitInferior = crearSplitInferior();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        JSplitPane splitGeneral = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitSuperior, splitInferior);
        splitGeneral.setResizeWeight(0.8);
        SwingUtilities.invokeLater(() -> splitGeneral.setDividerLocation(0.4));

        this.getContentPane().add(splitGeneral);
        this.setVisible(true);

        timer.start();
    }

    private JSplitPane crearSplitSuperior() {
        JPanel panellCalcul = new JPanel();
        panellCalcul.setLayout(new FlowLayout(FlowLayout.CENTER));
        panellCalcul.setBorder(BorderFactory.createTitledBorder("Opcions"));


        //Part opcions
        JPanel panelOpcions = new JPanel();
        panelOpcions.setLayout(new FlowLayout());

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

        panelOpcions.add(new JLabel("Idioma origen:"));
        panelOpcions.add(origen);
        panelOpcions.add(new JLabel("Idioma destí:"));
        panelOpcions.add(desti);

        //necesaris ara
        JCheckBox checkBox = new JCheckBox();
        JSlider slider = new JSlider();

        JButton botoCalcular = new JButton("Calcular");
        botoCalcular.addActionListener(e -> {
            calcularMain(origen, desti, checkBox.isSelected(), slider.getValue());
        });
        panelOpcions.add(botoCalcular);

        JButton botoAturar = new JButton("Aturar");
        botoAturar.addActionListener(e -> {
            final int[] ids = this.barresMap.keySet().stream().mapToInt(i -> i).toArray();
            for(int id: ids){
                this.barresMap.get(id).cancelar();
            }
            this.barresMap.clear();
            this.barresCarrega.removeAll();
        });
        panelOpcions.add(botoAturar);

        panellCalcul.add(panelOpcions);

        //part probabilistic
        JPanel panellProb = new JPanel();
        panellProb.setLayout(new FlowLayout());

        JLabel laberProb = new JLabel("% Probabilistic:");
        panellProb.add(laberProb);

        JLabel probText = new JLabel("");

        panellProb.add(checkBox);

        slider.addChangeListener(e -> {
            probText.setText(slider.getValue()+"%");
        });
        slider.setMinimum(1);
        slider.setMaximum(99);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);

        panellProb.add(slider);
        panellProb.add(probText);
        panellCalcul.add(panellProb);

        //part barres de carrega
        barresCarrega = new JPanel();
        barresCarrega.setBorder(BorderFactory.createTitledBorder("Tasques pendents"));
        barresCarrega.setLayout(new BoxLayout(barresCarrega, BoxLayout.Y_AXIS));
        barresCarrega.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(barresCarrega);

        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(375, 150));
        panellCalcul.add(scrollPane);


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
        SwingUtilities.invokeLater(() -> split.setDividerLocation(0.4d));

        return split;
    }

    private JPanel addDiagrama(Grafiques grafica) {
        JPanel panellGrafic = new JPanel(new BorderLayout());
        panellGrafic.setBorder(BorderFactory.createTitledBorder(grafica.titol));
        panellGrafic.setPreferredSize(new Dimension(WIDTH_PANELL, HEIGHT_PANELL));

        try {
            JPanel panelInterno = grafica.c.getConstructor().newInstance();

            switch (grafica) {
                case ARBRE_DIST:
                    this.arbreDistancies = (ArbreDistancies) panelInterno;
                    break;
                case ARBRE_FLEXIC:
                    this.arbreFiloLexic = (ArbreFiloLexic) panelInterno;
                    break;
                case DIA_BARRES:
                    this.diagramaBarres = (DiagramaBarres) panelInterno;
                    break;
            }
            JButton boto = new JButton("Actualitzar");
            boto.addActionListener(e -> {
                comunicar("actualitzar:" + grafica.titol);
            });
            panellGrafic.add(boto, BorderLayout.SOUTH);
            panellGrafic.add(panelInterno, BorderLayout.CENTER);
            return panellGrafic;

        } catch (Exception ex) {
            ex.printStackTrace();
            return new JPanel();
        }
    }

    private JSplitPane crearSplitInferior() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {


        ArrayList<JPanel> panells = new ArrayList<>();
        for (int i = 0; i < Grafiques.values().length; i++) {
            panells.add(addDiagrama(Grafiques.values()[i]));
        }

        if (panells.isEmpty()) {
            return new JSplitPane();
        }

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panells.get(0), panells.get(1));
        //split.setResizeWeight(1.0 / panells.size());
        final JSplitPane finalSplit = split;

        for (int i = 2; i < panells.size(); i++) {
            split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split, panells.get(i));
            //split.setResizeWeight(1.0 / (i + 1));
        }
        JSplitPane finalSplit1 = split;
        SwingUtilities.invokeLater(() -> {
            finalSplit.setDividerLocation(2.0/3.0);
            finalSplit1.setDividerLocation(2/3d);
        });


        return split;
    }

    private void calcularMain(JComboBox<String> origen, JComboBox<String> desti, boolean prob, int percent) {
        String idiomaOrigen = (String) origen.getSelectedItem();
        String idiomaDest = (String) desti.getSelectedItem();
        Idioma a = Idioma.valueOf(idiomaOrigen);
        Idioma b = Idioma.valueOf(idiomaDest);
        if(a==Idioma.TOTS && b==Idioma.TOTS) {
            comunicar.calcularTot(prob, percent);
            this.pintarArbreFiloLexic();

            this.actualitzarDiagBarres(Idioma.ESP); //per defecte
        }else if(a==b){
            JOptionPane.showMessageDialog(this, "Has intentat calcular la distància entre el mateix idioma.\nAquesta es 0.", "Avís", JOptionPane.WARNING_MESSAGE);
        }
        else{
            comunicar.calcular(a,b, prob, percent);
            this.actualitzarDiagBarres(a);
        }
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
    public synchronized void calcular(Idioma a, Idioma b, int id) {
        if (barresMap.containsKey(id)) {
            barresMap.get(id).iniciar();
        }else {

            BarraCarrega barra = new BarraCarrega("D("+a+","+b+") ", id);
            this.barresMap.put(id, barra);
            this.barresCarrega.add(barra);

        }
        this.revalidate();


    }

    @Override
    public void actualitzar(int id) {
        BarraCarrega b = this.barresMap.get(id);
        if (b!= null)
            b.end();
        actualitzarMatriu();
        this.revalidate();
    }

    @Override
    public void actualitzarDiagBarres(Idioma idioma) {
        if (diagramaBarres != null) {
            diagramaBarres.actualitzarDiagBarres(idioma);
        } else {
            System.err.println("La instancia de DiagramaBarres no está inicializada.");
        }
    }


    private void actualitzarMatriu() {
        double[][] matriu = dades.getDistancies();
        if (matriu == null) return;

        int n = matriu.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(matriu[i][j] != 0) {
                    modelDistancies.setValueAt(String.format("%.4f", matriu[i][j]), i, j + 1);
                }
            }
        }
    }

    @Override
    public void aturar(int id) {
        BarraCarrega barra = this.barresMap.remove(id);
        if (barra != null) {
            this.barresCarrega.remove(barra);
        }
        this.revalidate();
        this.repaint();
    }

    Timer timer = new Timer(1000/30, e -> {
        for(Map.Entry<Integer, BarraCarrega> entry: barresMap.entrySet()){
            entry.getValue().tick();
        }
    });

    @Override
    public void pintarArbreFiloLexic(){
        arbreFiloLexic.pintarArbreFiloLexic();
    }
}
