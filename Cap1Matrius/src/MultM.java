public class MultM implements Runnable, Comunicar {
/**
 *Classe que realitza la multiplicació de matrius en un fil d'execució.
 * Implementa la interfície Comunicar per enviar i rebre missatges.
 **/
    /**
     * Instància de la classe principal (Main)
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
    public MultM(Main p) {
        principal = p;
    }

    @Override
    /**
     * Mètode principal que executa el fil.
     * Calcula, dins un bucle, la multiplicació de dues matrius de mida incremental.
     */
    public void run() {
        stop = false;
        Dades data = principal.getDades();
        long time;

        dibuixConstantMult finestraCM = principal.getFinestraCM();
        //activem la barra de progrés de multiplicació
        principal.getFinestra().comunicar("activar:multiplicar");


        //Obtenim la mida introduïda de la matriu
        int n = data.getMatriuN();
        Double constantMult;

        for (int i = 1; i <= Dades.N_STEP && !stop; i++) {
            //calculem la mida de la matriu en aquest pas

            int size = (int) (n / (Dades.N_STEP / (double) i));
            //generem dues matrius arbitràries
            Matriu a = Matriu.generarMatriuRandom(size);
            Matriu b = Matriu.generarMatriuRandom(size);


            time = System.nanoTime();
            a.multiplicar(b);

            if (!stop) {
                double tempsEsperat;
                //temps total de l'operació de multiplicació de matrius
                time = System.nanoTime() - time;

                double doubleTime = time / 1000000.0;
                //La constant multiplicativa ja està inicialitzada?
                if (data.jaInicialitzada(data.getCMult())) { //SÍ
                    constantMult = data.getCMult();
                    tempsEsperat = constantMult * Math.pow(size, 3);

                    //afegim la mida de la matriu, el temps real, i el temps esperat a la taula corresponent
                    finestraCM.afegirFilaMutl(size, String.format("%.2f", doubleTime), String.format("%.2f", tempsEsperat));

                } else {//NO
                    //La calculem
                    constantMult = doubleTime / (Math.pow(size, 3));
                    data.setCMult(constantMult);
                    finestraCM.afegirFilaMutl(size, String.format("%.2f", doubleTime), "-");
                }

                //Desem el resultat de la multiplicació
                data.addResultatMultiplicar(time, size);
                //Comunicquem a la interfície per pintar la pantalla
                principal.comunicar("pintar");
            }

        }

        //prevenir tornar a aturar
        if(!stop)
            aturar();

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
     * Mètode per aturar el fil d'execució, i la barra de progrés corresponent.
     */
    private void aturar() {
        stop = true;
        principal.getFinestra().comunicar("desactivar:multiplicar");

    }
}
