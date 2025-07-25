package model.punts;

public class Punt3D extends Punt2D implements Punt{
    public final int z;

    public Punt3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public double distancia(Punt punt2) {
        Punt3D altre = (Punt3D) punt2;

        long dx = this.getX() - altre.getX();

        long dy = this.getY() - altre.getY();

        long dz = this.z - altre.z;

//        return Math.sqrt(Math.pow(dx, 2 ) + Math.pow(dy, 2) + Math.pow( dz ,2 ));
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {  //mateixa instància
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Punt3D other = (Punt3D) obj; //mateixes coordenades
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "Punt3D{" +
                "y=" + y +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
