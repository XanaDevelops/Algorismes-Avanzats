package vista;

import model.Dades;
import principal.Comunicar;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;
    Dades dades;
    JLabel colorLabel;
    JTextField nField;

    //L'element que interpreta la matriu de dades i dibuixa la figura
    Comunicar dibuix;

    public Finestra(Comunicar principal, Dades dades) {
        super();
        this.principal = principal;
        this.dades = dades;
        dibuix = new DibuixTromino(700, 700, principal);

        this.setTitle("Dibuixos recursius");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(700, 700));

        this.setLayout(new BorderLayout());
        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout());

        nField = (JTextField)botons.add(new JTextField(5));
        botons.add(generateComboBox());
        botons.add(new JButton("Aturar"));
        botons.add(new JButton("Borrar"));

        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            /*this.color = JColorChooser.showDialog(this, "Tria Color", color);
            this.colorLabel.setForeground(color);*/
            ((DibuixTromino) (dibuix)).colorON();

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
        comboBox.addItem("[REDACTED]");

        //temporal
        comboBox.addActionListener(e -> {
            System.out.println("CHANGED: "+ comboBox.getSelectedItem());
        });
        return comboBox;
    }

    private void enviar(String msg) {
        String nText = nField.getText().trim();
        if (nText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Si us plau, introdueix un número.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int n = Integer.parseInt(nText);
            //principal.comunicar(msg + ":" + n);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void comunicar(String s) {

    }
}
