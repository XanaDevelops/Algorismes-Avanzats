package Vista;

import controlador.Comunicar;

import javax.swing.*;

public class Finestra extends JFrame implements Comunicar {

    public Finestra() {

    }

    @Override
    public void comunicar(String s) {

    }

    @Override
    public void actualitzar() {
        throw new UnsupportedOperationException("HOLA :D");
    }
}
