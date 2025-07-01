package vista;

import control.Comunicar;
import control.Main;
import model.Dades;
import vista.zonaArbre.VistaArbreHuffman;
import vista.zonaFitxers.ElementFitxerLlista;
import vista.zonaFitxers.PanellFitxers;
import vista.zonaInfo.PanellInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Finestra extends JFrame implements Comunicar {

    private final Dades dades;
    private final Comunicar principal;

    private final PanellFitxers aComprimir;
    private final PanellFitxers aDescomprimir;
    private PanellInfo panellInfo;

    private final String[] nomsBtn = {"Carregar", "Comprimir", "Descomprimir", "Mostrar Arbre"};
    private final JButton[] botons = new JButton[nomsBtn.length];

    //    private final JPanel panellVisualitzador;
    public Finestra() {
        super("Compressor Huffman");

        principal = Main.instance;
        dades = Main.instance.getDades();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
//        setLocationRelativeTo(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(40, 60);
        setLayout(new BorderLayout());

        // Barra d'eines
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
        aComprimir = new PanellFitxers(this, "Arxius a comprimir", true, dades);
        aDescomprimir = new PanellFitxers(this, "Arxius a descomprimir", false, dades);

        // Panell de fitxers (esquerra)
        JSplitPane splitFitxers = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, aComprimir, aDescomprimir);
        splitFitxers.setResizeWeight(0.5);

        // Panell de visualització de l'arbre (dreta) --> se posa en finestra separada
//        panellVisualitzador = new JPanel(new BorderLayout());
//        panellVisualitzador.setBorder(BorderFactory.createTitledBorder("Arbre de Huffman"));
//        panellVisualitzador.add(new JLabel("Selecciona un fitxer .huf i fes clic a 'Veure Arbre'"), BorderLayout.CENTER);
//
//        // Panell principal
//        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitFitxers, panellVisualitzador);
//        splitPrincipal.setResizeWeight(0.6);
//        add(splitPrincipal, BorderLayout.CENTER);

        panellInfo = new PanellInfo();
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitFitxers, panellInfo);
        splitPrincipal.setResizeWeight(0.6);
        add(splitPrincipal, BorderLayout.CENTER);
        setVisible(true);
    }

    private void gestioBotons(String nom) {
        switch (nom) {
            case "Carregar" -> {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(true);
                if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    for (File f : fc.getSelectedFiles()) {
                        int id = principal.afegirEnEspera(f);
                        if (id >= 0) {
                            // Ara avisa el panell corresponent
                            boolean comprimit = f.getName().toLowerCase().endsWith(".huf");
                            if (comprimit) {
                                aDescomprimir.afegirElementFitxer(f, id);
                            } else {
                                aComprimir.afegirElementFitxer(f, id);
                            }
                        }
                    }
                    actualitzar();
                }
            }
            case "Eliminar" -> {
                File f = aComprimir.getSelectedFile();
                if (f != null) principal.eliminarFitxer(f, true);

                f = aDescomprimir.getSelectedFile();
                if (f != null) principal.eliminarFitxer(f, false);

            }
            case "Comprimir" -> {
                File sel = aComprimir.getSelectedFile();
                int id = dades.getIdFromFile(sel, true);
                DialegExecucio dlg = new DialegExecucio(this, DialegExecucio.Tipus.COMPRESS, sel);
                DialegExecucio.DEResult msg = dlg.mostra();
                if (msg != null)
                    Main.instance.comprimir(id, sel.getAbsolutePath(), msg.carpetaDesti, msg.wordSize, msg.tipusCua);
            }
            case "Descomprimir" -> {
                File sel = aDescomprimir.getSelectedFile();
                int id = dades.getIdFromFile(sel, false);
                DialegExecucio dlg = new DialegExecucio(this, DialegExecucio.Tipus.DECOMPRESS, sel);
                DialegExecucio.DEResult msg = dlg.mostra();
                if (msg != null) Main.instance.descomprimir(id, sel.getAbsolutePath(), msg.carpetaDesti);
            }

            case "Veure Arbre" -> {
                File sel = aComprimir.getSelectedFile() != null ?
                        aComprimir.getSelectedFile() :
                        aDescomprimir.getSelectedFile();
                this.visualitzar(sel);
            }
            default -> System.err.println("Acció no reconeguda: " + nom);
        }
    }

//    private void mostrarArbre(VistaArbreHuffman v) {
//        SwingUtilities.invokeLater(() -> {
//            panellVisualitzador.removeAll();
//            panellVisualitzador.add(new JScrollPane(v), BorderLayout.CENTER);
//            panellVisualitzador.revalidate();
//            panellVisualitzador.repaint();
//        });
//    }

    private void mostrarMissatge(String missatge) {
        JOptionPane.showMessageDialog(this, missatge, "Informació", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void visualitzar(File seleccionat) {
        if (seleccionat != null && seleccionat.getName().endsWith(".huf")) {
            try {
                // TODO: Obtenir l'arrel de l'arbre de Huffman del fitxer seleccionat
                // Huffman.Node arbre = Main.instance.getDades().getArbre();

                // Substituir null per l'arbre real
                // VistaArbreHuffman vista = new VistaArbreHuffman(arbre);

                VistaArbreHuffman vista = new VistaArbreHuffman(null); // De moment null

                JFrame finestraArbre = new JFrame("Visualització de l'arbre de Huffman");
                finestraArbre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                finestraArbre.getContentPane().add(new JScrollPane(vista));
                finestraArbre.pack();
                finestraArbre.setLocationRelativeTo(this);
                finestraArbre.setVisible(true);

            } catch (Exception e) {
                mostrarMissatge("Error en carregar l'arbre de Huffman.");
                e.printStackTrace();
            }
        } else {
            mostrarMissatge("Selecciona exactament un fitxer .huf per visualitzar l'arbre.");
        }
    }



    @Override
    public int afegirEnEspera(File file) {
        return Main.instance.afegirEnEspera(file);
    }

    @Override
    public void actualitzar() {
        repaint();
        //TODO: check
        aComprimir.refresh(); //o .refresh()
        aDescomprimir.refresh();
    }

    @Override
    public void arrancar(int id) {
        JButton[] botons = getBotons();
        for (JButton boto : botons) {
            boto.setEnabled(false);
        }
    }

    @Override
    public void finalitzar(int id) {
        JButton[] botons = getBotons();
        for (JButton boto : botons) {
            boto.setEnabled(true);
        }
        actualitzar();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        aComprimir.refresh(); //o .refresh()
        aDescomprimir.refresh();
    }

    @Override
    public void estadistiquesLLestes() {
        this.panellInfo.estadistiquesLLestes();
    }

    public JButton[] getBotons() {
        return botons;
    }


}
