package vista;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

public class PanellFitxers extends JPanel {

    private final Dades dades;
    private final Finestra finestra;
    private final boolean esDescomprimit;

    private final DefaultListModel<File> model = new DefaultListModel<>();

    private final JList<File> llistaFitxers = new JList<>(model);


    public PanellFitxers(Comunicar comunicar, String titol, boolean esDescomprimit, Dades dades) {
        super(new BorderLayout());
        this.dades = dades;
        this.esDescomprimit = esDescomprimit;
        this.finestra = (Finestra) comunicar;

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titol,
                TitledBorder.LEADING, TitledBorder.TOP));


        llistaFitxers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        llistaFitxers.setDropMode(DropMode.ON);
        llistaFitxers.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) return false;
                try {
                    Transferable t = support.getTransferable();
                    List<File> fitxers = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    fitxers.forEach(f -> carregarFitxer(f));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        });

        llistaFitxers.addListSelectionListener(e -> actualitzaBotons());
        add(new JScrollPane(llistaFitxers), BorderLayout.CENTER);

        // Botons
        JPanel panBtns = new JPanel(new FlowLayout());

        JButton btnAfegir = new JButton("Afegir");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnNet = new JButton("Netejar");

        panBtns.add(btnAfegir);
        panBtns.add(btnEliminar);
        panBtns.add(btnNet);
        add(panBtns, BorderLayout.SOUTH);

        btnAfegir.addActionListener(e -> obrirSelector());
        btnEliminar.addActionListener(e -> eliminarSeleccionats());
        btnNet.addActionListener(e -> refresh());

        actualitzaBotons();
    }

    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            model.clear();
            var src = esDescomprimit ? dades.getDescomprimits() : dades.getComprimits();
            src.forEach(model::addElement);
            llistaFitxers.clearSelection();
            actualitzaBotons();
        });
    }

    private void carregarFitxer(File f) {
        Main.instance.comunicar("Carregar:" + f.getAbsolutePath());
    }

    private void obrirSelector() {
        JFileChooser fc = new JFileChooser();
        if (!esDescomprimit) fc.setFileFilter(new javax.swing.filechooser
                .FileNameExtensionFilter("Huffman (*.huf)", "huf"));

        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File f : fc.getSelectedFiles()) carregarFitxer(f);
            refresh();
        }
    }

    private void eliminarSeleccionats() {
        for (File f : getSelectedFiles()) {
            Main.instance.comunicar("Eliminar:" + f.getAbsolutePath());
        }
        refresh();
    }

    private void actualitzaBotons() {
        boolean teSeleccionat = !llistaFitxers.getSelectedValuesList().isEmpty();
        if (esDescomprimit) {
            finestra.getBotons()[1].setEnabled(teSeleccionat);
        } else {
            finestra.getBotons()[2].setEnabled(teSeleccionat);
        }
    }

    public List<File> getSelectedFiles() {
        return llistaFitxers.getSelectedValuesList();
    }
}