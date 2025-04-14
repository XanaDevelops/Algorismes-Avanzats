package model.generadors;

import controlador.Main;
import model.punts.Punt;

import java.util.List;
import java.util.Random;

public abstract class Generador extends Random {
    protected int n;
    protected int min;
    protected int max;
    protected Random rand;

    public Generador(int n, int min, int max) {
        this(n, min, max, new Random());
    }

    public Generador(int n, int min, int max, Random rand) {
        this.n = n;
        this.min = min;
        this.max = max;
        this.rand = rand;
    }

    public abstract List<Punt> genera2D();

    public abstract List<Punt> genera3D();
}

