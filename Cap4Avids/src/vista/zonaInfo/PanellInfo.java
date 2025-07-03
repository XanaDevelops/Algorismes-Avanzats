package vista.zonaInfo;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class PanellInfo extends JPanel implements Comunicar {
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable table;

    public PanellInfo() {
        super();
        setLayout(new BorderLayout());

        // Crear modelo de tabla vacío
        model = new DefaultTableModel(new Object[]{"Dada", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // cel·les no editables
            }
        };
        /*for (int i = 0; i < 5; i++) {
            model.addRow(new Object[]{"", ""});
        }*/
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estadístiques"));
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void clearEstadistiques(){
        model.setRowCount(0); // limpiar tabla
        revalidate();
        repaint();

    }

    @Override
    public void estadistiquesLLestes() {
        Dades.Informacio info = Main.instance.getDades().getInfo();
        if (info == null) {
            clearEstadistiques();
            return;
        }

        model.setRowCount(0); // limpiar tabla


        Map<String, Object> data = new LinkedHashMap<>();

        for (Field field : Dades.Informacio.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(info);
                if (value != null && !(value instanceof Number n && n.longValue() == 0)) {
                    String name = field.getName();

                    switch (value) {
                        case Number n when name.toLowerCase().contains("temps") ->
                                value = String.format("%.2f ms", n.longValue() / 1_000_000.0);
                        case Number n when name.toLowerCase().contains("tamany") ->
                                value = n.longValue() + " bytes";
                        case Number n when name.toLowerCase().contains("longitud") ->
                                value = n.doubleValue() + " bits";
                        case Number n when name.toLowerCase().contains("taxa") ->
                                value = String.format("%.2f %%", n.doubleValue());
                        default -> {

                        }
                    }

                    data.put(name, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        String titol = (info.getTempsCompressio() != null) ? "compressió" : "decompressió";
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estadístiques de " + titol));

        revalidate();
        repaint();
    }

}
