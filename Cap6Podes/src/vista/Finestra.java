package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Finestra extends JFrame implements Comunicar {
    private Dades dades;
    private PanellGraf graf;
    private PanellInformacio panellInfo;

    private Timer timer = new Timer();
    private boolean stop;

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

        JCheckBox checkBox = new JCheckBox();

        JButton btnCalcular = new JButton("Calcular");

        btnCalcular.addActionListener(e -> {
            boolean stepMode = checkBox.isSelected();
            Main.getInstance().calcular(dades.getGraf(), stepMode);
            stop = false;
            if(stepMode) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (!stop) {
                            Main.getInstance().step(0);
                            repaint();
                        } else {
                            timer.cancel();
                        }

                    }
                }, 0L, 500L);
            }
        });
        panellBotons.add(btnCalcular);

        JLabel stepLabel = new JLabel("Step: ");
        panellBotons.add(stepLabel);


        panellBotons.add(checkBox);

        JButton btnEdit = new JButton("Editar Matriu");
        btnEdit.addActionListener((e -> {
            new EditorMatriu(this);
            graf.esborrarSolucio();
            repaint();
        }));
        panellBotons.add(btnEdit);

        JButton btnAturar = new JButton("Aturar");
        btnAturar.addActionListener((e -> {
            Main.getInstance().aturar(0);
            repaint();
        }));
        panellBotons.add(btnAturar);

        this.add(panellBotons, BorderLayout.NORTH);

        graf = new PanellGraf(dades);
        this.add(graf, BorderLayout.CENTER);

        panellInfo = new PanellInformacio();
        panellInfo.setPreferredSize(new Dimension(250, 0));
        this.add(panellInfo, BorderLayout.WEST);

        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        graf.repaint();
        panellInfo.mostrarResultat(dades.getResultat());

    }

    @Override
    public void step(int id){
        graf.mostrarSolucio(dades.getSolucio());
        repaint();
    }

    @Override
    public void comunicar(String msg) {
        System.err.println("Finestra: msg: " + msg);
    }

    @Override
    public void actualitzar(int id){
        stop = true;
        graf.mostrarSolucio(dades.getSolucio());
        repaint();
    }

    @Override
    public void aturar(int id) {
        graf.esborrarSolucio();
        timer.cancel();
        repaint();
    }
}
