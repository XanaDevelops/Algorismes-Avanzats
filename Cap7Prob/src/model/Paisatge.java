package model;

public enum Paisatge {
    BOSC_NORDIC       (60f, 150f, 0.10f, 0.55f, 0.15f, 0.85f),
    SELVA_TROPICAL    (65f, 160f, 0.55f, 1.00f, 0.30f, 1.00f),
    PAISATGE_COSTANER (170f,260f, 0.35f, 1.00f, 0.35f, 1.00f);
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