package model.punts;

public interface Punt {


    double distancia(Punt punt2);

    int getX();

     int getY();

    default int getZ() {
        return 0; // per defecte: 0 per 2D, sobreescrit en 3D
    }

}
