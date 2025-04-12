package model.punts;

public class Punt2D implements Punt {
    public final int x, y;

    public Punt2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public double distancia(Punt punt2) {
        Punt2D altre = (Punt2D) punt2;

        double dx = this.x - altre.x;
        double dy = this.y - altre.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "Punt2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
