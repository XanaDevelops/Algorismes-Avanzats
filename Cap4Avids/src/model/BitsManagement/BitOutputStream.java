package model.BitsManagement;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Escritor de bits MSB-first a partir d'un stream.
 */
public  class BitOutputStream implements Closeable, Flushable {
    private final OutputStream out;
    private int currentByte = 0;
    private int bitPos = 0;

    public BitOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writeBit(boolean bit) throws IOException {
        // Desplaça els bits anteriors a l'esquerra i afegeix el nou al final
        currentByte = (currentByte << 1) | (bit ? 1 : 0);
        bitPos++;
        //si el byte ja és complet, s'escriu al stream
        if (bitPos == 8) {
            out.write(currentByte);
            bitPos = 0;
            currentByte = 0;
        }
    }

    public void writeBits(String bits) throws IOException {
        for (char c : bits.toCharArray()) writeBit(c == '1');
    }

    @Override
    public void flush() throws IOException {
        //si hi ha qualque byte incomplet, se li afegeix el padding i s'escriu al stream
        if (bitPos > 0) {
            currentByte <<= (8 - bitPos);
            out.write(currentByte);
            bitPos = 0;
            currentByte = 0;
        }
        out.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        out.close();
    }
}