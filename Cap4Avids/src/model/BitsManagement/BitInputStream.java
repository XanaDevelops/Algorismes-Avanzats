package model.BitsManagement;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Lector de bits MSB-first a partir d'un stream donat.
 */
public class BitInputStream implements Closeable {
    private final InputStream in;
    private int currentByte = 0;
    private int bitPos = 0;

    public BitInputStream(InputStream in) {
        this.in = in;
    }
    //retorna el true si el bit més significatiu és 1

    public boolean readBit() throws IOException {
        if (bitPos == 0) {
            currentByte = in.read();
            if (currentByte < 0) throw new EOFException();
            bitPos = 8;
        }
        bitPos--;
        return ((currentByte >> bitPos) & 1) == 1;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
