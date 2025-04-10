package vista;

import controlador.Comunicar;
import model.Dades;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {
    public static final String ATURAR = "aturar";
    public static final String COMENÇAR = "començar";
    public static final String ESBORRAR = "esborrar";
    JTextField nPunts;
    Comunicar comunicar;
    JComboBox<String> dimensio;
    Dades dades;
    JComboBox<String> algorime;
    Eixos eixos;
    JComboBox<Distribucio> distribucio;
    JPanel panellBotons;
    JComboBox<String> distancia;

    public Finestra(Comunicar comunicar, Dades dades) {
        super();
        this.comunicar = comunicar;
        this.dades = dades;
        eixos = new Eixos(750, 890, dades);
        this.setTitle("Distàncies a un nuvol de punts");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(900, 900));

        panellBotons = new JPanel();
        nPunts = new JTextField(5);


        panellBotons.add(new JLabel("Nombre de Punts"));

        panellBotons.add(nPunts);

        ((JButton) panellBotons.add(new JButton(COMENÇAR))).addActionListener(e -> {
                    if (nPunts.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Si us plau, introdueix un número.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try{

                        int num = Integer.parseInt(nPunts.getText());
                        if (num <= 0) {
                            JOptionPane.showMessageDialog(this, "Introdueix un numero major a 0", "Error", JOptionPane.WARNING_MESSAGE);

                        }else{
                            if (num>10000){
                                int r = JOptionPane.showConfirmDialog(this, "El número que has introduït és molt gran. Pot ser difícil visualitzar la distància mínima");
                                if (r == JOptionPane.CANCEL_OPTION){
                                    return;

                                }
                            }
                            comunicar.comunicar("generar:"+num +":"+ distribucio.getSelectedItem().toString() + ":p"+ dimensio.getSelectedItem().toString() +  ":"+ distancia.getSelectedItem().toString() + ":"+ algorime.getSelectedItem().toString() );
                        }
                    }catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);

                    }



        });
        ((JButton) panellBotons.add(new JButton(ATURAR))).addActionListener(e -> {
            comunicar.comunicar(ATURAR);
        });
        ((JButton) panellBotons.add(new JButton(ESBORRAR))).addActionListener(e -> {
            comunicar.comunicar(ESBORRAR);
        });


        algorime = generateComBox();

        dimensio = generateDim();
        distribucio = generateComBoxDistr();
        distancia = generarDistCombox();

        panellBotons.add(dimensio);

        panellBotons.add(distancia);
        panellBotons.add(algorime);
        panellBotons.add(distribucio);
        this.add(panellBotons, BorderLayout.NORTH);
        this.add(eixos, BorderLayout.CENTER);


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private JComboBox<String> generarDistCombox(){
        JComboBox<String> combox = new JComboBox<>();
        combox.addItem("Parella propera");
        combox.addItem("Parella llunyana");
        combox.setSelectedIndex(0);
        combox.addActionListener(e -> {
            comunicar.comunicar(combox.getSelectedItem().toString());
        });
        return combox;
    }

    private JComboBox<Distribucio> generateComBoxDistr() {
        JComboBox<Distribucio> comboBox = new JComboBox<>();

        comboBox.addItem(Distribucio.Uniforme);
        comboBox.addItem(Distribucio.Gaussiana);
        comboBox.addItem(Distribucio.Exponencial);
        comboBox.setSelectedIndex(0);
        comboBox.addItemListener(e -> {
            comunicar.comunicar("distribucio:"+comboBox.getSelectedItem());

        });
        return comboBox;

    }
    private JComboBox<String> generateDim(){

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem(Dimensio.D2.getEtiqueta());
        comboBox.addItem(Dimensio.D3.getEtiqueta());

        comboBox.setSelectedIndex(0);
        comboBox.addItemListener(e -> {
            comunicar.comunicar("dimensio:"+comboBox.getSelectedItem());

        });

        return comboBox;
    }

    private JComboBox<String> generateComBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Força Bruta");
        comboBox.addItem("Dividir i vèncer");

        comboBox.setSelectedIndex(0); //default a classic
        comboBox.addActionListener(e -> {
            comunicar.comunicar("algorisme:"+comboBox.getSelectedItem());

        });

        return comboBox;

    }



    @Override
    public void comunicar(String s) {
        switch (s) {
            case "dibuixPunts":
                eixos.pintar();
                break;
            case "pintar":
                eixos.repaint();
                break;
        }

    }
}
