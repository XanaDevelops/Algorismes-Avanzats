/**
 *Classe que realitza la multiplicació de matrius en un fil d'execució.
 * Implementa la interfície Comunicar per enviar i rebre missatges.
 **/
public class MultM extends Thread implements Comunicar{
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
    public MultM(Main p) {
        principal = p;
    }
    /**
     * Mètode principal que executa el fil.
     * Calcula, dins un bucle, la multiplicació de dues matrius de mida incremental.
     */
    public void run() {
        stop = false;
        Dades data = principal.getDades();
        long time;

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
            time = System.nanoTime();
            a.multiplicar(b);

            if (!stop){
                time = System.nanoTime() - time;
                if (data.jaInicialitzada(data.getCMult())){
                    constantMult = data.getCSuma();
                    System.out.println("Per a la multiplicació de matrius de n = "+ size + " tardaré " + constantMult*(Math.pow(size, 3)));

                }else{
                    constantMult= (1.0*time)/(Math.pow(size, 3));
                    data.setCMult(constantMult);
                }
                System.out.println("Per a la multiplicació de matrius de n = " + size + " he tardat "+ time);

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
        }
    }
    /**
     * Mètode per aturar el fil d'execució
     */
    private void aturar(){
        stop = true;
    }
}
