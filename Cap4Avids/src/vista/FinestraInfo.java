package vista;

import control.Comunicar;

import javax.swing.*;
import java.awt.*;

public class FinestraInfo extends JFrame implements Comunicar {
    PanellInfo p = new PanellInfo();

    public FinestraInfo() {


        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(400, 400));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width -500, 60);

        this.setResizable(false);
        this.add(p, BorderLayout.CENTER);


        this.setVisible(true);
    }


    @Override
    public void estadistiquesLLestes() {
        p.estadistiquesLLestes();
    }
}
