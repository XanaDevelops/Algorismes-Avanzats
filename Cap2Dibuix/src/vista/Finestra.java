package vista;

import principal.Comunicar;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;
    JLabel colorLabel;
    JTextField nField;
    //temporal
    Color color = Color.BLACK;

    public Finestra(Comunicar principal) {
        super();
        this.principal = principal;

        this.setTitle("Dibuixos recursius");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        this.setLayout(new FlowLayout());
        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout());

        nField = (JTextField)botons.add(new JTextField(5));
        botons.add(generateComboBox());
        botons.add(new JButton("Aturar"));
        botons.add(new JButton("Borrar"));

        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            this.color = JColorChooser.showDialog(this, "Tria Color", color);
            this.colorLabel.setForeground(color);

        });
        this.colorLabel = (JLabel) botons.add(new JLabel("Color"));
        this.colorLabel.setPreferredSize(new Dimension(50, 50));
        this.colorLabel.setForeground(color);
        
        this.add(botons);
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
            principal.comunicar(msg + ":" + n);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void comunicar(String s){

    }
}
