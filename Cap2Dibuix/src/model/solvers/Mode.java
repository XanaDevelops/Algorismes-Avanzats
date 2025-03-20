package model.solvers;

public enum Mode {
    LD ("LD",  0),
    RD("RD",1),
    LU("LU", 2),
    RU("RU", 3);

    private final String type;
    private final int value;

    Mode(String type, int i) {
        this.type= type;
        this.value = i;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
