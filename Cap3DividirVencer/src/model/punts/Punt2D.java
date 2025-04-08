package model.punts;

public class Punt2D extends Punt {
    private final int x, y;

    public Punt2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


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
