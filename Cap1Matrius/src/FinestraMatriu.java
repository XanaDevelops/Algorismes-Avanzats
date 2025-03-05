
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FinestraMatriu extends JPanel implements Comunicar {

    private final Main principal;
    private Eixos dibuixMatrius;
    private JTextField nField;
    private final String[] missatgesBotons = {"comencar", "suma", "multiplicar", "aturar"};
    public FinestraMatriu(Main p) {
        principal = p;
        setPreferredSize(new Dimension(800, 625));
        setLayout(new BorderLayout());

        // Crear la barra superior
        JPanel topBar = new JPanel();
        topBar.setLayout(new GridLayout(1, 5));

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);

        JPanel nPanel = new JPanel();
        nPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel nLabel = new JLabel("N:");
        nLabel.setPreferredSize(new Dimension(20, nLabel.getPreferredSize().height));


        nField = new JTextField( );
        nField.setPreferredSize(new Dimension(130, nField.getPreferredSize().height));
        nField.setFont(font);
        nPanel.add(nLabel);
        nPanel.add(nField);

        topBar.add(nPanel);
        String[] textBotons = {"Començar", "Només Sumar", "Només multiplicar", "Aturar"};
        JButton[] botons = new JButton[textBotons.length];
        for (int i = 0; i < botons.length; i++) {
            botons[i] = new JButton(textBotons[i]);
            botons[i].setFont(font);
            int finalI = i;
            botons[i].addActionListener(e -> enviar(missatgesBotons[finalI]));
            topBar.add(botons[i]);

        }
        // Crear el panell principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 800, 600);
        dibuixMatrius = new Eixos(800, 600, principal);
        mainPanel.add(dibuixMatrius);

        // Afegir components
        add(topBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

    }


    private void enviar(String msg) {
        String nText = nField.getText().trim();
        if (nText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Si us plau, introdueix un número.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
             int n = Integer.parseInt(nText);
            principal.comunicar(msg + ":" + n);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public synchronized void comunicar(String s) {
        if (s.startsWith("pintar")) {
            dibuixMatrius.pintar();
        }else{ //limpiar

        }
    }
}