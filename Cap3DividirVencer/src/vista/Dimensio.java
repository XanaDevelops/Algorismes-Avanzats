package vista;
public enum Dimensio {
    D2("2D"),
    D3("3D");

    private final String etiqueta;

    Dimensio(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
