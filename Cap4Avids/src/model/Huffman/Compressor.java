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

    private final ByteArrayOutputStream[] fileChunks = new ByteArrayOutputStream[N_THREADS];
    private final int[] chunksSizes = new int[N_THREADS];

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
        Map<Long, Integer> codeLengths = new TreeMap<>(Long::compareUnsigned);
        int totalUnicSymbols = 0;
        List<Long> symbols = new ArrayList<>();
        double longitudMitjana = 0;
        Map<Long, Long> freqs = huffman.getFreqs();
        for (Map.Entry<Long, String> e : table.entrySet()) {
            long sym = e.getKey(); //byte positiu
            codeLengths.put(sym, e.getValue().length());
            longitudMitjana+=e.getValue().length()*freqs.get(sym);
            symbols.add(sym);
            totalUnicSymbols++;
        }
        //generar codi canònic a partir les longituds dels símbols
        Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);

        //afegir la signatura de l'extensió manualment


        String fileName = Path.of(inputPath).getFileName().toString();

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        System.out.println("File: " +outputFolder +"\\"+ fileName + Dades.EXTENSIO);
        String outputPath = outputFolder +"\\"+ fileName + Dades.EXTENSIO;
        Path outputFile = Path.of(outputPath);
        try (OutputStream fos = Files.newOutputStream(outputFile);
             BufferedOutputStream bufOut = new BufferedOutputStream(fos);
             DataOutputStream dos = new DataOutputStream(bufOut);
             BitOutputStream bitOut = new BitOutputStream(bufOut)) {

            /// HEADER
            //guardar l'extensió original de l'arxiu
            HuffHeader huffH = new HuffHeader();

            //guardar el tamany de les paraules comprimides
            huffH.byteSize= (byte) huffman.getByteSize();

            Path inputPath = Path.of(this.inputPath);
            String[] extension = this.inputPath.split("\\.", 2);
            byte[] extensionBytes = extension[1].getBytes(StandardCharsets.UTF_8);
            //tamany de l'extensió
            huffH.extensionLength= (byte) extensionBytes.length;
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


            //Escriure la codificació del contingut de l'arxiu d'entrada
            try (InputStream fis = new BufferedInputStream(Files.newInputStream(inputPath))) {
                byte[] bfis = fis.readAllBytes();
                int split = Math.max(((bfis.length/huffH.byteSize) / N_THREADS)*huffH.byteSize, 256);
                //System.err.println("C: split = " + split + ", bytes = " + bfis.length + ", " + (split*N_THREADS));
                for (int i = 0; i < N_THREADS - 1 && i * split < bfis.length; i++) {
                    int finalI = i;
                    executar(() -> {
                        comprimir(finalI, bfis, finalI*split, (finalI+1)*split, canonCodes, huffH.byteSize);
                    });
                }
                executar(()-> {
                    comprimir(N_THREADS-1, bfis, (N_THREADS-1) * split, bfis.length, canonCodes, huffH.byteSize);
                });

                waitAll();
                updateProgress(90);

                huffH.bitTamChunks = chunksSizes;
                HuffHeader.write(huffH, dos);
                dos.flush();

                for(ByteArrayOutputStream fileChunk : fileChunks) {
                    if(fileChunk == null) {
                        continue;
                    }
                    fileChunk.writeTo(bufOut);
                }
            }
            bitOut.flush();
            time = System.nanoTime() - time;
            updateProgress(100);
            System.err.println("end " + id);
            Dades.Informacio info = new Dades.Informacio(huffman.getEntropia(), Files.size(inputPath),Files.size(outputFile),totalUnicSymbols,longitudMitjana);
            info.setTempsCompressio(time);
            dades.setInfo(info);
//        data.addTempsCompressio(time, fileName, huffman.getTipusCua);
        }
    }

    private void comprimir(int id, byte[] arr, int ini, int fi, Map<Long, byte[]> canonCodes, int byteSize) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(fi-ini, 0));
             BitOutputStream bos2 = new BitOutputStream(bos)) {
            for (int i = ini; i < arr.length && i<fi; i+=byteSize) {
                long b = (long) arr[i] & (0xFFL);
                for (int j = 1; j < byteSize; j++) {
                    b <<= 8;
                    int aux;
                    if((i+j)>=arr.length)
                        aux = 0;
                    else{
                        aux = arr[i+j];
                    }
                    b |= (long) aux & (0xFFL);
                }

                byte[] codeBits = canonCodes.get(b);
                assert codeBits != null;
                for (byte codeBit : codeBits) {
                    bos2.writeBit(codeBit == 1);
                }
            }
            chunksSizes[id] = bos2.size();
            bos2.flush();
            fileChunks[id] = bos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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