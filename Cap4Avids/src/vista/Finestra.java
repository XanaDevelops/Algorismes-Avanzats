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

    private final PanellFitxers aComprimir;
    private final PanellFitxers aDescomprimir;


    private final String[] nomsBtn = {"Carregar", "Comprimir", "Descomprimir", "Mostrar Arbre"};
    private final JButton[] botons = new JButton[nomsBtn.length];

    private final JPanel panellVisualitzador;
    public Finestra() {
        super("Compressor Huffman");

        principal = Main.instance;
        dades = Main.instance.getDades();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
//        setLocationRelativeTo(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(40,60);
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

        // Panell de visualització de l'arbre (dreta)
        panellVisualitzador = new JPanel(new BorderLayout());
        panellVisualitzador.setBorder(BorderFactory.createTitledBorder("Arbre de Huffman"));
        panellVisualitzador.add(new JLabel("Selecciona un fitxer .huf i fes clic a 'Veure Arbre'"), BorderLayout.CENTER);

        // Panell principal
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitFitxers, panellVisualitzador);
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
                        boolean esDescomprimir = f.getName().endsWith(".huf");
                        principal.afegirEnEspera(Dades.getTaskId(), f, esDescomprimir);
                    }
                    actualitzar();
                }
            }
            case "Eliminar" -> {
                File f = aComprimir.getSelectedFile();
                if (f != null) principal.eliminarFitxer(f, false);

                f = aDescomprimir.getSelectedFile();
                if (f != null) principal.eliminarFitxer(f, true);

            }
            case "Comprimir" -> {
                File sel = aComprimir.getSelectedFile();
                DialegExecucio dlg = new DialegExecucio(this, DialegExecucio.Tipus.COMPRESS, sel);
                DialegExecucio.DEResult msg = dlg.mostra();
                if (msg != null) Main.instance.comprimir(Dades.getTaskId(), sel.getAbsolutePath(), msg.carpetaDesti, msg.wordSize, msg.tipusCua);
            }
            case "Descomprimir" -> {
                File sel = aComprimir.getSelectedFile();
                DialegExecucio dlg = new DialegExecucio(this, DialegExecucio.Tipus.DECOMPRESS, sel);
                DialegExecucio.DEResult msg = dlg.mostra();
                if (msg != null) Main.instance.descomprimir(Dades.getTaskId(),sel.getAbsolutePath(), msg.carpetaDesti);
            }

            case "Mostrar Arbre" -> {
                System.err.println("TODO: " + nom);
            }

            case "Veure Arbre" -> {
                File sel = aComprimir.getSelectedFile();
                this.visualitzar(sel);
            }
            default -> System.err.println("Acció no reconeguda: " + nom);
        }
    }

    private void mostrarArbre(VistaArbreHuffman v) {
        SwingUtilities.invokeLater(() -> {
            panellVisualitzador.removeAll();
            panellVisualitzador.add(new JScrollPane(v), BorderLayout.CENTER);
            panellVisualitzador.revalidate();
            panellVisualitzador.repaint();
        });
    }

    private void mostrarMissatge(String missatge) {
        JOptionPane.showMessageDialog(this, missatge, "Informació", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void visualitzar(File seleccionat) {
        if (seleccionat != null && seleccionat.getName().endsWith(".huf")) {
            try {
                // get arbre
                // var arbre = Main.instance.getDades().getTree();
                // var v = new VistaArbreHuffman(arbre);
                // mostrarArbre(v);
            } catch (Exception e) {
                mostrarMissatge("Error en carregar l'arbre de Huffman.");
                e.printStackTrace();
            }
        } else {
            mostrarMissatge("Selecciona exactament un fitxer .huf per visualitzar l'arbre.");
        }
    }


    @Override
    public void afegirEnEspera(int id, File file, boolean aComprimir) {
        Main.instance.afegirEnEspera(id, file, aComprimir);
    }

    @Override
    public void actualitzar(){
        repaint();
    }

    @Override
    public void arrancar(int id) {
        Comunicar.super.arrancar(id);
    }

    @Override
    public void finalitzar(int id) {
        Comunicar.super.finalitzar(id);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //TODO: check
        aComprimir.refresh(); //o .refresh()
        aDescomprimir.refresh();
    }

    public JButton[] getBotons() {
        return botons;
    }
}
