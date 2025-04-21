package model;

import control.Main;

public class Dades {
    private final Main principal;

    public boolean isEsSerialitzable() {
        return esSerialitzable;
    }

    public void setEsSerialitzable(boolean esSerialitzable) {
        this.esSerialitzable = esSerialitzable;
    }

    private boolean esSerialitzable;

    public Dades(boolean esSerialitzable) {
        principal = Main.instance;
    }
    public Dades() {
        principal = Main.instance;
    }

}
