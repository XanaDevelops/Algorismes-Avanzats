package vista;

import controlador.Comunicar;

import javax.swing.*;
import java.awt.*;

public class FinestraTempsExec extends JFrame implements Comunicar {

    private final EixosTempsExec eixosTempsExec;
    JPanel llegenda;

    protected final static Color VERD = new Color(34, 139, 34);
    protected final static Color VERMELL =  new Color(178, 34, 34);
    protected final static Color BLUE = new Color(102, 178, 255);
    public FinestraTempsExec() {
        super("Temps d'execució");
        llegenda = afegirLlegenda();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 600));
        eixosTempsExec = new EixosTempsExec(this.getWidth(), this.getHeight());
        this.add(llegenda, BorderLayout.NORTH);
        this.add(eixosTempsExec, BorderLayout.CENTER);
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - this.getWidth();
        int y = 0;
        this.setLocation(x, y);
        this.setVisible(true);
    }

    private JPanel afegirLlegenda() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        afegirEtiqueta("Dividir i Vèncer", VERD, panel);
        afegirEtiqueta("Força Bruta",VERMELL, panel);
        afegirEtiqueta("Kd-Arbre", BLUE, panel);
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
            case "pintaElement":
                eixosTempsExec.pintar();
                break;
            default:break;



        }
    }
}
