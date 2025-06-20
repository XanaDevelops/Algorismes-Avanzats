package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;
import model.Kmeans;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FinestraColors extends JFrame implements Comunicar {

    private  List<float[]> centroides;
    private  int[] clusterCounts;
    private  int numGrups;
    private int maxIterations;
    JPanel panelBarres;
    private final static int MAX_GRUPS = 20;
    private final static int MAX_ITERATIONS = 500;
    private Dades dades;


    public FinestraColors() {
        super("Colors dominants");
        this.dades = Main.getInstance().getDades();
        this.panelBarres = barres();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = screenSize.width - this.getWidth();

        this.setLocation(x, 60);
        JPanel panellBotons = barres();

        JTextField kField = new JTextField(5);
        JTextField MaxField = new JTextField(5);

        panellBotons.add(new JLabel("Nombre de clusters"));
        panellBotons.add(kField);
        panellBotons.add(new JLabel("Max iteracions"));
        panellBotons.add(MaxField);
        JButton botoCalcular = new JButton("Calcular");
        botoCalcular.addActionListener(e -> {
            String k = kField.getText();
            try{
                int numericK = Integer.parseInt(k);
                if (numericK<=0){
                    JOptionPane.showMessageDialog(null, "El nombre de colors ha de ser major que zero");
                }else if (numericK>=MAX_GRUPS){
                    int result = JOptionPane.showConfirmDialog(this, "Has triat un nombre de clusters relativament gross. És possible que els resultats no siguin massa precisos", "Avís", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.CANCEL_OPTION){
                        return;
                    }else{
                        this.numGrups = numericK;
                    }
                }else{
                    this.numGrups = numericK;
                }
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            String k2 = MaxField.getText();
            try{
                int maxIter = Integer.parseInt(k2);
                if (maxIter <=0){
                    JOptionPane.showMessageDialog(null, "El nombre d'iteracions màxim ha de ser major que zero");
                }else if (maxIter >=MAX_ITERATIONS){
                    int result = JOptionPane.showConfirmDialog(this, "Has triat un nombre d'iteracions relativament gross.", "Avís", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.CANCEL_OPTION){
                        return;
                    }else{
                        this.maxIterations = maxIter;
                    }
                }else{
                    this.maxIterations = maxIter;
                }
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "El valor introduït no és un número vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
            }


            executarKMeans( this.numGrups, this.maxIterations);
            this.panelBarres.repaint();
        });
        panellBotons.add(botoCalcular);
        this.add(panellBotons, BorderLayout.NORTH);

        add(panelBarres, BorderLayout.CENTER);
        setVisible(true);
    }
    private JPanel barres() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (clusterCounts != null && centroides != null) {
                    int total = 0;
                    for (int count : clusterCounts) total += count;

                    int width = getWidth();
                    int height = getHeight();
                    int n = centroides.size();

                    // Nombre de columnes i files
                    int cols = (int) Math.ceil(Math.sqrt(n));
                    int rows = (int) Math.ceil((double) n / cols);

                   //espai per la informació de cada color
                    int padding = 10;
                    int textH = 30;
                    int cellWidth = width / cols;
                    int cellHeight = height / rows;


                    int squareSize = Math.min(cellWidth - 2 * padding, cellHeight - textH - 2 * padding);

                    g.setFont(new Font("Arial", Font.PLAIN, 11));

                    for (int i = 0; i < n; i++) {
                        int row = i / cols;
                        int col = i % cols;

                        int x = col * cellWidth + padding;
                        int y = row * cellHeight + padding;

                        float[] hsv = centroides.get(i);
                        Color color = Color.getHSBColor(hsv[0]*360 , hsv[1]*100, hsv[2]*100);
                        double percent = (double) clusterCounts[i] / total;

                        // Quadrat de color
                        g.setColor(color);
                        g.fillRect(x, y, squareSize, squareSize);


                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, squareSize, squareSize);


                        int textX = x;
                        int textY = y + squareSize + 12;

                        String etiqueta = String.format("H:%.0f S:%.2f V:%.2f", hsv[0]/360f , hsv[1] * 100, hsv[2] * 100);
                        String percentatge = String.format("%.1f%%", percent * 100);

                        g.drawString(etiqueta, textX, textY);
                        g.drawString(percentatge, textX, textY + 14);
                    }
                }
            }
        };
    }


    @Override
    public void comunicar(String msg) {

    }

    @Override
    public void executarKMeans(int k, int maxIt) {
        Kmeans kmeans = new Kmeans(k,maxIt);
        kmeans.run();
        this.centroides = dades.getCentroids();
        this.clusterCounts = dades.getClusterCounts();
    }
}
