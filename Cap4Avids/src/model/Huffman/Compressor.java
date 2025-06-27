package model.Huffman;

import model.BitsManagement.BitOutputStream;
import model.Dades;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Compressor extends Proces {
    private final Huffman huffman;
    private final String inputPath;
    private final String outputFolder;

    private final ArrayList<Boolean>[] fileChunks = new ArrayList[N_THREADS];


    public Compressor(int id, Huffman.WordSize wordSize, Huffman.TipusCua cua, String inputPath, String outputFolder) {
        super(id);
        this.huffman = new Huffman(inputPath, wordSize, cua);
        this.inputPath = inputPath;
        this.outputFolder = outputFolder;

    }


    /**
     * Comprimeix el fitxer d'entrada.
     * La capçalera té la següent forma
     * (Byte) --> nombre de caracters únics de l'arxiu
     * Conjunt de bytes (Simbol, longitud) --> forma canònica de la codificació de huffman
     * A baix de la capçalera s'escriu el contingut codificat de huffman
     *
     * @throws IOException
     */

    public void compressFile() throws IOException {
        System.err.println("comprimint " + inputPath + " to " + outputFolder);
        System.err.println("settings bytes: " + huffman.getByteSize());

        huffman.run();
        Map<Long, String> table = huffman.getTable();
        long time = System.nanoTime();
        //calcular la longitud de les codificaciones de cada byte
        Map<Long, Integer> codeLengths = new TreeMap<>();
        int totalUnicSymbols = 0;
        List<Long> symbols = new ArrayList<>();
        for (Map.Entry<Long, String> e : table.entrySet()) {
            long sym = e.getKey(); //byte positiu
            codeLengths.put(sym, e.getValue().length());
            symbols.add(sym);
            totalUnicSymbols++;
        }
        //generar codi canònic a partir les longituds dels símbols
        Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);

        //afegir la signatura de l'extensió manualment


        String fileName = Path.of(inputPath).getFileName().toString();

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        System.out.println("File: " +outputFolder +"\\"+ fileName + Dades.EXTENSIO);
        try (OutputStream fos = Files.newOutputStream(Path.of(outputFolder +"\\"+ fileName + Dades.EXTENSIO));
             BufferedOutputStream bufOut = new BufferedOutputStream(fos);
             DataOutputStream dos = new DataOutputStream(bufOut);
             BitOutputStream bitOut = new BitOutputStream(bufOut)) {

            /// HEADER
            //guardar l'extensió original de l'arxiu
            HuffHeader huffH = new HuffHeader();

//            dos.write(Dades.magicNumbers);

            //guardar el tamany de les paraules comprimides
            huffH.byteSize= (short) huffman.getByteSize();
//            dos.writeShort(huffman.getByteSize());

            Path inputPath = Path.of(this.inputPath);
            String[] extension = this.inputPath.split("\\.", 2);
            byte[] extensionBytes = extension[1].getBytes(StandardCharsets.UTF_8);
            //tamany de l'extensió
            huffH.extensionLength= (short) extensionBytes.length;
            //l'extensió en sí
             huffH.originalExtension = extensionBytes;
             huffH.uniqueSymbols = totalUnicSymbols;

            huffH.codeLengths= codeLengths;

            huffH.originalBytes = Files.size(inputPath);
            huffH.mapClass = switch (huffH.byteSize) {
                case 1 -> Short.class;
                case 2 -> Integer.class;
                case 4 -> Long.class;
                default -> Byte.class;
            };
            HuffHeader.write(huffH, dos);
            dos.flush();

            //Escriure la codificació del contingut de l'arxiu d'entrada
            try (InputStream fis = new BufferedInputStream(Files.newInputStream(inputPath))) {
                byte[] bfis = fis.readAllBytes();
                int split = ((bfis.length/huffH.byteSize) / N_THREADS)*huffH.byteSize;
                System.err.println("C: split = " + split + ", bytes = " + bfis.length + ", " + (split*bfis.length));
                for (int i = 0; i < N_THREADS - 1; i++) {
                    int finalI = i;
                    executar(() -> {
                        comprimir(finalI, bfis, finalI*split, (finalI+1)*split, canonCodes, huffH.byteSize);
                    });
                }
                executar(()-> {
                    comprimir(N_THREADS-1, bfis, (N_THREADS-1) * split, bfis.length, canonCodes, huffH.byteSize);
                });

                waitAll();
                for(ArrayList<Boolean> fileChunk : fileChunks) {
                    for(Boolean b : fileChunk) {
                        bitOut.writeBit(b);
                    }
                }
            }
            bitOut.flush();
        }


        time = System.nanoTime() - time;
        System.err.println("end " + id);
//        data.addTempsCompressio(time, fileName, huffman.getTipusCua);
    }

    private void comprimir(int id, byte[] arr, int ini, int fi, Map<Long, byte[]> canonCodes, int byteSize) {
        ArrayList<Boolean> fileChunk = new ArrayList<>();
        for (int i = ini; i < arr.length && i<fi; i+=byteSize) {
            long b = arr[i];
            for (int j = 1; j < byteSize; j++) {
                b <<= 8;
                int aux;
                if((i+j)>=arr.length)
                    aux = 0;
                else{
                    aux = arr[i+j];
                }
                b |= aux;
            }
            byte[] codeBits = canonCodes.get(b);
            assert codeBits != null;
            for (byte codeBit : codeBits) {
                fileChunk.add(codeBit == 1);
            }
        }
        fileChunks[id] = fileChunk;
    }




    @Override
    protected void exec() {
        try {
            this.compressFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}