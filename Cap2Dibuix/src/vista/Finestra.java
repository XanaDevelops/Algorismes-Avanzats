package vista;

import principal.Comunicar;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;

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
        botons.add(new JButton("Aturar"));
        botons.add(new JButton("Borrar"));
        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            this.color = JColorChooser.showDialog(this, "Tria Color", color);
        });
        botons.add(new JLabel(""));

        this.add(botons);
        this.pack();
        this.setLocationRelativeTo(null); //centra pantalla
        this.setVisible(true);
    }

    @Override
    public void comunicar(String s){

    }
}
