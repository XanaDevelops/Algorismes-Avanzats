package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;
import model.Paisatge;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumMap;

public class Finestra extends JFrame implements Comunicar {

    private Dades dades;
    private boolean stop;
    private JLabel imatgeLabel;
    private JPanel classficacionsPanel;
    private Categoria[] categories;
    public Finestra() {
        super();
        dades =  Main.getInstance().getDades();
        categories = new Categoria[Paisatge.values().length];
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

                dades.setImatge(new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB));

                double widthRatio = (double) panellImatge.getWidth() / imgWidth;
                double heightRatio = (double) panellImatge.getHeight() / imgHeight;
                double scale = Math.min(widthRatio, heightRatio);

                Image scaledImage = originalImage.getScaledInstance((int) (imgWidth * scale), (int)(imgHeight * scale), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                mostrarImatge(scaledIcon);
            }
        });
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("HSV");
        comboBox.addItem("Xarxa neuronal");
        comboBox.addActionListener(e -> {
                    int selected = comboBox.getSelectedIndex();
                    if (selected == 0) {
                        this.classificarHSV();
                    }else{
                        classificarXarxa();
                    }
                }
        );
        JButton classificar = new JButton("Classificar");
        classificar.addActionListener(e -> {
            this.classificarHSV();
        });
        panellBotons.setLayout(new FlowLayout());
        JButton aturar = new JButton("Aturar");
        aturar.addActionListener(e -> {
            stop = true;
        });


        panellBotons.add(botoCarregar);
        panellBotons.add(comboBox);
        panellBotons.add(classificar);
        panellBotons.add(aturar);

        this.add(panellBotons, BorderLayout.NORTH);


         classficacionsPanel = new JPanel(new GridLayout(1, 3));
        classficacionsPanel.setPreferredSize(new Dimension(this.getWidth(), 40));

        for (int i = 0; i < categories.length; i++) {
            categories[i] = new Categoria(Paisatge.values()[i].toString());
            classficacionsPanel.add(categories[i].panel);
        }

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

    @Override
    public void classificarHSV(){
        Main.getInstance().classificarHSV();
    }
    private void mostrarImatge(ImageIcon icon) {
        this.imatgeLabel.setIcon(icon);
    }
    @Override
    public void classificarXarxa(){
        Main.getInstance().classificarXarxa();
    }
    @Override
    public void actualitzarFinestra(){
        EnumMap<Paisatge, Double> percentatges = dades.getPercentatges();
        EnumMap<Paisatge, Double> margesDeError = dades.getMargesDeError();

        Paisatge[] values = Paisatge.values();
        for (int i = 0; i < 3; i++) {
            categories[i].actualitzar(percentatges.get(values[i]),margesDeError.get(values[i]) );
        }
    }



}
