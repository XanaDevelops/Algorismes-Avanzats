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
        this.setPreferredSize(new Dimension(700, 700));
        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout());

        JLabel nLabel = new JLabel("2^n  -->  N:");
        JTextField nField = new JTextField(5);
        botons.add(nLabel);
        botons.add(nField);

        ((JButton)botons.add(new JButton("Arrancar"))).addActionListener(e -> {
            principal.getDades().netejar();
            principal.getDades().resol();
            panellFinestra.pintar();
        });
        botons.add(new JButton("Aturar"));
        botons.add(new JButton("Borrar"));

        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            panellFinestra.pintar();
            panellFinestra.colorON();

            /*this.color = JColorChooser.showDialog(this, "Tria Color", color);
            this.colorLabel.setForeground(color);*/

        });
        botons.add(new JLabel(""));
        this.add(botons, BorderLayout.NORTH);

        // Crear el panell principal
        panellFinestra = new Dibuix(800, 600, principal);

        this.add(panellFinestra, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null); //centra pantalla
        this.setVisible(true);
    }

    @Override
    public void comunicar(String s) {

    }
}
