package vista;

import principal.Comunicar;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;

    private final Dibuix panellFinestra;
    //temporal
    Color color = Color.BLACK;

    public Finestra(Comunicar principal) {
        super();
        this.principal = principal;

        this.setTitle("Dibuixos recursius");
        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout());

        JLabel nLabel = new JLabel("2^n  -->  N:");
        JTextField nField = new JTextField(5);
        botons.add(nLabel);
        botons.add(nField);

        botons.add(new JButton("Arrancar"));
        botons.add(new JButton("Aturar"));
        botons.add(new JButton("Borrar"));
        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            this.color = JColorChooser.showDialog(this, "Tria Color", color);
        });
        botons.add(new JLabel(""));

        // Crear el panell principal
        panellFinestra = new Dibuix(800, 600, this.principal);

        this.add(botons, BorderLayout.NORTH);
        this.add(panellFinestra, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void comunicar(String s){
        panellFinestra.pintar();
    }
}
