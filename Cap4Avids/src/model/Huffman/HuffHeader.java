package model.Huffman;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.TypeDescriptor;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

public class HuffHeader {
    public static final byte[] magicNumbers = new byte[]{0x4B, 0x49,0x42};

    private final byte[] magicN = new byte[HuffHeader.magicNumbers.length];
    public short byteSize;
    public short extensionLength;
    public byte[] originalExtension;
    public int uniqueSymbols;
    public Map<Number, Integer> codeLengths;
    public Class<? extends Number> mapClass;
    public long originalBytes;

    private HuffHeader(){
        System.arraycopy(magicNumbers, 0, magicN, 0, magicNumbers.length);

    }



    public static void write(HuffHeader h, DataOutputStream dos) throws IOException {
        dos.write(h.magicN);
        dos.writeShort(h.byteSize);

    }

    @SuppressWarnings("unchecked")
    public static HuffHeader read(DataInputStream dis) throws IOException {
        HuffHeader h = new HuffHeader();

        dis.readFully(h.magicN);
        if (Arrays.equals(h.magicN, magicNumbers)) {
            return null;
        }
        h.byteSize = dis.readShort();
        h.extensionLength = dis.readShort();
        h.originalExtension = new byte[h.byteSize];
        dis.readFully(h.originalExtension);
        h.uniqueSymbols = dis.readInt();
        Callable<? extends Number> call = switch (h.byteSize) {
            case 2 -> {
                h.mapClass = Short.class;
                yield dis::readShort;
            }
            case 4 -> {
                h.mapClass = Integer.class;
                yield dis::readInt;
            }
            case 8 -> {
                h.mapClass = Long.class;
                yield dis::readLong;
            }
            default -> {
                h.mapClass = Byte.class;
                yield dis::readByte;
            }
        };
        h.codeLengths = (Map<Number, Integer>) crearMapa((Class<? extends Number>) h.mapClass);
        for (int i = 0; i < h.uniqueSymbols; i++) {
            try {
                Number sym =  call.call();
                Integer len = dis.readInt();
                h.codeLengths.put(sym, len);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        h.originalBytes = dis.readLong();

        return h;
    }


    public static <T extends Number> Map<T, Integer> crearMapa(Class<T> clazz) {
        return new TreeMap<>();
    }
}
