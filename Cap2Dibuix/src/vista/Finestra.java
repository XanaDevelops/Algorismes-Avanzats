package vista;

import model.Dades;
import model.Tipus;
import principal.Comunicar;
import vista.visualitzadors.DibuixCarpet;
import vista.visualitzadors.DibuixImage;
import vista.visualitzadors.DibuixSierpinski;
import vista.visualitzadors.DibuixTromino;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Finestra extends JFrame implements Comunicar {

    Comunicar principal;
    Dades dades;
    JLabel colorLabel;
    JTextField nField;
    JComboBox<String> dibuixosCBox;
    JButton pintarButton;

    //L'element que interpreta la matriu de dades i dibuixa la figura
    Comunicar dibuix;

    public Finestra(Comunicar principal, Dades dades) {
        super();
        this.principal = principal;
        this.dades = dades;

        this.setTitle("Dibuixos recursius");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        this.setLayout(new BorderLayout());

        dibuix = new DibuixTromino(principal);

        //volem un quadrat al dibuix, no necesariament la finestra
        ((Component)dibuix).setPreferredSize(new Dimension(900, 900));
        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout());

        pintarButton = new JButton("Pintar");

        nField = (JTextField)botons.add(new JTextField(5));
        nField.addActionListener(e -> {
            pintarButton.doClick();
        });


        dibuixosCBox = (JComboBox<String>) botons.add(generateComboBox());

        ((JButton)botons.add(pintarButton)).addActionListener(e -> {
            enviarAmbN("executar:"+dibuixosCBox.getSelectedItem());
        });
        ((JButton)botons.add(new JButton("Aturar"))).addActionListener(e -> {
            principal.comunicar("aturar");
        });
        ((JButton)botons.add(new JButton("Borrar"))).addActionListener(e -> {
            principal.comunicar("borrar");
        });

        ((JButton)botons.add(new JButton("Color"))).addActionListener(e -> {
            /*this.color = JColorChooser.showDialog(this, "Tria Color", color);
            this.colorLabel.setForeground(color);*/
            dibuix.comunicar("color");

        });

        for (int i = 0; i < 4; i++) {
            botons.add(new BotoColor(dades.getColor(i), i));
        }

        this.add(botons, BorderLayout.NORTH);
        this.add((Component) dibuix, BorderLayout.CENTER); //per exemple


        this.pack();
        this.setLocationRelativeTo(null); //centra pantalla
        this.setVisible(true);

        dibuix.comunicar("arrancar");
    }

    private JComboBox<String> generateComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("tromino");
        comboBox.addItem("triangles");
        comboBox.addItem("quadrat");
        comboBox.addItem("arbre");

        comboBox.setSelectedIndex(0);

        comboBox.addActionListener(e -> {
            System.out.println("CHANGED: "+ comboBox.getSelectedItem());

            switch ((String) Objects.requireNonNull(comboBox.getSelectedItem())){
                case "tromino":
                    if (!(dibuix instanceof DibuixTromino)){
                        principal.comunicar("borrar");
                        replace(new DibuixTromino(principal));
                    }
                    break;
                case "triangles":
                    if (!(dibuix instanceof DibuixSierpinski)){
                        principal.comunicar("borrar");
                        replace(new DibuixSierpinski(principal));
                    }
                    break;
                case "quadrat":
                    if (!(dibuix instanceof DibuixCarpet)){
                        principal.comunicar("borrar");
                        replace(new DibuixCarpet(principal));
                    }
                    break;
                case "arbre":
                    //és possible que un DibuixImage s'usi per generacions no matricials
                    if (!(dibuix instanceof DibuixImage) && dades.getTipus() != Tipus.TREE){
                        principal.comunicar("borrar");
                        replace(new DibuixImage(principal));
                    }
                    break;
                default:
                    System.err.println("dibuix no valid");
            }
        });
        return comboBox;
    }

    private void replace(Comunicar nouDibuix) {
        dibuix.comunicar("aturar");
        this.remove((Component) dibuix);
        dibuix = nouDibuix;

        this.add((Component) nouDibuix, BorderLayout.CENTER);
        this.revalidate();

        dibuix.comunicar("arrancar");
    }


    /**
     * Envia un missatge d'execució a principal (controlador.Main) amb n
     * @param msg missatge a enviar
     */
    private void enviarAmbN(String msg) {
        String nText = nField.getText().trim();
        if (nText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Si us plau, introdueix un número.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int n = Integer.parseInt(nText);
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "Introdueix un numero major a 0", "Error", JOptionPane.WARNING_MESSAGE);
            }
            else{
                if (n > 7 && this.dibuixosCBox.getSelectedIndex() != 3){
                    int r =JOptionPane.showConfirmDialog(this, n+" és un numero més gran que 7. " +
                                                "És possible que els detalls del dibuix siguin massa petits", "Avís",
                                                JOptionPane.OK_CANCEL_OPTION);
                    if (r == JOptionPane.CANCEL_OPTION){
                        return;
                    }
                }
                principal.comunicar(msg + ":" + n);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void comunicar(String s) {
        if (s.startsWith("temps")) {
            String msg = s.split(":", 2)[1];
            if (s.startsWith("tempsEsperat")) {
                JOptionPane.showMessageDialog(null, "Tardaré "+ msg, "Temps Esperat", JOptionPane.INFORMATION_MESSAGE);

            }else{
                JOptionPane.showMessageDialog(null, "He tardat "+ msg, "Temps Real", JOptionPane.INFORMATION_MESSAGE);

            }
        } else {
            dibuix.comunicar(s);
        }
    }

    private class BotoColor extends JButton{
        public Color colorHold;
        public int i;

        public BotoColor(Color color, int i) {
            super();
            this.colorHold = color;
            this.i = i;

            setPreferredSize(new Dimension(32, 32));
            addActionListener(e -> {
                JColorChooser chooser = new JColorChooser(colorHold);
                JOptionPane.showConfirmDialog(this, chooser, "Color", JOptionPane.OK_CANCEL_OPTION);
                colorHold = chooser.getColor();
                dades.setColor(i, colorHold);
                if(dibuixosCBox.getSelectedIndex() == 3){
                    pintarButton.doClick();
                }else {
                    dibuix.comunicar("pintar");
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(colorHold);
            g.fillRect(0, 0, getWidth(), getHeight());
        }


    }

}
