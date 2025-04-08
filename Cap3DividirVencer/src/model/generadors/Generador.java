package model.generadors;

import model.punts.Punt;

import java.util.List;

public abstract class Generador {
    protected int n;
    protected int min;
    protected int max;

    public Generador(int n, int min, int max) {
        this.n = n;
        this.min = min;
        this.max = max;
    }

    public abstract List<Punt> genera();
}

