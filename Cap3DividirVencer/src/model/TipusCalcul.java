package model;

public enum TipusCalcul {
    FB_MIN,
    FB_MAX,
    DV_MIN,
    KD_MIN,
    UNI_MAX;

    public String getFromString(String s){
        return null;
    }

    public static boolean isMin(TipusCalcul tipusCalcul){
        return switch (tipusCalcul) {
            case FB_MIN, DV_MIN, KD_MIN -> true;
            default -> false;
        };
    }
    public  boolean isMin(){
        return isMin(this);
    }
}
