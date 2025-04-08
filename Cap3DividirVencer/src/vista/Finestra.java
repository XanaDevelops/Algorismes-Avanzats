package vista;

import controlador.Comunicar;
import javafx.application.Platform;
import model.Dades;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Finestra extends JFrame implements Comunicar {
    public static final String ATURAR = "aturar";
    public static final String ARRANCAR = "arrancar";
    JComboBox<Integer> nPunts;
    Comunicar comunicar;

    Dades dades;
    JComboBox<String> algorime;
    Eixos eixos;

    JPanel panellBotons;
   public Finestra(Comunicar comunicar, Dades dades) {
       super();
       this.comunicar = comunicar;
       this.dades = dades;
       //eixos= new Eixos(750, 900,dades );
       this.setTitle("Distàncies a un nuvol de punts");
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setResizable(false);
       this.setLayout(new BorderLayout());
       this.setPreferredSize(new Dimension(900, 900));

       panellBotons = new JPanel();
       nPunts = generateNPunts();

       panellBotons.add(new JLabel("Nombre de Punts"));

       panellBotons.add(nPunts);

       ((JButton)panellBotons.add(new JButton(ARRANCAR))).addActionListener(e ->{
           comunicar.comunicar(ARRANCAR);
       });
       ((JButton)panellBotons.add(new JButton(ATURAR))).addActionListener(e ->{
           comunicar.comunicar(ATURAR);});

       algorime = generateComBox();
       panellBotons.add(algorime);
      this.add(panellBotons, BorderLayout.NORTH);
      //this.add(eixos, BorderLayout.CENTER);
       Eixos3D eixos3D = new Eixos3D();
       this.add(eixos3D, BorderLayout.CENTER);

      this.pack();
      this.setLocationRelativeTo(null);
      this.setVisible(true);

      Platform.runLater(eixos3D::initFX);

   }

    private JComboBox<String> generateComBox() {
       JComboBox<String> comboBox = new JComboBox<>();
       comboBox.addItem("Clàssic O(N)");
       comboBox.addItem("Optimitzat O(N·log N)");

       comboBox.setSelectedIndex(0); //default a classic
        comboBox.addActionListener(e ->{
            switch (comboBox.getSelectedIndex()) {
            //per ara
                case 0:
                    comunicar.comunicar("classic");
                    break;

                case 1:
                    comunicar.comunicar("optimitzat");
                    break;



            }
        });

       return comboBox;

    }

    private JComboBox<Integer> generateNPunts() {
       JComboBox<Integer> comboBox = new JComboBox<>();
       comboBox.addItem(100);
       comboBox.addItem(500);
       comboBox.addItem(1000);
       comboBox.addItem(2000);
       comboBox.addItem(5000);
        comboBox.addItem(10000);

        comboBox.addActionListener(e -> comunicar.comunicar("generar:" + comboBox.getSelectedItem()));
       return comboBox;
    }

    @Override
    public void comunicar(String s) {

    }
}
