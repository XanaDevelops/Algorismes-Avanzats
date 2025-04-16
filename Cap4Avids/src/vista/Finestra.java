package vista;

import control.Comunicar;
import control.Main;
import model.Dades;

import javax.swing.*;

public class Finestra extends JFrame implements Comunicar {

    private final Dades dades;
    private final Comunicar principal;

    public Finestra() {
        principal = Main.instance;
        dades = Main.instance.getDades();
    }


    /**
     * Envia un missatge
     *
     * @param s El missatge
     */
    @Override
    public void comunicar(String s) {
        String[] args = s.split(":");
        switch (args[0]) {
            default -> System.err.println("WARNING: Finestra reb missatge?: " + s);
        }
    }
}
