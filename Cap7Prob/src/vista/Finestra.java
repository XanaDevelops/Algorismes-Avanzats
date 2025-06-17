package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Finestra extends JFrame implements Comunicar {

    @Override
    public void comunicar(String msg) {
        System.err.println(msg);
    }
}
