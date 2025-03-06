public class MultM implements Runnable, Comunicar{
/**
 *Classe que realitza la multiplicació de matrius en un fil d'execució.
 * Implementa la interfície Comunicar per enviar i rebre missatges.
 **/
    /**
     * Instància de la classe principal (Main)
     */
    private final Main principal;
    private volatile  boolean stop;

    /**
     * Variable booleana per poder aturar el fil d'execució.
     */
    /**
     * Constructor de classe
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

        //Obtenim la mida introduïda de la matriu
        int n = data.getMatriuN();
        Double constantMult;

        System.out.println("TAMANY N "+ n);
        for (int i = 1; i <= Dades.N_STEP && !stop; i++) {
            //calculem la mida de la matriu en aquest pas

            int size = (int)(n / (Dades.N_STEP / (double)i));

            Matriu a = Matriu.generarMatriuRandom(size);
            Matriu b = Matriu.generarMatriuRandom(size);

            //System.out.println(a.multiplicar(b));
            System.out.println("multiplicant n=" + size);


//            time = System.nanoTime();
            time = System.currentTimeMillis();
            a.multiplicar(b);

            if (!stop){
                double tempsEsperat;
                time = System.nanoTime() - time;
                double doubleTime = time/ 1000000.0;
                if (data.jaInicialitzada(data.getCMult())){
                    constantMult = data.getCMult();
                    tempsEsperat = constantMult* Math.pow(size, 3);
                    System.out.println("constant "+ constantMult);

                    System.out.println("Per a la multiplicació de matrius de n = "+ size + " tardaré " +  String.format("%.2f", tempsEsperat)  + " milisegons");
                    finestraCM.afegirFilaMutl(size, String.format("%.2f",doubleTime), String.format("%.2f",tempsEsperat));

                }else{
                    constantMult= doubleTime/(Math.pow(size, 3));
                    data.setCMult(constantMult);
                    finestraCM.afegirFilaMutl(size, String.format("%.2f",doubleTime), "-");
                }
                System.out.println("Per a la multiplicació de matrius de n = " + size + String.format(" he tardat %.2f", doubleTime  ) + " milisegons");

                //Desem el resultat de la multiplicació
                data.addResultatMultiplicar(time, size);
                //Comunicar a la interfície per pintar la pantalla

                principal.comunicar("pintar");
            }

        }

    }
    /**
     * Implementació del mètode comunicar de la interfície Comunicar
     * @param s el missatge rebut
     */
    @Override
    public void comunicar(String s) {
        if (s.contentEquals("aturar")){


            aturar();
            System.out.println("MultM aturat");

        }
    }
    /**
     * Mètode per aturar el fil d'execució
     */
    private void aturar(){
        stop = true;
    }
}
