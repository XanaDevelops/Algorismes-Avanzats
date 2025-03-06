import java.util.concurrent.Callable;

public class SumaM implements Runnable, Comunicar{
/**
 *Classe que realitza la suma de matrius en un fil d'execució.
 * Implementa la interfície Comunicar per enviar i rebre missatges.
 **/
    /**
     * Instància de la classe principal (Main)
     */
    private final Main principal;
    private volatile boolean stop;
    /**
     * Variable booleana per poder aturar el fil d'execució.
     */


    /**
     * Constructor de classe
     * @param p instància del programa principal
     */


    public SumaM(Main p) {
        principal = p;
    }

    @Override
    /**
     * Mètode principal que executa el fil.
     * Calcula, dins un bucle, la suma de dues matrius de mida incremental.
     */
    public void run() {
        stop = false;
        Dades data = principal.getDades();
        long time;

        dibuixConstantMult finestraCM = principal.getFinestraCM();
       //Obtenim la mida introduïda de la matriu
        //generate two random matrices
        int n = data.getMatriuN();
        Double constantSuma;


        for (int i = 1; i <= Dades.N_STEP  && !stop; i++) { //NSTEP o n?
            //calculem la mida de la matriu en aquest pas
            int size = (int)(n / (Dades.N_STEP / (double)i));

            Matriu a = Matriu.generarMatriuRandom(size);
            Matriu b = Matriu.generarMatriuRandom(size);

            System.out.println("sumant n= " + size);
            time = System.nanoTime();
            a.sumar(b);
            if (!stop){
                double tempsEsperat;
                time = System.nanoTime() - time;
                double doubleTime = time/ 1000000.0;
                if (data.jaInicialitzada(data.getCSuma())){
                    constantSuma = data.getCSuma();
//                    System.out.println("CONSTANT DE SUMA  = "+constantSuma);
                    tempsEsperat = constantSuma*size*size;

//                    System.out.println("Per la suma de matrius de n = "+ size + " tardaré " + String.format("%.2f", tempsEsperat) + " milisegons");
                    finestraCM.afegirFilaSuma(size, String.format("%.2f",doubleTime), String.format("%.2f",tempsEsperat));

                }else{
                    finestraCM.afegirFilaSuma(size, String.format("%.2f",doubleTime), "-");

                    constantSuma= (doubleTime)/(size*size);
                    data.setCSuma(constantSuma);


                }
//                System.out.println("Per a la suma de matrius de n = " + size + String.format(" he tardat %.2f", doubleTime  ) + " milisegons");

                //Desem el resultat de la suma
                data.addResultatSumar(time, size);

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
    public  void comunicar(String s) {
       if (s.contentEquals("aturar")){
           aturar();
           System.out.println("SumaM aturat");
       }
    }

    /**
     * Mètode per aturar el fil d'execució
     */
    private void aturar(){
        stop = true;
    }

}

