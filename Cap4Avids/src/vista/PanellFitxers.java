package vista;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class PanellFitxers extends JPanel {

    private final Dades dades;
    private  final Comunicar finestra;
    private final boolean esDescomprimit;

    private final DefaultListModel<File> model = new DefaultListModel<>();

    private final JList<File> llistaFitxers = new JList<>(model);
    private final JButton btnAfegir = new JButton("Afegir");
    private final JButton btnEliminar = new JButton("Eliminar");


    public PanellFitxers(Comunicar comunicar, String titol, boolean esDescomprimit, Dades dades) {
        super(new BorderLayout());
        this.dades = dades;
        this.esDescomprimit = esDescomprimit;
        this.finestra = comunicar;

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titol,
                TitledBorder.LEADING, TitledBorder.TOP));

        // Afegim la JList amb scroll al centre
        llistaFitxers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(llistaFitxers), BorderLayout.CENTER);



       /* // Drag & drop
        new DropTarget(llistaFitxers, dtde -> {
            try {
                Transferable t = dtde.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(dtde.getDropAction());
                    @SuppressWarnings("unchecked") List<File> fitxers =
                            (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    fitxers.forEach(f -> afegirFitxer(f));
                    panellModel.actualitzaDades();
                    dtde.dropComplete(true);
                }
            } catch (Exception ex) {
                dtde.dropComplete(false);
            }
        });*/

        // Botons
        JPanel panBtns = new JPanel(new FlowLayout());
        panBtns.add(btnAfegir);
        panBtns.add(btnEliminar);
        add(panBtns, BorderLayout.SOUTH);

        btnAfegir.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                for (File f : fc.getSelectedFiles()) {
                    if (!esDescomprimit && f.getName().endsWith(".huf")) {
                        Main.instance.comunicar("Carregar:" + f.getAbsolutePath());
                    } else if (esDescomprimit && (f.getName().endsWith(".bin") || f.getName().endsWith(".txt"))){
                        Main.instance.comunicar("Carregar:" + f.getAbsolutePath());
                    }
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            for (File f : llistaFitxers.getSelectedValuesList()) {
               Main.instance.comunicar("Eliminar:" + f.getAbsolutePath());
            }
        });

    }

    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            model.clear();
            var src = esDescomprimit
                    ? dades.getDescomprimits()
                    : dades.getComprimits();
            for (File f : src) model.addElement(f);
        });
    }

    public List<File> getSelectedFiles() {
        return llistaFitxers.getSelectedValuesList();
    }

    public boolean thereAreNotFiles(){
        return model.isEmpty();
    }
}