package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author mascport
 */
public class ImgFile {

    public static double[] cargarGris(String f, int w, int h) {
        double[] res = new double[w * h];
        BufferedImage img = leerImagen(f);
        img = escalarImagen(img, w, h);
        res = nivelesGrisEntre0y1(img);
        return res;
    }

    public static double[] cargar9Colores(String f) {
        double[] res = null;
        BufferedImage img = leerImagen(f);
        res = niveles9ColoresEntre0y1(img);
        return res;
    }

    private static BufferedImage leerImagen(String nombreArchivo) {
        BufferedImage res = null;
        try {
            // Ruta relativa a la carpeta 'ims' en el directorio del proyecto
            File imagenArchivo = new File(nombreArchivo);
            // Cargar la imagen como BufferedImage
            res = ImageIO.read(imagenArchivo);
            if (res == null) {
                System.err.println("Error: No se pudo leer la imagen " + nombreArchivo);
            }
        } catch (Exception e) {
            System.err.println("Error al leer la imagen: " + e.getMessage());
            res = null;
        }
        return res;
    }

    // Método para escalar una imagen a un tamaño específico
    private static BufferedImage escalarImagen(BufferedImage imagenOriginal, int width, int height) {
        BufferedImage imagenEscalada = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenEscalada.createGraphics();
        // Dibujar la imagen original en la nueva imagen escalada
        g2d.drawImage(imagenOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
        g2d.dispose(); // Liberar recursos gráficos
        return imagenEscalada;
    }

    private static double[] nivelesGrisEntre0y1(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        double[] res = new double[w * h];
        int rgb, rojo, verde, azul;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb = img.getRGB(i, j);
                rojo = (rgb >> 16) & 0xFF;
                verde = (rgb >> 8) & 0xFF;
                azul = rgb & 0xFF;
                res[i + (j * w)] = ((rojo + verde + azul) / 3.0) / 255.0;   // valor entre 0.0y1.0 porcentaje de blanco
            }
        }
        return res;
    }

    private static double[] niveles9ColoresEntre0y1(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        double[] res = new double[9];
        for(int i=0;i<res.length;i++) {
            res[i] = 0.0;
        }
        int rgb, rojo, verde, azul, alpha;
        int color;
        int cont = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb = img.getRGB(i, j);
                rojo = (rgb >> 16) & 0xFF;
                verde = (rgb >> 8) & 0xFF;
                azul = rgb & 0xFF;
                alpha = (rgb >> 24) & 0xFF;
                if (alpha > 200) {
                    color = color9HSV(rojo, verde, azul);
                    res[color] = res[color] + 1;   // valor entre 0.0y1.0 porcentaje de blanco 
                    cont++;
                } else {
                    ;    // no tener en cuenta el pixel. Es transparente.
                }
            }
        }
        for(int i=0;i<res.length;i++) {
            res[i] = res[i] / cont;
        }
        return res;
    }
      
    private static int color9HSV(int r, int g, int b) {
        int res = -1;
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        // Convertimos HSB a formato más legible
        float h = hsb[0] * 360; // Hue en grados [0, 360]
        float s = hsb[1] * 100; // Saturación en porcentaje [0, 100]
        float bValue = hsb[2] * 100; // Brillo en porcentaje [0, 100]
        if ((bValue >=89) && (s <= 11)) { // es blanco
            res = 0;
        } else if (bValue <= 11) {  // es negro
            res = 8;
        } else {    // Es un color.
            if ((h >= 330) || (h< 30)) {   // rojo
                res = 1;
            } else if ((h >= 330) || (h< 30)) {   // rojo
                res = 1;
            } else if (h < 55) {     // marron-naranja
                res = 2;
            } else if (h < 80) {     // amarillo
                res = 3;
            } else if (h < 80) {     // amarillo
                res = 3;
            } else if (h < 165) {     // verde
                res = 4;
            } else if (h < 195) {     // cian
                res = 5;
            } else if (h < 285) {     // azul
                res = 6;
            } else {                  // magenta
                res = 7;
            }
        }
        return res;
    }
}
