package vista;

import controlador.Comunicar;

import javax.swing.*;
import java.awt.*;

public class FinestraTempsExec extends JFrame implements Comunicar {

    private EixosTempsExec eixosTempsExec;
    public FinestraTempsExec() {
        super("Temps d'execució");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 600));
        eixosTempsExec = new EixosTempsExec(this.getWidth(), this.getHeight());
        this.add(eixosTempsExec);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void comunicar(String s) {
        switch (s){
            case "pintaElement":
                eixosTempsExec.pintar();
                break;



        }
    }
}
