package vista;

import controlador.Comunicar;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {

    public Finestra() {
        super();
        this.setTitle("Branch&Bound");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);

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

        this.add(panellBotons);

        this.setVisible(true);
    }

    @Override
    public void comunicar(String msg) {

    }
}
