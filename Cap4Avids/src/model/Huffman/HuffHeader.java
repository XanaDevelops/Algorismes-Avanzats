package model.Huffman;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

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
        dos.writeShort(h.extensionLength);
        dos.write(h.originalExtension);
        dos.writeInt(h.uniqueSymbols);
        Consumer<Number> call = getWriteConsumer(dos, h);
        for(Map.Entry<Number, Integer> entry : h.codeLengths.entrySet()){
            call.accept(entry.getKey());
            dos.writeInt(entry.getValue());
        }
        dos.writeLong(h.originalBytes);
        dos.flush();
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
        Callable<? extends Number> call = getReadCallable(dis, h);
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

    private static @NotNull Consumer<Number> getWriteConsumer(DataOutputStream dos, HuffHeader h) {
        Consumer<Number> call;
        switch (h.byteSize) {
            case 2 -> {
                h.mapClass = Short.class;
                call = n -> {
                    try {
                        dos.writeShort(n.shortValue());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                };
            }
            case 4 -> {
                h.mapClass = Integer.class;
                call = n -> {
                    try {
                        dos.writeInt(n.intValue());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                };
            }
            case 8 -> {
                h.mapClass = Long.class;
                call = n -> {
                    try {
                        dos.writeLong(n.longValue());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                };
            }
            default -> {
                h.mapClass = Byte.class;
                call = n -> {
                    try {
                        dos.writeByte(n.byteValue());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                };
            }
        }
        return call;
    }

    private static @NotNull Callable<? extends Number> getReadCallable(DataInputStream dis, HuffHeader h) {
        return switch (h.byteSize) {
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
    }


    public static <T extends Number> Map<T, Integer> crearMapa(Class<T> clazz) {
        return new TreeMap<>();
    }
}
