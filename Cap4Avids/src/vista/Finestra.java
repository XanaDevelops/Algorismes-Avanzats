package vista;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.MissingFormatArgumentException;

public class Finestra extends JFrame implements Comunicar {

    private final Dades dades;
    private final Comunicar principal;

    private final PanellFitxers descomprimits;
    private final PanellFitxers comprimits;


    private final String[] nomsBtn = {"Carregar", "Comprimir", "Descomprimir", "Guardar", "Mostrar Arbre"};
    private final JButton[] botons = new JButton[nomsBtn.length];

    private final JPanel panellVisualitzador;
    public Finestra() {
        super("Compressor Huffman");

        principal = Main.instance;
        dades = Main.instance.getDades();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
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
        descomprimits = new PanellFitxers(this, "Arxius a comprimir", true, dades);
        comprimits = new PanellFitxers(this, "Arxius a descomprimir", false, dades);

        // Panell de fitxers (esquerra)
        JSplitPane splitFitxers = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descomprimits, comprimits);
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
                        Main.instance.comunicar("Carregar;" + f.getAbsolutePath());
                    }
                }
            }
            case "Eliminar" -> {
                File f = descomprimits.getSelectedFile();
                principal.comunicar("Eliminar;" + f.getAbsolutePath());

                f = comprimits.getSelectedFile();
                principal.comunicar("Eliminar;" + f.getAbsolutePath());

            }
            case "Comprimir" -> {
                File sel = descomprimits.getSelectedFile();
                DialegExecucio dlg = new DialegExecucio(this, DialegExecucio.Tipus.COMPRESS, sel);
                String msg = dlg.mostra();
                if (msg != null) Main.instance.comunicar(msg);
            }
            case "Descomprimir" -> {
                File sel = descomprimits.getSelectedFile();
                DialegExecucio dlg = new DialegExecucio(this, DialegExecucio.Tipus.DECOMPRESS, sel);
                String msg = dlg.mostra();
                if (msg != null) Main.instance.comunicar(msg);
            }

            case "Guardar" -> Main.instance.comunicar("Guardar");

            case "Mostrar Arbre" -> {
                // FinestraArbre dlg = new FinestraArbre();
                //dlg.mostra();
                Main.instance.comunicar(nom);
            }

            case "Veure Arbre" -> {
                File seleccionat = comprimits.getSelectedFile();
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
            default -> {
                // Comprimir, Descomprimir, Guardar
                Main.instance.comunicar(nom);
            }
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

    /**
     * Envia un missatge
     *
     * @param s El missatge
     */
    @Override
    public void comunicar(String s) {
        String[] p = s.split(";");
        switch (p[0]){
            case "actualitzar":
                switch (p[1]) {
                    case "descomprimit" -> descomprimits.refresh();
                    case "comprimit" -> comprimits.refresh();
                    default -> System.err.println(
                            "Finestra: no sé refrescar -> " + s);
                }
                break;
            case "carregar":
                DialegExecucio dlg = new DialegExecucio(this, p[2].equalsIgnoreCase("comprimir") ? DialegExecucio.Tipus.DECOMPRESS : DialegExecucio.Tipus.COMPRESS, new File(p[1]));
                String msg = dlg.mostra();
                if (msg != null){
                    int id = Dades.taskId++;

                    Main.instance.comunicar(s+";"+id); //carregar
                    Main.instance.comunicar(msg+";"+id); //comprimir/descomprimir
                }
                break;
            default:
                System.err.println("Finestra: missatge desconegut -> " + s);
        }
    }

    public JButton[] getBotons() {
        return botons;
    }
}
