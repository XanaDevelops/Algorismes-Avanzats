
import javax.swing.*;
import java.awt.*;

/**
 * Classe que manipula la finestra principal del programa.
 */
public class FinestraMatriu extends JPanel implements Comunicar {

    private final Main principal;
    private final Eixos dibuixMatrius;
    private final JTextField nField;
    private final JPanel panelLlagenda;

    private final JProgressBar sumarBar;
    private final JProgressBar multiplicarBar;
    private final String[] missatgesBotons = {"comencar", "suma", "multiplicar", "aturar", "net"};

    public FinestraMatriu(Main p) {
        principal = p;
        setPreferredSize(new Dimension(800, 625));
        setLayout(new BorderLayout());

        // Crear la barra superior
        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout());

        JLabel nLabel = new JLabel("N:");
        nField = new JTextField(5);
        topBar.add(nLabel);
        topBar.add(nField);

        JButton[] botons = new JButton[missatgesBotons.length];
        for (int i = 0; i < botons.length; i++) {
            String[] textBotons = {"Començar", "Només Sumar", "Només Multiplicar", "Aturar", "Netejar"};
            botons[i] = new JButton(textBotons[i]);
            int finalI = i;
            // Afegir listeners als botons
            botons[i].addActionListener(e -> enviar(missatgesBotons[finalI]));
            topBar.add(botons[i]);
        }
        //Llegenda
        panelLlagenda = new JPanel();

        configuraLlegenda();

        this.add(panelLlagenda);

        //marcadors Execució

        sumarBar = new JProgressBar();
        multiplicarBar = new JProgressBar();
        setAndAddBar(sumarBar, "S", topBar);
        setAndAddBar(multiplicarBar, "M", topBar);

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


    private void configuraLlegenda() {
        panelLlagenda.setLayout(new FlowLayout(FlowLayout.LEFT));
        afegirEtiqueta("Suma", Color.GREEN);
        afegirEtiqueta("Multiplicar", Color.RED);
        afegirEtiqueta("     x: N    y: log10(Temps)", Color.BLACK);
        panelLlagenda.setSize(panelLlagenda.getPreferredSize());
        panelLlagenda.setLocation(100, 50);
    }

    /**
     * Crea i afegeix una etiqueta al panelLlengenda
     * @param txt text de l'etiqueta
     * @param color color del text
     */
    private void afegirEtiqueta(String txt, Color color) {
        JLabel etiqueta = new JLabel(txt);
        etiqueta.setForeground(color);
        panelLlagenda.add(etiqueta);
    }

    /**
     * Inicialitza i afegeix la barra b i el text corresponent al JPanel panel.
     *
     * @param panel panell al qual s'afegiran b i text
     */
    private void setAndAddBar(JProgressBar b, String tipus, JPanel panel) {
        JLabel text = new JLabel(tipus + ":");
        b.setIndeterminate(false); //desactiva la barra
        b.setPreferredSize(new Dimension(50, sumarBar.getPreferredSize().height));
        panel.add(text);
        panel.add(b);

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
        String[] split = s.split(":");
        switch (split[0]) {
            case "pintar":
                panelLlagenda.repaint();
                dibuixMatrius.pintar();
                break;
            case "activar":
                ("sumar".equals(split[1]) ? sumarBar : multiplicarBar).setIndeterminate(true);
                break;
            case "desactivar":
                ("sumar".equals(split[1]) ? sumarBar : multiplicarBar).setIndeterminate(false);
                break;
        }

    }
}