package vista;

import controlador.Comunicar;
import controlador.Main;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Finestra extends JFrame implements Comunicar {
    public static final String ATURAR = "aturar";
    public static final String COMENÇAR = "començar";
    public static final String ESBORRAR = "esborrar";

    private final String[] opcionsAlgorisme = {"Força Bruta", "Dividir i vèncer"};
    private final Comunicar comunicar;
    private JTextField nPunts;


    private JComboBox<String> dimensio;
    private JComboBox<String> algorisme;
    private JComboBox<String> distancia;

    private Comunicar[] eixos;
    private Comunicar currentEixos;
    private JComboBox<Distribucio> distribucio;

    private JPanel eixosPanel;
    private CardLayout cardLayout;

    public Finestra() {
        super();
        this.comunicar = Main.instance;

        eixos = new Comunicar[]{new Eixos2D(750, 890), new Eixos3D()};
        currentEixos = eixos[0];

        setTitle("Distàncies a un núvol de punts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 900));

        JPanel panellBotons = crearPanellBotons();

        this.add(panellBotons, BorderLayout.NORTH);
        //currentEixos = eixos2D;
        //this.add((Component) currentEixos, BorderLayout.CENTER);
        cardLayout = new CardLayout();
        eixosPanel = new JPanel(cardLayout);
        eixosPanel.setBorder(BorderFactory.createEmptyBorder());

        eixosPanel.add((Component) eixos[0], Dimensio.D2.getEtiqueta());
        eixosPanel.add((Component) eixos[1], Dimensio.D3.getEtiqueta());
        this.add(eixosPanel, BorderLayout.CENTER);
        this.pack();
        setLocationRelativeTo(null);
        setVisible(true);

        cardLayout.show(eixosPanel, Dimensio.D2.getEtiqueta());

    }

    private JPanel crearPanellBotons() {
        JPanel panel = new JPanel();
        nPunts = new JTextField(5);
        panel.add(new JLabel("Nombre de Punts"));
        panel.add(nPunts);
        algorisme = generateComBoxAlgorisme();

        dimensio = generateDim();
        distribucio = generateComBoxDistr();
        distancia = generarDistCombox();

        ((JButton) panel.add(new JButton(COMENÇAR))).addActionListener(e -> {
            if (nPunts.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Si us plau, introdueix un número.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {

                int num = Integer.parseInt(nPunts.getText());
                if (num <= 0) {
                    JOptionPane.showMessageDialog(this, "Introdueix un numero major a 0", "Error", JOptionPane.WARNING_MESSAGE);

                } else {
                    if (num > 10000) {
                        int r = JOptionPane.showConfirmDialog(this, "El número que has introduït és molt gran. Pot ser difícil visualitzar la distància mínima", "AVÍS", JOptionPane.OK_CANCEL_OPTION );
                        if (r == JOptionPane.CANCEL_OPTION) {
                            return;

                        }
                    }
                    comunicar.comunicar("generar:" + num + ":" + distribucio.getSelectedItem() + ":p" + dimensio.getSelectedItem() + ":" + distancia.getSelectedItem() + ":" + algorisme.getSelectedItem());
                    currentEixos.comunicar("aturar");
                    cardLayout.show(eixosPanel, (String) dimensio.getSelectedItem());
                    currentEixos = eixos[dimensio.getSelectedIndex()];
                    currentEixos.comunicar("pintar");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);

            }

        });
        ((JButton) panel.add(new JButton(ATURAR))).addActionListener(e -> comunicar.comunicar(ATURAR));
        ((JButton) panel.add(new JButton(ESBORRAR))).addActionListener(e -> comunicar.comunicar(ESBORRAR));

        panel.add(dimensio);

        panel.add(distancia);
        panel.add(algorisme);
        panel.add(distribucio);
        return panel;
    }

    private JComboBox<String> generarDistCombox() {
        JComboBox<String> combox = new JComboBox<>();
        combox.addItem("Parella propera");
        combox.addItem("Parella llunyana");
        combox.setSelectedIndex(0);
        combox.addActionListener(e -> {
            String selected = Objects.requireNonNull(combox.getSelectedItem()).toString();
            comunicar.comunicar(selected);
            if (selected.equals("Parella llunyana")) {
                updateOptionsAlgorisme(new String[]{"Força Bruta"});
            } else {
                updateOptionsAlgorisme(opcionsAlgorisme);
            }
        });
        return combox;
    }

    private void updateOptionsAlgorisme(String[] opcions) {
        algorisme.removeAllItems();
        for (String opcio : opcions) {
            algorisme.addItem(opcio);
        }
        algorisme.setSelectedIndex(0);
    }


    private JComboBox<Distribucio> generateComBoxDistr() {
        JComboBox<Distribucio> comboBox = new JComboBox<>();

        comboBox.addItem(Distribucio.Uniforme);
        comboBox.addItem(Distribucio.Gaussiana);
        comboBox.addItem(Distribucio.Exponencial);
        comboBox.setSelectedIndex(0);

        return comboBox;

    }

    private JComboBox<String> generateDim() {

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem(Dimensio.D2.getEtiqueta());
        comboBox.addItem(Dimensio.D3.getEtiqueta());

        comboBox.setSelectedIndex(0);

        return comboBox;
    }

    private JComboBox<String> generateComBoxAlgorisme() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Força Bruta");
        comboBox.addItem("Dividir i vèncer");

        comboBox.setSelectedIndex(0); //default a classic

        return comboBox;

    }


    @Override
    public void comunicar(String s) {
        switch (s) {
            case "dibuixPunts":
            case "pintar":
                currentEixos.comunicar(s);
                break;
            default:
                break;
        }


    }
}
