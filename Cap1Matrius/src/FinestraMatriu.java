
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FinestraMatriu extends JPanel implements Comunicar {

    private final Main principal;
    private Eixos dibuixMatrius;
    private JTextField nField;
    private  JPanel panelLlagenda;

    private JProgressBar sumarBar, multiplicarBar;

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
        JButton botoNet = new JButton("Netejar");

        //Llegenda
        panelLlagenda = new JPanel();
        panelLlagenda.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaS = new JLabel("Suma");
        etiquetaS.setForeground(Color.GREEN);
        JLabel etiquetaM = new JLabel("     Multiplicar");
        etiquetaM.setForeground(Color.RED);
        JLabel etiquetaEixos = new JLabel("     x: N    y: log10(Temps)");
        etiquetaEixos.setForeground(Color.BLACK);
        panelLlagenda.add(etiquetaS);
        panelLlagenda.add(etiquetaM);
        panelLlagenda.add(etiquetaEixos);
        panelLlagenda.setSize(panelLlagenda.getPreferredSize());
        panelLlagenda.setLocation(100, 50);
        this.add(panelLlagenda);

        //marcadors Execució
        JLabel textSuma = new JLabel("S:");
        JLabel textMultiplicar = new JLabel("M:");
        sumarBar = new JProgressBar();
        sumarBar.setIndeterminate(false); //desactiva la barra
        sumarBar.setPreferredSize(new Dimension(50, sumarBar.getPreferredSize().height));
        multiplicarBar = new JProgressBar();
        multiplicarBar.setIndeterminate(false);
        multiplicarBar.setPreferredSize(new Dimension(50, multiplicarBar.getPreferredSize().height));

        topBar.add(nLabel);
        topBar.add(nField);
        topBar.add(comencarBoto);
        topBar.add(sumaBoto);
        topBar.add(multBoto);
        topBar.add(aturarBoto);
        topBar.add(botoNet);
        topBar.add(textSuma);
        topBar.add(sumarBar);
        topBar.add(textMultiplicar);
        topBar.add(multiplicarBar);



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
        botoNet.addActionListener(e -> enviar("net"));

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