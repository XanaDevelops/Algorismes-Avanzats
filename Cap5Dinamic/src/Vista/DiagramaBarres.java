
package Vista;

import Model.Dades;
import Model.Idioma;
import controlador.Comunicar;
import controlador.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public  class DiagramaBarres extends JPanel  {
    private final Dades dades;
    private  int height;
    private  int width;
    private  Idioma idioma;
    private final int totalIds;
    private JComboBox<Idioma> idiomaBox;
        public DiagramaBarres() {
            this.dades = Main.getInstance().getDades();
            this.width = Finestra.WIDTH_PANELL;
            this.height = Finestra.HEIGHT_PANELL;

            idioma = Idioma.ESP; // Valor inicial por defecto
            Idioma[] idiomesValids = java.util.Arrays.stream(Idioma.values())
                    .limit(Idioma.values().length - 1) //descartar la opció de tots
                    .toArray(Idioma[]::new);
            idiomaBox = new JComboBox<>(idiomesValids);
            idiomaBox.setSelectedItem(idioma);
            this.totalIds = Idioma.values().length - 1;
            setPreferredSize(new Dimension(width, height));
            setLayout(null); // Permite posiciones absolutas
            idiomaBox.setBounds(width - 1, 1, 75, 25);
            idiomaBox.addActionListener(e -> {
                Idioma seleccionat = (Idioma) idiomaBox.getSelectedItem();
                actualitzarDiagBarres(seleccionat);
            });

            add(idiomaBox);

        }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.width = getWidth();
        this.height = getHeight();
        drawTitle(g2, width);

        int marginLeft   = (int) (width * 0.12);
        int marginRight  = (int) (width * 0.05);
        int marginTop    = (int) (height * 0.10);
        int marginBottom = (int) (height * 0.10);
        int graphWidth  = width - marginLeft - marginRight;
        int graphHeight = height - marginTop - marginBottom;


        drawAxes(g2,  marginLeft, marginTop, marginBottom, graphWidth);

        int refIndex = idioma.ordinal();
        double[][] distancies = dades.getDistancies();
        double maxValor = computeMaxValor(distancies, refIndex, totalIds);
        if (maxValor == 0) {
            maxValor = 1;
        }

        drawTicks(g2, marginLeft, graphHeight, marginTop, maxValor, width);

        drawBars(g2, distancies, refIndex, totalIds, marginLeft, marginBottom, graphWidth, graphHeight, height, maxValor);

        g2.dispose();
    }

    private void drawTitle(Graphics2D g2, int panelWidth) {
        Font originalFont = g2.getFont();
        Font titleFont = new Font("Monospaced", Font.BOLD, (int) (panelWidth * 0.04));
        g2.setFont(titleFont);
        FontMetrics titleFM = g2.getFontMetrics();
        String titleText = idioma.name();
        int titleWidth = titleFM.stringWidth(titleText);
        int titleX = (panelWidth - titleWidth) / 2;
        int titleY = titleFM.getAscent() -1;

        g2.setColor(Color.BLACK);
        g2.drawString(titleText, titleX, titleY);
        g2.setFont(originalFont);
    }


    private void drawAxes(Graphics2D g2,  int marginLeft, int marginTop, int marginBottom, int graphWidth) {
        g2.setColor(Color.BLACK);

        g2.drawLine(marginLeft, height - marginBottom, marginLeft + graphWidth, height - marginBottom);

        g2.drawLine(marginLeft, height - marginBottom, marginLeft, marginTop);
    }

    private double computeMaxValor(double[][] distancies, int refIndex, int totalIdiomas) {
        double maxValor = 0;
        for (int i = 0; i < totalIdiomas; i++) {
            if (i == refIndex) continue;
            maxValor = Math.max(maxValor, distancies[refIndex][i]);
        }
        return maxValor;
    }

    private void drawTicks(Graphics2D g2, int marginLeft, int graphHeight, int marginTop, double maxValor, int panelWidth) {
        int nTicks = 10;
        double tickIncrement = maxValor / nTicks;
        Font tickFont = new Font("Monospaced", Font.BOLD, (int) (panelWidth * 0.03));
        g2.setFont(tickFont);
        FontMetrics fm = g2.getFontMetrics();
        for (int i = 0; i <= nTicks; i++) {
            double tickValue = i * tickIncrement;
            int tickY = (int) (marginTop + graphHeight * (1 - (tickValue / maxValor)));
            g2.drawLine(marginLeft - 5, tickY, marginLeft, tickY);
            String tickLabel = String.format("%.2f", tickValue);
            int labelWidth = fm.stringWidth(tickLabel);
            g2.drawString(tickLabel, marginLeft - 10 - labelWidth, tickY + fm.getAscent() / 2);
        }
    }

    private void drawBars(Graphics2D g2, double[][] distancies, int refIndex, int totalIdiomas, int marginLeft, int marginBottom, int graphWidth, int graphHeight, int panelHeight, double maxValor) {
        int numBarras = totalIdiomas - 1;
        int spacePerCategory = graphWidth / numBarras;
        int barWidth = (int) (spacePerCategory * 0.6);
        FontMetrics fm = g2.getFontMetrics();
        int currentBarIndex = 0;
        for (int i = 0; i < totalIdiomas; i++) {
            if (i == refIndex) continue;
            double distancia = distancies[refIndex][i];

            if (distancies[refIndex][i] ==0.0) {
                continue;
            }
            int barHeight = (int) ((distancia / maxValor) * graphHeight);
            int x = marginLeft + currentBarIndex * spacePerCategory + (spacePerCategory - barWidth) / 2;
            int y = panelHeight - marginBottom - barHeight;

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, barWidth, barHeight);

            String idiomaLabel = Idioma.values()[i].name();
            int labelWidth = fm.stringWidth(idiomaLabel);
            int labelX = marginLeft + currentBarIndex * spacePerCategory + (spacePerCategory - labelWidth) / 2;
            int labelY = panelHeight - marginBottom + fm.getHeight() + 5;
            g2.drawString(idiomaLabel, labelX, labelY);

            String valorTexto = String.format("%.2f", distancia);
            int valorWidth = fm.stringWidth(valorTexto);
            int valorX = x + (barWidth - valorWidth) / 2;
            int valorY = y - 5;
            g2.drawString(valorTexto, valorX, valorY);

            currentBarIndex++;
        }
    }




    public void actualitzarDiagBarres(Idioma id){
            this.idioma = id;
            revalidate();
            repaint();
    }


}
