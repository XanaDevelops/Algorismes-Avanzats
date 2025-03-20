package model.solvers;
import model.Dades;
import model.Tipus;
import principal.Main;
public class CarpetaSierpinski implements Runnable {
  Main main;
  Dades data;
  private int size;
  private static int numActual;

  public CarpetaSierpinski(Main main) {
      this.main = main;
      this.data = main.getDades();
      data.setTipus(Tipus.QUADRAT);
      this.size = (int)Math.pow(3, data.getProfunditat());
      numActual = 1;
  }

    private void drawSquare(int x, int y, int size) {
        for (int i = size; i <2*size ; i++) {
            for (int j = size; j <2*size ; j++) {
                data.setValor(x+i, y+j, numActual);
            }
        }

        numActual++;


    }
    private void drawSiperpinskiCarpet(int x, int y, int size, int p) {
        if (p==0) {
            drawSquare(x, y, size);
        }else{
            for (int i = 0; i < 3; i++) {//posició y
                for (int j = 0; j < 3; j++) { // posició x
                    if (!(i == 1 && j == 1)){ //quadre no buit
                        drawSiperpinskiCarpet(j + size/3, x + size/3,  size/3, p-1);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(3, data.getProfunditat());

        main.comunicar("tempsEsperat");//??
        long time = System.currentTimeMillis();
        drawSiperpinskiCarpet(0, 0, Dades.HEIGHT/3, data.getProfunditat());

        main.comunicar("acabar");

        time = (System.currentTimeMillis() - time)/1000;
        System.out.println("Temps real " + time  + " segons");
        main.comunicar("tempsReal");

        //actualitzar la constant multiplicativa
        data.setConstantMultiplicativa(time/Math.pow(3, data.getProfunditat() ));


    }
}
