package vista;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.Consumer;

public class Finestra extends JFrame implements Comunicar {

    private final Dades dades;
    private final Comunicar principal;

    private final PanellFitxers originals;
    private final PanellFitxers compressats;


    private final String[] nomsBtn = {"Carregar", "Comprimir", "Descomprimir", "Guardar"};
    private final JButton[] botons = new JButton[nomsBtn.length];

    private final ActionListener[] accions = new ActionListener[] {
            e -> carregarFitxers(),
            e -> Main.instance.comunicar("COMPRIMIR"),
            e -> Main.instance.comunicar("DESCOMPRIMIR"),
            e -> Main.instance.comunicar("GUARDAR")
    };


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
            JButton aux = new JButton(nomsBtn[i]);
            aux.addActionListener(accions[i]);
            botons[i] = aux;
            barra.add(aux);
            barra.addSeparator();
        }

        add(barra, BorderLayout.NORTH);

        // Panells inferiors: dos llistats amb drag & drop i botons add/remove
        originals = new PanellFitxers("Arxius descomprimits");
        compressats = new PanellFitxers("Arxius comprimits");
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, originals, compressats);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);


        // Estat inicial dels botons
        //updateBotons();
        setVisible(true);
    }

    /**
     * Carrega fitxers via JFileChooser i distribueix a panell corresponent.
     */
    private void carregarFitxers() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File f : fc.getSelectedFiles()) {
                if (f.getName().endsWith(".huf")) compressats.afegirFitxer(f);
                else originals.afegirFitxer(f);
            }
        }
    }

    /**
     * Actualitza l'estat habilitat/deshabilitat dels botons segons les llistes.
     */
    private void updateBotons() {
        botons[1].setEnabled(!originals.estaBuit());   // Comprimir
        botons[2].setEnabled(!compressats.estaBuit()); // Descomprimir
        botons[3].setEnabled(!compressats.estaBuit()); // Guardar
    }


    /**
     * Envia un missatge
     *
     * @param s El missatge
     */
    @Override
    public void comunicar(String s) {
        String[] args = s.split(":");
        switch (args[0]) {
            default -> System.err.println("WARNING: Finestra reb missatge?: " + s);
        }
    }
}
