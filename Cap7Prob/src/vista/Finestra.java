package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Finestra extends JFrame implements Comunicar {

    private Dades dades;
    private boolean stop;
    private JLabel imatgeLabel;

    public Finestra() {
        super();
        dades =  Main.getInstance().getDades();
        this.setTitle("Classificador d'imatges naturals");
        this.setSize(new Dimension(700, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        JPanel panellBotons = new JPanel();
        panellBotons.setLayout(new GridLayout(1, 3));
        JButton botoCarregar = new JButton("Carregar");
        JPanel panellImatge = new JPanel();
        imatgeLabel = new JLabel();
        panellImatge.add(imatgeLabel);
        this.add(panellImatge, BorderLayout.CENTER);
        botoCarregar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg", "gif", "svg");
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image originalImage = icon.getImage();
                int imgWidth = originalImage.getWidth(null);
                int imgHeight = originalImage.getHeight(null);

                double widthRatio = (double) panellImatge.getWidth() / imgWidth;
                double heightRatio = (double) panellImatge.getHeight() / imgHeight;
                double scale = Math.min(widthRatio, heightRatio);

                Image scaledImage = originalImage.getScaledInstance((int) (imgWidth * scale), (int)(imgHeight * scale), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                mostrarImatge(scaledIcon);
            }
        });
        JButton classificar = new JButton("Classificar");
        classificar.addActionListener(e -> {
        });
        panellBotons.setLayout(new FlowLayout());
        JButton aturar = new JButton("Aturar");
        aturar.addActionListener(e -> {
            stop = true;
        });


        panellBotons.add(botoCarregar);
        panellBotons.add(classificar);
        panellBotons.add(aturar);

        this.add(panellBotons, BorderLayout.NORTH);


        JPanel classficacionsPanel = new JPanel(new GridLayout(1, 3));
        classficacionsPanel.setPreferredSize(new Dimension(this.getWidth(), 40));

        Categoria bosc = new Categoria("Bosc nòrd");
        Categoria selva = new Categoria("Selva tropical");
        Categoria costa = new Categoria("Costaner");
        classficacionsPanel.add(bosc.panel);
        classficacionsPanel.add(selva.panel);
        classficacionsPanel.add(costa.panel);

        this.add(classficacionsPanel, BorderLayout.SOUTH);

    }

    @Override
    public void comunicar(String msg) {

    }

    private class Categoria {
        JPanel panel;
        JLabel label;

        public Categoria(String nom) {
            label = new JLabel(nom + ": 0%", SwingConstants.CENTER);
            panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.CENTER);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.setBackground(Color.LIGHT_GRAY);
        }

        public void actualitzar(Double percentatge, Double margeError) {
            label.setText(String.format("%s: %.2f%% +-%.2f%%", getNom(), percentatge ,margeError));
        }

        public String getNom() {
            String text = label.getText();
            return text.split(":")[0];
        }

        public void setColorFons(Color color) {
            panel.setBackground(color);
        }
    }

    private void mostrarImatge(ImageIcon icon) {
        this.imatgeLabel.setIcon(icon);
    }

}
