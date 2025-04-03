package model;

public class Punt2D {
    public final int x, y;

    public Punt2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distancia(Punt2D altre) {
        double dx = this.x - altre.x;
        double dy = this.y - altre.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
