package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame implements Comunicar {
    private Dades dades;
    private PanellGraf graf;

    public Finestra() {
        super();
        this.setTitle("Branch&Bound");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(900, 700));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        dades = Main.getInstance().getDades();
        JPanel panellBotons = new JPanel();

        panellBotons.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.addActionListener(e -> {
            Main.getInstance().calcular(dades.getGraf());
        });
        panellBotons.add(btnCalcular);

        JLabel stepLabel = new JLabel("Step: ");
        panellBotons.add(stepLabel);

        JCheckBox checkBox = new JCheckBox();
        panellBotons.add(checkBox);

        JButton btnEdit = new JButton("Editar Matriu");
        btnEdit.addActionListener((e -> {
            new EditorMatriu(this);
            repaint();
        }));
        panellBotons.add(btnEdit);

        this.add(panellBotons, BorderLayout.NORTH);

        graf = new PanellGraf(dades);
        this.add(graf, BorderLayout.CENTER);

        PanellInformacio panellInfo = new PanellInformacio();
        panellInfo.setPreferredSize(new Dimension(250, 0));
        this.add(panellInfo, BorderLayout.WEST);

        this.setVisible(true);

        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        graf.repaint();
    }

    @Override
    public void comunicar(String msg) {
        System.err.println("Finestra: msg: " + msg);
    }

    @Override
    public void actualitzar(int id){
        repaint();
    }
}
