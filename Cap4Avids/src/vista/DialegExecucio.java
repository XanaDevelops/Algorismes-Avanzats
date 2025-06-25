package vista;

import model.Huffman.Huffman;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DialegExecucio extends JDialog {
    public static class DEResult{
        public String carpetaDesti;
        public Huffman.TipusCua tipusCua;
        public Huffman.WordSize wordSize;
    }

    public enum Tipus { COMPRESS, DECOMPRESS }

    JTextField textDesti;
    JComboBox<Huffman.TipusCua> tipusCoa;
    JComboBox<Huffman.WordSize> midaP;

    private DEResult result = null;

    public DialegExecucio(Frame parent, Tipus tipus, File file) {
        super(parent,
                (tipus == Tipus.COMPRESS ? "Configurar Compressió" : "Configurar Descompressió"),
                true);

        //ESTIL
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        // fitxerSeleccionat
        c.gridx = 0; c.gridy = 0;
        add(new JLabel("Fitxer:"), c);

        c.gridx = 1;
        String nom = file.toString().replace("[", "").replace("]", "");

        JTextArea fitxerSeleccionat = new JTextArea(nom);
        fitxerSeleccionat.setEditable(false);
        fitxerSeleccionat.setColumns(30);
        //add(new JScrollPane(fitxerSeleccionat), c);
        add(fitxerSeleccionat, c);


        // elecio coa
        if(tipus == Tipus.COMPRESS) {
            c.gridy++;
            c.gridx = 0;
            add(new JLabel("Estructura de cua:"), c);

            c.gridx = 1;
            tipusCoa = new JComboBox<>(Huffman.TipusCua.values());
            add(tipusCoa, c);


            // midaParaula
            c.gridy++;
            c.gridx = 0;
            add(new JLabel("Mida de paraula:"), c);

            c.gridx = 1;
            midaP = new JComboBox<>(Huffman.WordSize.values());
            add(midaP, c);
        }
        // Destí
        c.gridy++; c.gridx = 0;
        add(new JLabel("Carpeta de destí:"), c);

        c.gridx = 1;
        JPanel panellDesti = getPanellDesti();
        add(panellDesti, c);

        // Panell Botons
        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JPanel panellInferiror = new JPanel();

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        panellInferiror.add(ok);
        panellInferiror.add(cancel);

        add(panellInferiror, c);

        ok.addActionListener(e -> {
            String outDir   = textDesti.getText().trim();

            if (outDir.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Has de seleccionar una carpeta de destí.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            result = new DEResult();

            result.carpetaDesti = outDir;
            result.tipusCua = (Huffman.TipusCua) tipusCoa.getSelectedItem();
            result.wordSize = (Huffman.WordSize) midaP.getSelectedItem();

            dispose();
        });

        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel getPanellDesti() {
        JPanel panellDesti = new JPanel(new BorderLayout());
        textDesti = new JTextField(20);
        textDesti.setEditable(false);
        JButton btnDir = new JButton("…");

        btnDir.addActionListener(e -> {
            JFileChooser fd = new JFileChooser();
            fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fd.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textDesti.setText(fd.getSelectedFile().getAbsolutePath());
            }
        });

        panellDesti.add(textDesti, BorderLayout.CENTER);
        panellDesti.add(btnDir, BorderLayout.EAST);
        return panellDesti;
    }

    /**
     * Mostra el dialeg i retorna el missatge per enviar a Main.instance.comunicar(),
     * o null si s'ha cancel·lat.
     */
    public DEResult mostra() {
        setVisible(true);
        return result;
    }
}