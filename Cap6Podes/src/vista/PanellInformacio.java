package vista;

import model.Result;

import javax.swing.*;
import java.awt.*;

public class PanellInformacio extends JPanel {
    private JLabel lblCostTotal;
    private JLabel lblBranquesExplorades;
    private JLabel lblNodesDescartats;
    private JLabel lblEfectePoda;
    private JTextArea areaLog;

    JLabel[] labels;

    public PanellInformacio() {
        setLayout(new BorderLayout());

        JPanel panellDades = new JPanel(new GridLayout(4, 2));
        lblCostTotal = new JLabel("Cost total: ");
        lblBranquesExplorades = new JLabel("Explorades: ");
        lblNodesDescartats = new JLabel("Nodes descartats: ");
        lblEfectePoda = new JLabel("Efecte de la poda: ");

        labels = new JLabel[4];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("---");
        }

        panellDades.add(lblCostTotal);
        panellDades.add(labels[0]);
        panellDades.add(lblBranquesExplorades);
        panellDades.add(labels[1]);
        panellDades.add(lblNodesDescartats);
        panellDades.add(labels[2]);
        panellDades.add(lblEfectePoda);
        panellDades.add(labels[3]);

        areaLog = new JTextArea(4, 30);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scroll = new JScrollPane(areaLog);

        add(panellDades, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Informació del procés"));
    }

    public void mostrarResultat(Result resultat){

        labels[0].setText(""+resultat.costTotal);
        labels[1].setText(""+resultat.branquesExplorades);
        labels[2].setText(""+resultat.nodesDescartats);
        labels[3].setText(String.format("%.2f %%", resultat.nodesTotals == 0 ?
                0 :
                (resultat.nodesDescartats / (double) resultat.nodesTotals)*100f));
    }

    public void log(String missatge) {
        areaLog.append(missatge + "\n");
    }

    public void netejarLog() {
        areaLog.setText("");
    }
}

