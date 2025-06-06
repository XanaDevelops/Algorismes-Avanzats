package vista;

import javax.swing.*;
import java.awt.*;

public class PanellInformacio extends JPanel {
    private JLabel lblCostTotal;
    private JLabel lblBranquesExplorades;
    private JLabel lblNodesDescartats;
    private JLabel lblEfectePoda;
    private JTextArea areaLog;

    public PanellInformacio() {
        setLayout(new BorderLayout());

        JPanel panellDades = new JPanel(new GridLayout(4, 1));
        lblCostTotal = new JLabel("Cost total: ");
        lblBranquesExplorades = new JLabel("Branques explorades: ");
        lblNodesDescartats = new JLabel("Nodes descartats: ");
        lblEfectePoda = new JLabel("Efecte de la poda: ");

        panellDades.add(lblCostTotal);
        panellDades.add(lblBranquesExplorades);
        panellDades.add(lblNodesDescartats);
        panellDades.add(lblEfectePoda);

        areaLog = new JTextArea(4, 30);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scroll = new JScrollPane(areaLog);

        add(panellDades, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Informació del procés"));
    }

    public void setCost(int cost) {
        lblCostTotal.setText("Cost total: " + cost);
    }

    public void setBranques(int n) {
        lblBranquesExplorades.setText("Branques explorades: " + n);
    }

    public void setDescartats(int n) {
        lblNodesDescartats.setText("Nodes descartats: " + n);
    }

    public void setEfectePoda(String text) {
        lblEfectePoda.setText("Efecte de la poda: " + text);
    }

    public void log(String missatge) {
        areaLog.append(missatge + "\n");
    }

    public void netejarLog() {
        areaLog.setText("");
    }
}

