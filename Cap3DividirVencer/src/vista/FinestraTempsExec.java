package vista;

import controlador.Comunicar;

import javax.swing.*;
import java.awt.*;

public class FinestraTempsExec extends JFrame implements Comunicar {

    private final EixosTempsExec eixosTempsExec;


    protected final static Color VERD = new Color(34, 139, 34);
    protected final static Color VERMELL =  new Color(178, 34, 34);
    protected final static Color BLUE = new Color(102, 178, 255);

    protected final static Color MVERD = new Color(146, 191, 177);

    protected final static Color ORANGE = new Color(244, 172, 69);
    protected final static Color BLACK = Color.black;

    public FinestraTempsExec() {
        super("Temps d'execució");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 600));
        eixosTempsExec = new EixosTempsExec(this.getWidth(), this.getHeight());
        JPanel llegendes = new JPanel(new BorderLayout());
        llegendes.add(afegirLlegendaMin(), BorderLayout.NORTH);
        llegendes.add(afegirLlegendaMax(), BorderLayout.CENTER);
        this.add(llegendes, BorderLayout.NORTH);

        this.add(eixosTempsExec, BorderLayout.CENTER);
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - this.getWidth();
        int y = 0;
        this.setLocation(x, y);
        this.setResizable(false);
        this.setVisible(true);
    }

    private JPanel afegirLlegendaMin() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        panel.add(new JLabel("Min: "));
        afegirEtiqueta("Dividir i Vèncer", VERD, panel);
        afegirEtiqueta("Força Bruta",VERMELL, panel);
        afegirEtiqueta("Kd-Arbre", BLUE, panel);
        afegirEtiqueta("x: Nº punts  y: log10(Temps)", Color.DARK_GRAY, panel);
        return panel;
    }

    private JPanel afegirLlegendaMax() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        panel.add(new JLabel("Max: "));
        afegirEtiqueta("Convex Hull", MVERD, panel);
        afegirEtiqueta("Força Bruta",ORANGE, panel);
        afegirEtiqueta("Uniforme", BLACK, panel);
        afegirEtiqueta("x: Nº punts  y: log10(Temps)", Color.DARK_GRAY, panel);
        return panel;
    }


    /**
     * Crea i afegeix una etiqueta al panel
     * @param txt text de l'etiqueta
     * @param color color del text
     */

    private void afegirEtiqueta(String txt, Color color, JPanel panel) {
        JLabel colorDot = new JLabel("●");
        colorDot.setForeground(color);
        colorDot.setFont(new Font("Dialog", Font.BOLD, 12));

        JLabel etiqueta = new JLabel(txt);
        etiqueta.setFont(new Font("Verdana", Font.PLAIN, 13));

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        subPanel.setOpaque(false);
        subPanel.add(colorDot);
        subPanel.add(etiqueta);

        panel.add(subPanel);
    }

    @Override
    public void comunicar(String s) {
        switch (s){
            case "pintaElement", "pintar":
                eixosTempsExec.pintar();
                break;
            default:break;



        }
    }
}
