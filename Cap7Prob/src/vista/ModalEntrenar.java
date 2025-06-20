package vista;

import controlador.Comunicar;
import controlador.Main;
import model.XarxaSolver;

import javax.swing.*;
import java.awt.*;

public class ModalEntrenar extends JDialog implements Comunicar {
    private JTextArea text;

    public ModalEntrenar(JFrame parent) {
        super(parent, "Entrenar Xarxa", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        setSize(350, 500);
        setLocationRelativeTo(parent);
        JPanel opcions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        opcions.setBorder(BorderFactory.createTitledBorder("Opcions"));
        JButton entrenar = new JButton("Entrenar");
        JTextField epocs = new JTextField(7);
        JButton aturar = new JButton("Aturar");

        entrenar.addActionListener(e -> {
            Integer i = Integer.getInteger(epocs.getText());
            text.setText("");
            if(i == null){
                i = -1;
            }
            Main.getInstance().entrenarXarxa(i);
        });
        opcions.add(entrenar);

        opcions.add(epocs);

        aturar.addActionListener(e -> {Main.getInstance().aturar();});
        opcions.add(aturar);

        this.add(opcions, BorderLayout.NORTH);

        text = new JTextArea();
        JScrollPane scrollText = new JScrollPane(text);

        text.setEditable(false);
        this.add(scrollText, BorderLayout.CENTER);

        //setVisible(true);

    }

    @Override
    public void logText(String text) {
        if(!text.endsWith(System.lineSeparator())){{
            text += System.lineSeparator();}
        }
        this.text.setText(this.text.getText()+text);
    }


    @Override
    public void comunicar(String msg) {

    }
}
