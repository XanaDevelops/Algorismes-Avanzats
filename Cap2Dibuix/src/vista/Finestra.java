package vista;

import model.Dades;
import principal.Comunicar;
import vista.visualitzadors.DibuixSierpinski;
import vista.visualitzadors.DibuixTromino;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;
    Dades dades;
    JLabel colorLabel;
    JTextField nField;
    JComboBox<String> dibuixosCBox;

    //L'element que interpreta la matriu de dades i dibuixa la figura
    Comunicar dibuix;

    public Finestra(Comunicar principal, Dades dades) {
        super();
        this.principal = principal;
        this.dades = dades;

        this.setTitle("Dibuixos recursius");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(700, 700));
        setResizable(false);

        this.setLayout(new BorderLayout());

        dibuix = new DibuixTromino(300, 300, principal);

        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout());

        JButton pintarButton = new JButton("Pintar");

        nField = (JTextField)botons.add(new JTextField(5));
        nField.addActionListener(e -> {
            pintarButton.doClick();
        });

        dibuixosCBox = (JComboBox<String>) botons.add(generateComboBox());

        ((JButton)botons.add(pintarButton)).addActionListener(e -> {
            enviarAmbN("executar:"+dibuixosCBox.getSelectedItem());
        });
        ((JButton)botons.add(new JButton("Aturar"))).addActionListener(e -> {
            principal.comunicar("aturar");
        });
        ((JButton)botons.add(new JButton("Borrar"))).addActionListener(e -> {
            principal.comunicar("borrar");
        });

        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            /*this.color = JColorChooser.showDialog(this, "Tria Color", color);
            this.colorLabel.setForeground(color);*/
            dibuix.comunicar("color");

        });

        this.add(botons, BorderLayout.NORTH);
        this.add((JComponent)dibuix, BorderLayout.CENTER); //per exemple


        this.pack();
        this.setLocationRelativeTo(null); //centra pantalla
        this.setVisible(true);
    }

    private JComboBox<String> generateComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("tromino");
        comboBox.addItem("triangles");
        comboBox.addItem("quadrat");

        comboBox.setSelectedIndex(0);

        comboBox.addActionListener(e -> {
            System.out.println("CHANGED: "+ comboBox.getSelectedItem());

            switch ((String) Objects.requireNonNull(comboBox.getSelectedItem())){
                case "tromino":
                    if (!(dibuix instanceof DibuixTromino)){
                        principal.comunicar("borrar");
                        replace(new DibuixTromino(300, 300, principal));
                    }
                    break;
                case "triangles":
                    if (!(dibuix instanceof DibuixSierpinski)){
                        principal.comunicar("borrar");
                        replace(new DibuixSierpinski(principal));
                    }
                    break;
                case "quadrat":
                    //TODO: CAMBIAR POR DIBUIX PROPI!!!!
                    if (!(dibuix instanceof DibuixTromino)){
                        principal.comunicar("borrar");
                        replace(new DibuixTromino(300, 300, principal));
                    }
                    break;
                default:
                    System.err.println("dibuix no valid");
            }
        });
        return comboBox;
    }

    private void replace(Comunicar nouDibuix) {
        this.remove((Component) dibuix);
        dibuix.comunicar("borrar");
        dibuix = nouDibuix;

        this.add((Component) nouDibuix, BorderLayout.CENTER);
        this.revalidate();
    }


    /**
     * Envia un missatge d'execució a principal (Main) amb n
     * @param msg missatge a enviar
     */
    private void enviarAmbN(String msg) {
        String nText = nField.getText().trim();
        if (nText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Si us plau, introdueix un número.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int n = Integer.parseInt(nText);
            principal.comunicar(msg + ":" + n);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void comunicar(String s) {
        dibuix.comunicar(s);
    }
}
