package model;
public enum Extensio {
    RAR(new byte[] { 0x52, 0x61, 0x72, 0x21, 0x01, 0x07, 0x00 }),
    ZIP(new byte[] { 0x50, 0x4B, 0x03, 0x04 }),
    LZH(new byte[] { 0x2D, 0x6C, 0x68, 0x30, 0x2D });

    private final byte[] magicBytes;

    Extensio(byte[] magicBytes) {
        this.magicBytes = magicBytes;
    }

    public byte[] getMagicBytes() {
        return magicBytes;
    }
    public static boolean notValid(byte[] bytes) {
        return bytes == RAR.getMagicBytes() || bytes == ZIP.getMagicBytes() || bytes == LZH.getMagicBytes();
    }

}
