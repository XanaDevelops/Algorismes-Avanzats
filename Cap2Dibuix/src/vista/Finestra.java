package vista;

import principal.Comunicar;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;
    JLabel colorLabel;
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
        ((JComboBox<String>)botons.add(new JComboBox<String>())).addItem("Quadrats");
        botons.add(new JButton("Aturar"));
        botons.add(new JButton("Borrar"));
        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            this.color = JColorChooser.showDialog(this, "Tria Color", color);
            this.colorLabel.setForeground(color);
            this.repaint();
        });
        this.colorLabel = (JLabel) botons.add(new JLabel("Color"));
        this.colorLabel.setPreferredSize(new Dimension(50, 50));
        this.colorLabel.setForeground(color);
        
        this.add(botons);
        this.pack();
        this.setLocationRelativeTo(null); //centra pantalla
        this.setVisible(true);
    }

    @Override
    public void comunicar(String s){

    }
}
