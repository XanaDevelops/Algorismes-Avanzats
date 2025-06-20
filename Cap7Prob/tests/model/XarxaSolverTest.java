package model;

import controlador.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XarxaSolverTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Main.main(null);
    }

    @Test
    public void loadImatges(){
        new XarxaSolver();
    }

    @Test
    public void train(){
        XarxaSolver solver = new XarxaSolver();
        File xFile = new File("res/xarxa.bin");
        Xarxa xarxa;
        if(!xFile.exists() || true){
            solver.run();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(xFile))){
            xarxa = (Xarxa) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        double[][] entrades = solver.getEntradas();
        double[][] sortides = solver.getSortides();
        for (int i = 0; i < entrades.length; i++) {
            double[] res = xarxa.predecir(entrades[i]);
            System.out.println("Entrada: " + i);
            System.out.println("Sortida: " + Arrays.toString(res));
            System.out.println("Esperat: " + Arrays.toString(sortides[i]));
        }
        System.out.println("\n\n");
        File carpeta = new File(Dades.PATH_IMATGES);
        File[] fotos = carpeta.listFiles((dir, name) -> !name.contains("test"));
        System.out.println(Arrays.toString(Paisatge.values()));
        for(File f : fotos){
            try {
                double[] entrada = solver.generarEntrada(f);

                double[] sortida = xarxa.predecir(entrada);

                System.out.println("Entrada: " + f);
                //System.out.println(Arrays.toString(entrada));
                System.out.println("Sortida: " + Arrays.toString(sortida));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}