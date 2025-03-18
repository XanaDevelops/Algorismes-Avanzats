package vista;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    Main principal;
    JLabel colorLabel;
    JTextField nField;
    Dibuix panellFinestra;

    //temporal
    Color color = Color.BLACK;

    public Finestra(Main principal) {
        super();
        this.principal = principal;

        this.setTitle("Dibuixos recursius");
        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        // Crear el panell principal
        panellFinestra = new Dibuix(800, 600, principal);

        this.add(panellFinestra, BorderLayout.CENTER);

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
    public void comunicar(String s) {

    }
}
