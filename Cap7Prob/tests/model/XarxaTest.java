package model;

import controlador.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XarxaTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Main.main(new String[0]);
    }

    @org.junit.jupiter.api.Test
    void predecir() {
        Xarxa x = new Xarxa(1, new int[]{3,3}, 1);
        System.out.println(Arrays.toString(x.predecir(new double[]{1.0})));
    }

    @Test
    void entrenar(){
        Xarxa x = new Xarxa(1, new int[]{3,3}, 1);
        x.entrenar(new double[][]{{1.0}, {0.5}}, new double[][]{{0.7}, {0.4}}, 1);
    }

    @Test
    void banderes(){
        int epocas = 1000000;
        int numEntradas = 9; // Píxeles
        int numNeuronasOcultas = 16; // Capa oculta con 16 neuronas
        int numSalidas = 1; // Salida entre [0.0,1.0] como % de parecido
        System.out.println("\n\n");
        System.out.println("******************************************************");
        System.out.println(" Vamos a probar de detectar el porcentaje  de color  *");
        System.out.println("******************************************************");
        Xarxa po1 = new Xarxa(numEntradas, new int[]{4,4}, numSalidas);
        // Suponiendo que ya tienes los vectores de las imágenes normalizados [0.0, 1.0]
        File carpeta = new File("testRes/banderes/flags/");
        File[] archivosPNG = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        double[][] entradas = new double[archivosPNG.length][numEntradas];
        double[][] salidasEsperadas = new double[archivosPNG.length][1]; // Imagen elegida es 100% similar, el resto es 0% similar
        if (archivosPNG.length > 0) {
            for (int i = 0; i < archivosPNG.length; i++) {
                entradas[i] = ImgFile.cargar9Colores(carpeta + "/" + archivosPNG[i].getName());
                if (archivosPNG[i].getName().contentEquals("es.png")) {
                    salidasEsperadas[i][0] = 1.0;  // a la española se le asigna un 100%
                } else {
                    salidasEsperadas[i][0] = 0.0;  // al resto un 0%
                }
            }
        }
        // Prueba con una nueva imagen
        double prediccion = po1.predecir(ImgFile.cargar9Colores(carpeta + "/es.png"))[0];
        System.out.println("Parecido antes de entrenar bandera ES: " + prediccion);
        prediccion = po1.predecir(ImgFile.cargar9Colores(carpeta + "/gb.png"))[0];
        System.out.println("Parecido antes de entrenar bandera GB: " + prediccion);
        // Entrenar con epocas iteraciones si no está ya calculado
        File archivo = new File("res/xarxa.bin");
        if (!archivo.exists()) {
            po1.entrenar(entradas, salidasEsperadas, epocas);
        } else {
            System.out.println("LEYENDO GUARDADO");
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                po1 = (Xarxa) (ois.readObject()); // Devuelve el objeto leído
            } catch (Exception e) {
                po1.entrenar(entradas, salidasEsperadas, epocas);
            }
        }
        // Prueba con red neuronal inteligente
        System.out.println("\nPredicción de parecido con la bandera española después de entrenar");
        System.out.println("    (Solo se muestran las que dan más de un 10% de parecido)");
        if (archivosPNG.length > 0) {
            for (int i = 0; i < archivosPNG.length; i++) {
                prediccion = po1.predecir(ImgFile.cargar9Colores(carpeta + "/" + archivosPNG[i].getName()))[0];
                if (prediccion >= 0.01) {
                    System.out.println("Parecido " + archivosPNG[i].getName() + ": " + prediccion);
                }
            }
        }
        // Pruebas con objetos
        carpeta = new File("testRes/banderes/objectes/");
        archivosPNG = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        System.out.println("\nPredicción de parecido de objetos con la bandera española después de entrenar");
        if (archivosPNG != null && archivosPNG.length > 0) {
            for (int i = 0; i < archivosPNG.length; i++) {
                prediccion = po1.predecir(ImgFile.cargar9Colores(carpeta + "/" + archivosPNG[i].getName()))[0];
                System.out.println("Parecido " + archivosPNG[i].getName() + ": " + prediccion);
            }
        }
    }

}