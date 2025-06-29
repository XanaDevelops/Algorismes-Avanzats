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
    private Dades.informacio info;

    public PanellInfo() {
        super();
        info = Main.instance.getDades().getInfo();
        setLayout(new BorderLayout());

        //usar reflexió per iterar dinàmicament sobre els atributs de la classe info
        Map<String, Object> data = new LinkedHashMap<>();

        if (info != null) {
            for (Field field : Dades.informacio.class.getDeclaredFields()) {
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
        }

        // Crear model de taula
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Dada", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //cel·ledes no editables
            }
        };
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        String titol = (info.getTempsCompressio() != null) ? "compressió" : "decompressió";
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estadístiques de "+ titol));
        add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void estadistiquesLLestes(){
        this.repaint();
    }

}
