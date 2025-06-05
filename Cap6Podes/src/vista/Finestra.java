package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {
    private Dades dades;

    public Finestra() {
        super();
        this.setTitle("Branch&Bound");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        // dades = Main.getInstance().getDades();
        JPanel panellBotons = new JPanel();

        panellBotons.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnCalcular = new JButton("Calcular");
        panellBotons.add(btnCalcular);
        JLabel stepLabel = new JLabel("Step: ");
        panellBotons.add(stepLabel);
        JCheckBox checkBox = new JCheckBox();
        panellBotons.add(checkBox);
        JButton btnEdit = new JButton("Editar Matriu");
        btnEdit.addActionListener((e -> {
            new EditorMatriu(this);
        }));
        panellBotons.add(btnEdit);

        this.add(panellBotons, BorderLayout.NORTH);

        PanellGraf panell = new PanellGraf(dades);
        this.add(panell, BorderLayout.CENTER);

        this.setVisible(true);

        this.setVisible(true);
    }

    @Override
    public void comunicar(String msg) {

    }
}
