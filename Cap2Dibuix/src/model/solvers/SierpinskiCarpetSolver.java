package model.solvers;
import model.Dades;
import model.Tipus;
import principal.Comunicar;
import principal.Main;

import java.util.Arrays;

/**
 * Classe que genera la fractal de "Carpeta de Sierpinski" de forma recursiva.
 * Implementa la interfície Runnable per permetre l'execució en un fil separat, i la interfície Comunicar
 * per comunicar-se amb el controlador (Main).
 */
public class SierpinskiCarpetSolver extends RecursiveSolver implements Comunicar {

  Main main;
  Dades data;

  private long time;

    /**
     * Valor numèric assignat als quadres de la catifa
     */
  private static int numActual;



    /**
     * Constructor de classe.
     * Inicialitza els atributs de classe, i omple el tauler de Dades.
     * @param main instància de la classe principal del programa
     * @param dades instància de Dades
     */
  public SierpinskiCarpetSolver(Main main, Dades dades) {
      this.main = main;
      this.data = dades;
      //Estableix el tipus de la fractal
      data.setTipus(Tipus.QUADRAT);
      numActual = 1;
      int [][] t = new int[data.getProfunditat()][data.getProfunditat()];
      for (int[] ints : t) {
          Arrays.fill(ints, 0);
      }
      data.setTauler(t);
  }

    /**
     * Dibuixa un quadrat de mida @size a partir de les coordenades (x, y) en el tauler de dades.

     * Aquest mètode recorre totes les coordenades dins d'un quadrat de mida `size x size`
     * i assigna el valor `numActual` a cada cel·la dins d'aquesta àrea. Després de dibuixar
     * el quadrat, el mètode fa una petita pausa per permetre la visualització de la modificació.
     *
     * @param x Coordenada X de la cantonada superior esquerra del quadrat en el tauler.
     * @param y Coordenada Y de la cantonada superior esquerra del quadrat en el tauler.
     * @param size La mida del costat del quadrat que s'ha de dibuixar.
     *
     */
    private void drawSquare(int x, int y, int size) {
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                data.setValor(i+x, j+y, numActual);
                main.comunicar("pintar");
                esperar(0, 150);
            }
        }


    }

    /**
     * Dibuixa la fractal de la catifa de Sierpinski en el tauler de dades.

     * Aquest mètode utilitza la recursió per dibuixar la fractal de la catifa de Sierpinski,
     * començant des de la coordenada (x, y) amb una mida `size`. La fractal es construeix
     * de manera iterativa, dividint el quadrat inicial en 9 subquadrats i recursivament
     * dibuixant en 8 d'aquests subquadrats (excloent el del centre).
     *
     * @param x Coordenada X de la cantonada superior esquerra del quadrat actual.
     * @param y Coordenada Y de la cantonada superior esquerra del quadrat actual.
     * @param size La mida del costat del quadrat actual.
     */
    private void drawSiperpinskiCarpet(int x, int y, int size) {
        if(!aturar){
            //Cae base: dibuixa un quadrat
            if (size==1) {
                drawSquare(x, y, size);
                numActual++;

                return;
            }

            for (int i = 0; i < 3 && !aturar; i++) {//posició y
                for (int j = 0; j < 3 && !aturar; j++) { // posició x
                    if (!(i == 1 && j == 1)){ //quadre no buit
                        //requisit final de exp. lambda
                        int finalI = i;
                        int finalJ = j;
                        //requisit final de exp. lambda
                        runThread(() -> drawSiperpinskiCarpet(x + (finalJ * size / 3), y + (finalI * size / 3), size / 3));
                    }
                }
            }
            
        }
    }

    /**
     * Executa el mètode drawSierpinskiCarpet i calcula el temps real d'execució de l'algorisme, així com
     * el temps esperat, utilitzant la constant multiplicativa.
     * Al programa principal, comunica els temps calculats, així com quan acaba d'executar el programa.
     */

    @Override
    public void run() {
        aturar = false;

        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(8, data.getProfunditat());
        main.comunicar("tempsEsperat:"+ tempsEsperat);//??

        time = System.nanoTime();
        runThread(() -> drawSiperpinskiCarpet(0, 0, data.getTauler().length));

        //main.comunicar("aturar");

    }
//
//    @Override
//    protected void end() {
//        System.err.println("he acabat");
//        System.err.println(getSleepTime());
//        time = (System.nanoTime() - time - getSleepTime())/1000000000;
//        System.out.println("Temps real " + time  + " segons");
//        main.comunicar("tempsReal:"+ time);
//
//        // Actualitza la constant multiplicativa basant-se en el temps real mesurat
//        data.setConstantMultiplicativa(time/Math.pow(3, data.getProfunditat() ));
//        if (!aturar) {
//            main.comunicar("aturar");
//            //aturar();
//        }
//    }
@Override
protected void end() {
    time = (System.nanoTime() - time - getSleepTime())/1000000000;
    double profunditatExp = Math.pow(8, data.getProfunditat()/2);
    double tempsEsperat;
    double constantMultiplicativa = time / profunditatExp;
    if (data.getTipus() == Tipus.QUADRAT && data.getConstantMultiplicativa()!=null) {
        //ja està inicialitzada
        tempsEsperat = data.getConstantMultiplicativa()*profunditatExp;
    }else{
        tempsEsperat = constantMultiplicativa*profunditatExp;

    }

    data.setConstantMultiplicativa(constantMultiplicativa);
    // Mostra els resultats
    System.out.printf("Temps esperat: %.8f segons%n", tempsEsperat);
    main.comunicar("tempsEsperat");

    System.out.printf("Temps real: %.8f segons%n", time);
    main.comunicar("tempsReal");
//        System.err.println("he acabat");
//        System.err.println(getSleepTime());
//        time = (System.nanoTime() - time - getSleepTime())/1000000000;
//        System.out.println("Temps real " + time  + " segons");
//        main.comunicar("tempsReal:"+ time);
//
//        // Actualitza la constant multiplicativa basant-se en el temps real mesurat
//        data.setConstantMultiplicativa(time/Math.pow(3, data.getProfunditat() ));
    if (!aturar) {
        main.comunicar("aturar");
        //aturar();
    }
}


    /**
     * Implementació del mètode comunicar de la interfície Comunicar.
     *
     * @param s el missatge rebut
     */
    @Override
    public void comunicar(String s) {
        if (s.contentEquals("aturar")) {
            aturar();

        }
    }

    /**
     * Mètode per aturar el fil d'execució.
     */
    private void aturar() {
        if(aturar)return;
        aturar = true;
        executor.shutdown();
    }
}
