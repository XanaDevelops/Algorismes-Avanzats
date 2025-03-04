public class SumaM extends Thread implements Comunicar{
    private final Main principal;
    private boolean stop;

    public SumaM(Main p) {
        principal = p;

    }

    public void run() {
        stop = false;

        Dades data = principal.getDades();
        long time;

       //generate two random matrices
        int n = data.getMatriuN();

        for (int i = 1; i <= Dades.N_STEP && !stop; i++) {

            int size = (int)(n / (Dades.N_STEP / (double)i));

            Matriu a = Matriu.generarMatriuRandom(size);
            Matriu b = Matriu.generarMatriuRandom(size);

            //System.out.println( a.sumar(b).toString());
            System.out.println("sumant n= " + size);
            time = System.nanoTime();
            a.sumar(b);
            if (!stop){
                time = System.nanoTime() - time;
                data.addResultatSumar(time, size);
                principal.comunicar("pintar");
            }
        }



    }
    @Override
    public  void comunicar(String s) {
       if (s.contentEquals("aturar")){
           aturar();
       }
    }

    private void aturar(){
        stop = true;
    }

}

