/**
 *Classe que realitza la suma de matrius en un fil d'execució.
 * Implementa la interfície Comunicar per enviar i rebre missatges.
 **/
public class SumaM extends Thread implements Comunicar{
    /**
     * Instància de la classe principal (Main)
     */
    private final Main principal;
    /**
     * Variable booleana per poder aturar el fil d'execució.
     */
    private boolean stop;

    /**
     * Constructor de classe
     * @param p instància del programa principal
     */
    public SumaM(Main p) {
        principal = p;
    }

    /**
     * Mètode principal que executa el fil.
     * Calcula, dins un bucle, la suma de dues matrius de mida incremental.
     */
    public void run() {
        stop = false;
        Dades data = principal.getDades();
        long time;

       //Obtenim la mida introduïda de la matriu
        int n = data.getMatriuN();
        Double constantSuma;


        for (int i = 1; i <= Dades.N_STEP && !stop; i++) {
            //calculem la mida de la matriu en aquest pas
            int size = (int)(n / (Dades.N_STEP / (double)i));

            Matriu a = Matriu.generarMatriuRandom(size);
            Matriu b = Matriu.generarMatriuRandom(size);

            System.out.println("sumant n= " + size);
            time = System.nanoTime();
            a.sumar(b);
            if (!stop){
                time = System.nanoTime() - time;
                if (data.jaInicialitzada(data.getCSuma())){
                    constantSuma = data.getCSuma();
                    System.out.println("CONSTANT DE SUMA  = "+constantSuma);
                    System.out.println("Per la suma de matrius de n = "+ size + " tardaré " + constantSuma*size*size);

                }else{
                    constantSuma= (1.0*time)/(size*size);
                    System.out.println("CONSTANT DE SUMA  = "+constantSuma);
                    data.setCSuma(constantSuma);

                }
                System.out.println("Per a la suma de matrius de n = " + size + " he tardat "+ time);
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
       }
    }

    /**
     * Mètode per aturar el fil d'execució
     */
    private void aturar(){
        stop = true;
    }

}

