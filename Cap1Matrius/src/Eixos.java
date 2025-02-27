
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Eixos extends JPanel {

    private Main principal;

    public Eixos(int w, int h, Main p) {
        principal = p;
        this.setBounds(0, 0, w, h);
    }

    public void pintar() {
        if (this.getGraphics() != null) {
            paintComponent(this.getGraphics());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dades dad = principal.getDades();
        int w = this.getWidth() - 1;
        int h = this.getHeight() - 24;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.drawLine(10, 10, 10, h - 10);
        g.drawLine(10, h - 10, w - 10, h - 10);
        if (dad != null) {
            int maxelement = 0;
            for (int i = 0; i < dad.getTamN(); i++) {
                if (dad.getTamanyN(i) > maxelement) {
                    maxelement = dad.getTamanyN(i);
                }
            }
            long maxtemps;
            int px, py, pax, pay;
            maxtemps = 0;
            for (int i = 0; i < dad.getTamTempsSuma(); i++) {
                if (dad.getTempsSuma(i) > maxtemps) {
                    maxtemps = dad.getTempsSuma(i);
                }
            }
            for (int i = 0; i < dad.getTamTempsMult(); i++) {
                if (dad.getTempsMult(i) > maxtemps) {
                    maxtemps = dad.getTempsMult(i);
                }
            }
            // llistaSuma
            pax = 10;
            pay = h - 10;
            for (int i = 0; i < dad.getTamTempsSuma(); i++) {
                g.setColor(Color.green);
                px = dad.getTamanyN(i) * (w - 20) / maxelement;
                py = (h - 20) - ((int) (dad.getTempsSuma(i) * (h - 40) / maxtemps));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
            // llistaMult
            pax = 10;
            pay = h - 10;
            for (int i = 0; i < dad.getTamTempsMult(); i++) {
                g.setColor(Color.red);
                px = dad.getTamanyN(i) * (w - 20) / maxelement;
                py = (h - 20) - ((int) (dad.getTempsMult(i) * (h - 40) / maxtemps));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
        }
    }
}