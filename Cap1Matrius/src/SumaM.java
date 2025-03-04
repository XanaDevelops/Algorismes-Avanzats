import java.util.concurrent.Callable;

public class SumaM implements Callable<Void>, Comunicar{
    private final Main principal;
    private volatile boolean stop;

    public SumaM(Main p) {
        principal = p;

    }

    @Override
    public Void call() {
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

        return null;
    }
    @Override
    public  void comunicar(String s) {
       if (s.contentEquals("aturar")){
           aturar();
           System.out.println("SumaM aturat");
       }
    }

    private void aturar(){
        stop = true;
    }

}

