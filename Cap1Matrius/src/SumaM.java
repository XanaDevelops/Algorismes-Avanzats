/**
 * Classe que realitza la suma de matrius en un fil d'execució.
 * Implementa la interfície Comunicar per enviar i rebre missatges.
 */

public class SumaM implements Runnable, Comunicar {

    /**
     * Instància de la classe principal (controlador.Main)
     */
    private final Main principal;
    /**
     * Variable booleana per poder aturar el fil d'execució.
     */
    private volatile boolean stop;


    /**
     * Constructor de classe
     *
     * @param p instància del programa principal
     */


    public SumaM(Main p) {
        principal = p;
    }

    @Override
    /*
     * Mètode principal que executa el fil.
     * Calcula, dins un bucle, la suma de dues matrius de mida incremental.
     */
    public void run() {
        stop = false;
        Dades data = principal.getDades();
        long time;

        //activem la barra de progrés de la suma de matrius
        dibuixConstantMult finestraCM = principal.getFinestraCM();
        principal.getFinestra().comunicar("activar:sumar");

        //Obtenim la mida introduïda de la matriu
        int n = data.getMatriuN();
        Double constantSuma;


        for (int i = 1; i <= Dades.N_STEP && !stop; i++) {
            //calculem la mida de la matriu en aquest pas
            int size = (int) (n / (Dades.N_STEP / (double) i));
            //generem dues matrius arbitràries

            Matriu a = Matriu.generarMatriuRandom(size);
            Matriu b = Matriu.generarMatriuRandom(size);

            time = System.nanoTime();
            a.sumar(b);
            if (!stop) {
                double tempsEsperat;
                time = System.nanoTime() - time;
                double doubleTime = time / 1000000.0;
                //La constant multiplicativa ja està inicialitzada?

                if (data.jaInicialitzada(data.getCSuma())) { //SÍ
                    constantSuma = data.getCSuma();
                    tempsEsperat = constantSuma * size * size;

                    //afegim la mida de la matriu, el temps real, i el temps esperat a la taula corresponent
                    finestraCM.afegirFilaSuma(size, String.format("%.2f", doubleTime), String.format("%.2f", tempsEsperat), String.valueOf(constantSuma));

                } else {//NO
                    //La calculem

                    constantSuma = (doubleTime) / (size * size);
                    data.setCSuma(constantSuma);
                    finestraCM.afegirFilaSuma(size, String.format("%.2f", doubleTime), "-", String.valueOf(constantSuma));


                }

                //Desem el resultat de la suma
                data.addResultatSumar(time, size);

                //Comuniquem a la interfície per pintar la pantalla
                principal.comunicar("pintar");
            }
        }

        //prevenir tornar a aturar
        if(!stop)
            aturar();

    }

    /**
     * Implementació del mètode comunicar de la interfície Comunicar
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
     * Mètode per aturar el fil d'execució, i la barra de progrés corresponent.
     */
    private void aturar() {
        stop = true;
        principal.getFinestra().comunicar("desactivar:sumar");

    }

}

