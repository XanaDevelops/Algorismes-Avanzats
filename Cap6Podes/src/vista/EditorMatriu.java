package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class EditorMatriu extends JDialog implements ActionListener {

    private Dades dades;
    private DefaultTableModel model;
    private JTable matriu;
    private JTextField txtTam;
    private JScrollPane taula;
    private Comunicar finestra;

    public EditorMatriu(JFrame parent) {
        super(parent, "Editar Matriu", ModalityType.APPLICATION_MODAL);
        this.finestra = (Comunicar) parent;
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.dades = Main.getInstance().getDades();

        this.setSize(700, 500);
        this.setAlwaysOnTop(true);
        this.setLayout(new BorderLayout());
        JPanel botons = new JPanel();
        botons.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener((e -> {guardarDades();}));
        botons.add(btnGuardar);
        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener((e -> {
            int n = Integer.parseInt(txtTam.getText());
            dades.generarRandom(n);
            this.repaint();
        }));
        botons.add(btnGenerar);
        JLabel lTam = new JLabel("N:");
        txtTam = new JTextField(5);

        txtTam.setText(Integer.toString(dades.getGraf() != null ? dades.getGraf()[0].length : Dades.DEFAULT_GRAPH_SIZE));

        lTam.setLabelFor(txtTam);
        botons.add(lTam);
        botons.add(txtTam);

        JButton btnImportar = new JButton("Importar");
        btnImportar.addActionListener((e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setFileFilter((new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".csv") || f.getName().toLowerCase().endsWith(".txt");
                }

                /**
                 * The description of this filter. For example: "JPG and GIF Images"
                 *
                 * @return the description of this filter
                 * @see FileView#getName
                 */
                @Override
                public String getDescription() {
                    return "CSV o TXT";
                }
            }));
            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    dades.importarDades(file.getAbsolutePath());
                } catch (RuntimeException | IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al importar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            repaint();
        }));
        botons.add(btnImportar);

        JButton btnExportar = new JButton("Exportar");
        btnExportar.addActionListener((e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setFileFilter((new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".csv") || f.getName().toLowerCase().endsWith(".txt");
                }

                /**
                 * The description of this filter. For example: "JPG and GIF Images"
                 *
                 * @return the description of this filter
                 * @see FileView#getName
                 */
                @Override
                public String getDescription() {
                    return "CSV o TXT";
                }
            }));
            int res = fc.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    dades.exportarDades(file.getAbsolutePath());
                } catch (RuntimeException | IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al exportar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            repaint();
        }));
        botons.add(btnExportar);
        this.add(botons, BorderLayout.NORTH);

        model = new DefaultTableModel(Dades.DEFAULT_GRAPH_SIZE,Dades.DEFAULT_GRAPH_SIZE);
        matriu = new JTable(model);
        matriu.setDragEnabled(false);
        //matriu.setTableHeader(null);
        matriu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        taula = new JScrollPane(matriu);

        this.add(taula, BorderLayout.CENTER);
        this.setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        dibuixarMatriu();
    }

    private void dibuixarMatriu() {
        int[][] graf = dades.getGraf();
        if(graf == null){
            dades.generarRandom();
            JOptionPane.showMessageDialog(this, "No hi dades del graf, generat aleatòriament", "Avís", JOptionPane.WARNING_MESSAGE);
            graf = dades.getGraf();
        }
        System.err.println(graf.length + ", " + graf[0].length);
        model.setColumnCount(graf[0].length);
        model.setRowCount(graf.length);
        model.fireTableDataChanged();
        for (int i = 0; i < graf.length; i++) {
            //matriu.getColumnModel().getColumn(i).setPreferredWidth(50);
            for (int j = 0; j < graf[0].length; j++) {
                matriu.setValueAt(graf[i][j], i, j);
            }
        }
        this.revalidate();
    }


    private void guardarDades(){
        for (int i = 0; i < matriu.getRowCount(); i++) {
            for (int j = 0; j < matriu.getColumnCount(); j++) {
                Object value = matriu.getValueAt(i,j);
                int val = Integer.MAX_VALUE;
                if (value instanceof Integer) {
                    val = (Integer) value;
                } else if (value instanceof String) {
                    val = Integer.parseInt((String) value);
                }else{
                    System.err.println("ERROR tipus");
                }
                dades.setVal(j, i, val);
            }
        }
        this.dispose();
    }







    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
