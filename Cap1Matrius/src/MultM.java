public class MultM implements Runnable, Comunicar{
    private final Main principal;
    private volatile  boolean stop;

    public MultM(Main p) {
        principal = p;
    }

    @Override
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

            //System.out.println(a.multiplicar(b));
            System.out.println("multiplicant n=" + size);
            time = System.nanoTime();
            a.multiplicar(b);

            if (!stop){
                time = System.nanoTime() - time;
                data.addResultatMultiplicar(time, size);
                principal.comunicar("pintar");
            }
        }

    }

    @Override
    public void comunicar(String s) {
        if (s.contentEquals("aturar")){
            aturar();
            System.out.println("MultM aturat");

        }
    }

    private void aturar(){
        stop = true;
    }
}
