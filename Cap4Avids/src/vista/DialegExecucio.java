package vista;

import model.Huffman.Huffman;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DialegExecucio extends JDialog {

    public static class DEResult {
        public String carpetaDesti;
        public Huffman.TipusCua tipusCua;
        public Huffman.WordSize wordSize;
    }

    public enum Tipus { COMPRESS, DECOMPRESS }

    private JTextField textDesti;
    private JComboBox<Huffman.TipusCua> tipusCuaCombo;
    private JComboBox<Huffman.WordSize> midaParaulaCombo;

    private DEResult result = null;

    public DialegExecucio(Frame parent, Tipus tipus, File file) {
        super(parent,
                (tipus == Tipus.COMPRESS ? "Configurar Compressió" : "Configurar Descompressió"),
                true);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        // Fitxer seleccionat
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Fitxer:"), c);

        c.gridx = 1;
        JTextArea fitxerSeleccionat = new JTextArea(file.getAbsolutePath());
        fitxerSeleccionat.setEditable(false);
        fitxerSeleccionat.setColumns(30);
        add(fitxerSeleccionat, c);

        // Només per a compressió: tipus de cua i mida paraula
        if (tipus == Tipus.COMPRESS) {
            c.gridy++;
            c.gridx = 0;
            add(new JLabel("Estructura de cua:"), c);

            c.gridx = 1;
            tipusCuaCombo = new JComboBox<>(Huffman.TipusCua.values());
            add(tipusCuaCombo, c);

            c.gridy++;
            c.gridx = 0;
            add(new JLabel("Mida de paraula:"), c);

            c.gridx = 1;
            midaParaulaCombo = new JComboBox<>(Huffman.WordSize.values());
            add(midaParaulaCombo, c);
        }

        // Carpeta de destí
        c.gridy++;
        c.gridx = 0;
        add(new JLabel("Carpeta de destí:"), c);

        c.gridx = 1;
        JPanel panellDesti = getPanellDesti();
        add(panellDesti, c);

        // Botons
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JPanel panellInferior = new JPanel(new FlowLayout());

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            String outDir = textDesti.getText().trim();
            if (outDir.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Has de seleccionar una carpeta de destí.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            result = new DEResult();
            result.carpetaDesti = outDir;

            if (tipus == Tipus.COMPRESS) {
                result.tipusCua = (Huffman.TipusCua) tipusCuaCombo.getSelectedItem();
                result.wordSize = (Huffman.WordSize) midaParaulaCombo.getSelectedItem();
            }

            dispose();
        });

        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        panellInferior.add(ok);
        panellInferior.add(cancel);
        add(panellInferior, c);

        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel getPanellDesti() {
        JPanel panell = new JPanel(new BorderLayout());
        textDesti = new JTextField(20);
        textDesti.setEditable(false);
        JButton btn = new JButton("…");

        btn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textDesti.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        panell.add(textDesti, BorderLayout.CENTER);
        panell.add(btn, BorderLayout.EAST);
        return panell;
    }

    /**
     * Mostra el diàleg i retorna el resultat, o null si s’ha cancel·lat.
     */
    public DEResult mostra() {
        setVisible(true);
        return result;
    }
}
