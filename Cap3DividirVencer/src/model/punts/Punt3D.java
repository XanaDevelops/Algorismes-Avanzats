package model.punts;

public class Punt3D extends Punt2D {
    public final int z;

    public Punt3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public double distancia(Punt punt2) {
        Punt3D altre = (Punt3D) punt2;

        int dx = this.getX() - altre.getX();
        int dy = this.getY() - altre.getY();
        int dz = this.z - altre.z;
        return Math.sqrt(dx * dx + dy * dy  + dz * dz);
    }
}
