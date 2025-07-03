package vista.zonaFitxers;

import control.Comunicar;
import control.Main;
import model.Dades;
import vista.Finestra;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanellFitxers extends JPanel {

    private final Dades dades;
    private final Finestra finestra;
    private final boolean esDescomprimit;

    private final DefaultListModel<ElementFitxerLlista> model = new DefaultListModel<>();

    private final JList<ElementFitxerLlista> llistaFitxers = new JList<>(model);

    private Map<Integer, ElementFitxerLlista> elementFitxers = new HashMap<>();

    public PanellFitxers(Comunicar comunicar, String titol, boolean esDescomprimit, Dades dades) {
        super(new BorderLayout());
        this.dades = dades;
        this.esDescomprimit = esDescomprimit;
        this.finestra = (Finestra) comunicar;

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titol,
                TitledBorder.LEADING, TitledBorder.TOP));

        llistaFitxers.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            if (isSelected) {
                value.setBackground(list.getSelectionBackground());
                value.setForeground(list.getSelectionForeground());
            } else {
                value.setBackground(list.getBackground());
                value.setForeground(list.getForeground());
            }
            value.setPreferredSize(new Dimension(list.getWidth(), value.getPreferredSize().height));
            return value;
        });

        llistaFitxers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

    public void ferTicks(){
        int i = llistaFitxers.getSelectedIndex();
        for(ElementFitxerLlista e: elementFitxers.values()){
            e.tick();
        }
        repaint();
        llistaFitxers.setSelectedIndex(i);
    }


    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            Map<Integer, File> src = esDescomprimit ? dades.getAComprimir() : dades.getADescomprimir();

            for (int id : elementFitxers.keySet().toArray(new Integer[0])) {
                if (!src.containsKey(id)) {
                    ElementFitxerLlista e = elementFitxers.remove(id);
                    model.removeElement(e);
                }
            }

            src.forEach((k, v) -> {
                ElementFitxerLlista e = elementFitxers.get(k);
                if (e == null) {
                    afegirElementFitxer(v, k);
                }
            });

            //llistaFitxers.clearSelection();
            actualitzaBotons();
        });
    }

    public void afegirElementFitxer(File f, int id) {
        ElementFitxerLlista e = new ElementFitxerLlista(f, f.toPath(), id);
        elementFitxers.put(id, e);
        model.addElement(e);
    }

    private void carregarFitxer(File f) {
        int id = Main.instance.afegirEnEspera(f);
        if (id >= 0) {
            afegirElementFitxer(f, id);
        }
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
        File f = getSelectedFile();
        Main.instance.eliminarFitxer(f, esDescomprimit);

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

    public File getSelectedFile() {
        ElementFitxerLlista selected = llistaFitxers.getSelectedValue();
        return selected != null ? selected.getFile() : null ;
    }

    public ElementFitxerLlista getElementFitxer(int id) {
        return elementFitxers.get(id);
    }
}