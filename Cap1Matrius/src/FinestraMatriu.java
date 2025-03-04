
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FinestraMatriu extends JPanel implements Comunicar {

    private Main principal;
    private Eixos dibuixMatrius;
    private JTextField nField;

    public FinestraMatriu(Main p) {
        principal = p;
        setPreferredSize(new Dimension(800, 625));
        setLayout(new BorderLayout());

        // Crear la barra superior
        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout());

        JLabel nLabel = new JLabel("N:");
        nField = new JTextField(5);
        JButton comencarBoto = new JButton("Començar");
        JButton sumaBoto = new JButton("Només Sumar");
        JButton multBoto = new JButton("Només Multiplicar");
        JButton aturarBoto = new JButton("Aturar");
        topBar.add(nLabel);
        topBar.add(nField);
        topBar.add(comencarBoto);
        topBar.add(sumaBoto);
        topBar.add(multBoto);
        topBar.add(aturarBoto);

        // Crear el panell principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 800, 600);
        dibuixMatrius = new Eixos(800, 600, principal);
        mainPanel.add(dibuixMatrius);

        // Afegir components
        add(topBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Afegir listeners als botons
        comencarBoto.addActionListener(e -> enviar("comencar"));
        sumaBoto.addActionListener(e -> enviar("suma"));
        multBoto.addActionListener(e -> enviar("multiplicar"));
        aturarBoto.addActionListener(e -> enviar("aturar"));

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
    public void comunicar(String s) {
        if (s.startsWith("pintar")) {
            dibuixMatrius.pintar();
        }else{

        }
    }
}