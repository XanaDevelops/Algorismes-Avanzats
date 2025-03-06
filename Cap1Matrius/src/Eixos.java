
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 */

public class Eixos extends JPanel {

    private Main principal;

    public Eixos(int w, int h, Main p) {
        principal = p;
        this.setBounds(0, 0, w, h);
    }

    public synchronized void pintar() {
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
            for(int i = 0; i<dad.getSumes().size(); i++){
                Dades.Resultat r = dad.getSumes().get(i);
                if(r.getN() > maxelement){
                    maxelement = r.getN();
                }
            }
            for(int i = 0; i<dad.getMult().size(); i++){
                Dades.Resultat r = dad.getMult().get(i);
                if(r.getN() > maxelement){
                    maxelement = r.getN();
                }
            }

            long maxtemps;
            int px, py, pax, pay;
            maxtemps = 0;
            for(int i = 0; i<dad.getSumes().size(); i++){
                Dades.Resultat r = dad.getSumes().get(i);
                if (r.getTemps() > maxtemps) {
                    maxtemps = r.getTemps();
                }
            }
            for(int i = 0; i<dad.getMult().size(); i++){
                Dades.Resultat r = dad.getMult().get(i);
                if (r.getTemps() > maxtemps) {
                    maxtemps = r.getTemps();
                }
            }
            // llistaSuma
            pax = 10;
            pay = h - 10;
            for(int i = 0; i<dad.getSumes().size(); i++){
                Dades.Resultat r = dad.getSumes().get(i);
                if(maxelement == 0){
                    break;
                }
                g.setColor(Color.green);
                px = r.getN() * (w - 20) / maxelement;
                py = (h - 20) - ((int) (r.getTemps() * (h - 40) / maxtemps));
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
            for(int i = 0; i<dad.getMult().size(); i++){
                Dades.Resultat r = dad.getMult().get(i);
                if(maxelement == 0){
                    break;
                }
                g.setColor(Color.red);
                px = r.getN() * (w - 20) / maxelement;
                py = (h - 20) - ((int) (r.getTemps() * (h - 40) / maxtemps));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
        }
    }


//    public void limpiar() {
//        repaint();
//    }
}