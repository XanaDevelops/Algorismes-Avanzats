package vista;

import controlador.Main;
import model.Dades;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorMatriu extends JDialog implements ActionListener {

    private Dades dades;
    private DefaultTableModel model;
    private JTable matriu;
    private JTextField txtWidth, txtHeight;
    private JScrollPane taula;


    public EditorMatriu(JFrame parent) {
        super(parent, "Editar Matriu", ModalityType.APPLICATION_MODAL);
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
            int width = Integer.parseInt(txtWidth.getText());
            int height = Integer.parseInt(txtHeight.getText());
            dades.generarRandom(width, height);
            this.repaint();
        }));
        botons.add(btnGenerar);
        JLabel lWidth = new JLabel("W:");
        JLabel lHeight = new JLabel("H:");
        txtWidth = new JTextField(5);
        txtHeight = new JTextField(5);

        txtWidth.setText(Integer.toString(dades.getGraf() != null ? dades.getGraf()[0].length : Dades.DEFAULT_GRAPH_SIZE));
        txtHeight.setText(Integer.toString(dades.getGraf() != null ? dades.getGraf().length : Dades.DEFAULT_GRAPH_SIZE));

        lWidth.setLabelFor(txtWidth);
        botons.add(lWidth);
        botons.add(txtWidth);
        lHeight.setLabelFor(txtHeight);
        botons.add(lHeight);
        botons.add(txtHeight);

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
