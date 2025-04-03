package model;

public class Punt3D extends Punt2D {
    public final int z;

    public Punt3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public double distancia(Punt3D altre) {
        int dx = this.x - altre.x;
        int dy = this.y - altre.y;
        int dz = this.z - altre.z;
        return Math.sqrt(dx * dx + dy * dy  + dz * dz);
    }
}
