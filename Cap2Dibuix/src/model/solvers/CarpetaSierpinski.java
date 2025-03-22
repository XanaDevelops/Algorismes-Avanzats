package model.solvers;
import model.Dades;
import model.Tipus;
import principal.Comunicar;
import principal.Main;

import java.util.Arrays;

public class CarpetaSierpinski implements Runnable, Comunicar {
  Main main;
  Dades data;
  private int size;
  private static int numActual;

  public CarpetaSierpinski(Main main, Dades dades) {
      this.main = main;
      this.data = dades;
      data.setTipus(Tipus.QUADRAT);
      this.size = (int)Math.pow(3, data.getProfunditat());
      numActual = 1;
      int [][] t = new int[size][size];
      for (int i = 0; i < t.length; i++) {
          Arrays.fill(t[i], 0);
      }
      data.setTauler(t);
  }

    private void drawSquare(int x, int y, int size) {
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                data.setValor(i+x, j+y, numActual);
            }
        }

//        numActual++;

    }
    private void drawSiperpinskiCarpet(int x, int y, int size) {

        if (size==1) {
            drawSquare(x, y, size);
            numActual++;
            return;
        }
            for (int i = 0; i < 3; i++) {//posició y
                for (int j = 0; j < 3; j++) { // posició x
                    if (!(i == 1 && j == 1)){ //quadre no buit
                        drawSiperpinskiCarpet(x + (j * size / 3), y+ (i * size / 3), size / 3);
                    }
                }
            }


    }
public void imprimir() {
     int [][] t = data.getTauler();
     for (int i = 0; i < t.length; i++) {
         for (int j = 0; j < t[i].length; j++) {
             System.out.print(t[i][j] + " ");
         }
         System.out.println();
     }
}
    @Override
    public void run() {
        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(3, data.getProfunditat());
        main.comunicar("tempsEsperat "+ tempsEsperat);//??
        long time = System.currentTimeMillis();
        drawSiperpinskiCarpet(0, 0, data.getTauler().length);

        main.comunicar("acabar");

        time = (System.currentTimeMillis() - time)/1000;
        System.out.println("Temps real " + time  + " segons");
        main.comunicar("tempsReal");

        //actualitzar la constant multiplicativa
        data.setConstantMultiplicativa(time/Math.pow(3, data.getProfunditat() ));


    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {
        switch (s){
            case "aturar":
                System.err.println("ATURAR carpeta sierpinski");
                break;
        }

    }
}
