package vista;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Finestra extends JFrame implements Comunicar {

    private final Dades dades;
    private final Comunicar principal;

    private final PanellFitxers descomprimits;
    private final PanellFitxers comprimits;


    private final String[] nomsBtn = {"Carregar", "Comprimir", "Descomprimir", "Guardar"};
    private final JButton[] botons = new JButton[nomsBtn.length];



    public Finestra() {
        super("Compressor Huffman");

        principal = Main.instance;
        dades = Main.instance.getDades();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JToolBar barra = new JToolBar();

        for (int i = 0; i < nomsBtn.length; i++) {
            String nom = nomsBtn[i];
            JButton boto = new JButton(nom);

            boto.addActionListener(e -> gestioBotons(nom));
            botons[i] = boto;

            barra.add(boto);
            barra.addSeparator();
        }

        add(barra, BorderLayout.NORTH);

        // Panells inferiors: dos llistats amb drag & drop i botons add/remove
        descomprimits = new PanellFitxers(this, "Arxius descomprimits", true ,dades);
        comprimits = new PanellFitxers(this,"Arxius comprimits",false ,dades);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descomprimits, comprimits);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);


        setVisible(true);
    }

    private void gestioBotons(String nom) {
        switch (nom) {
            case "Carregar" -> {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(true);
                if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    for (File f : fc.getSelectedFiles()) {
                        Main.instance.comunicar("Carregar:" + f.getAbsolutePath());
                    }
                }
            }
            case "Eliminar" -> {
                for (File f : descomprimits.getSelectedFiles()) {
                    principal.comunicar("Eliminar:" + f.getAbsolutePath());
                }
                for (File f : comprimits.getSelectedFiles()) {
                    principal.comunicar("Eliminar:" + f.getAbsolutePath());
                }
            }
            default -> {
                // Comprimir, Descomprimir, Guardar
                Main.instance.comunicar(nom);
            }
        }

    }


    /**
     * Envia un missatge
     *
     * @param s El missatge
     */
    @Override
    public void comunicar(String s) {
        String[] p = s.split(":", 2);
        if (p[0].equals("actualitzar") && p.length > 1) {

            switch (p[1]) {
                case "descomprimit" -> descomprimits.refresh();
                case "comprimit"   -> comprimits.refresh();
                default -> System.err.println(
                        "Finestra: no sé refrescar -> " + s);
            }
        } else {
            System.err.println("Finestra: missatge desconegut -> " + s);
        }
    }

    public JButton[] getBotons() {
        return botons;
    }
}
