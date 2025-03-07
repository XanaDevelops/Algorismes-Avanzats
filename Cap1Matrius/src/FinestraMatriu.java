
import javax.swing.*;
import java.awt.*;

/**
 * Classe que manipula la finestra principal del programa.
 *
 */
public class FinestraMatriu extends JPanel implements Comunicar {
    /**
     * Instància de la classe del programa principal
     */
    private final Main principal;
    /**
     *
     */
    private Eixos dibuixMatrius;
    private JTextField nField;
    private  JPanel panelLlagenda;

    private JProgressBar sumarBar, multiplicarBar;

    private final String[] missatgesBotons = {"comencar", "suma", "multiplicar", "aturar", "net"};
    private final String[] textBotons = {"Començar", "Només Sumar","Només Multiplicar", "Aturar", "Netejar" };
    private final JButton[] botons = new JButton[missatgesBotons.length];
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

        for (int i = 0; i < botons.length; i++) {
            botons[i] = new JButton(textBotons[i]);
            int finalI = i;
            // Afegir listeners als botons
            botons[i].addActionListener(e -> enviar(missatgesBotons[finalI]));
            //Afegir els botons al panell
            topBar.add(botons[i]);
        }

        //Llegenda
        panelLlagenda = new JPanel();
        panelLlagenda.setLayout(new FlowLayout(FlowLayout.LEFT));
      //Etiquetes de la gràfica
        JLabel etiquetaS = new JLabel("Verd: Suma");
        etiquetaS.setForeground(Color.GREEN);
        JLabel etiquetaM = new JLabel("Vermell: Multiplicar");
        etiquetaM.setForeground(Color.RED);
        panelLlagenda.add(etiquetaS);
        panelLlagenda.add(etiquetaM);

        panelLlagenda.setSize(panelLlagenda.getPreferredSize());
        panelLlagenda.setLocation(100, 50);
        this.add(panelLlagenda);

        setAndAddBar(sumarBar, "S", topBar);
        setAndAddBar(multiplicarBar,"M", topBar);

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

    /**
     * Inicialitza i afegeix la barra b i el text corresponent al JPanel panel.
     * @param panel panell al qual s'afegiran b i text
     */
    private void setAndAddBar(JProgressBar b, String tipus,  JPanel panel) {
        JLabel text;
        text = new JLabel(tipus + ":");
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
        if (s.startsWith("pintar")) {
            panelLlagenda.repaint();
            dibuixMatrius.pintar();
        }else if (s.startsWith("activar")){
            String[] split = s.split(":");
            if(split[1].startsWith("sumar")){
                sumarBar.setIndeterminate(true);
            }else if (split[1].startsWith("multiplicar")){
                multiplicarBar.setIndeterminate(true);
            }
        }else if (s.startsWith("desactivar")){
            String[] split = s.split(":");
            if (split[1].startsWith("sumar")) {
                sumarBar.setIndeterminate(false);
            }
            if(split[1].startsWith("multiplicar")){
                multiplicarBar.setIndeterminate(false);
            }
        }
    }
}