package model;

public enum Paisatge {
    BOSC_NORDIC      (60f, 150f, 0.2f, 0.5f, 0.2f, 0.8f),
    SELVA_TROPICAL   (60f, 150f, 0.5f, 1.0f, 0.5f, 1.0f),
    PAISATGE_COSTANER(0f,  360f, 0.0f, 1.0f, 0.0f, 1.0f);

    private final float hMin, hMax, sMin, sMax, vMin, vMax;

    Paisatge(float hMin, float hMax,
             float sMin, float sMax,
             float vMin, float vMax) {
        this.hMin = hMin; this.hMax = hMax;
        this.sMin = sMin; this.sMax = sMax;
        this.vMin = vMin; this.vMax = vMax;
    }

    /** Retorna true si (H,S,V) cau dins del rang d’aquest paisatge */
    public boolean matches(float H, float S, float V) {
        return H >= hMin && H <= hMax
                && S >= sMin && S <= sMax
                && V >= vMin && V <= vMax;
    }

    @Override
    public String toString() {
        switch(this) {
            case BOSC_NORDIC:       return "Bosc Nòrdic";
            case SELVA_TROPICAL:    return "Selva Tropical";
            default:                return "Paisatge Costaner";
        }
    }
}