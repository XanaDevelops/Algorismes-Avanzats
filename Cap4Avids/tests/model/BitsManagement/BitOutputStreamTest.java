package model.BitsManagement;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class BitOutputStreamTest {

    String file = "tests/res/test.bin";

    @Test
    public void test() throws IOException {
        int N = 10;
        ByteArrayOutputStream[] bos = new ByteArrayOutputStream[N];
        int[] bitsSizes = new int[N];
        for (int i = 0; i < bos.length; i++) {
            try(ByteArrayOutputStream b = new ByteArrayOutputStream();
            BitOutputStream bo = new BitOutputStream(b)) {
                for (int j = 0; j < (i+1)*7; j++) {
                    bo.writeBit(true);
                }
                System.out.println(bo.size());
                bitsSizes[i] = bo.size();
                bo.flush();
                bos[i] = b;
            }
        }
        try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))){
            int acum = 0;
            int i = 0;
            for (ByteArrayOutputStream bo : bos) {
                bo.writeTo(out);
                System.out.println("N: " + bo.size() + " acum: " + acum + " bits: " + bitsSizes[i] + "?: " + (bitsSizes[i++] % 8));
                acum += bo.size();
            }
        }


        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))){
            byte[] file = in.readAllBytes();
            int lastByte = 0;
            for (int i = 0; i < bitsSizes.length; i++) {
                try(ByteArrayInputStream b = new ByteArrayInputStream(file);
                BitInputStream bi = new BitInputStream(b)) {
                    System.out.println("lastByte: " + lastByte + " bits: " + bitsSizes[i]);
                    b.skip(lastByte);
                    int read = 0;
                    while(read < bitsSizes[i]){
                        assertTrue(bi.readBit());
                        read++;
                    }
                }
                lastByte += (bitsSizes[i]>>3) + ((bitsSizes[i] % 8) == 0 ? 0 : 1);
            }
        }
    }

}