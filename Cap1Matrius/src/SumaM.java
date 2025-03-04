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
        for (int i = 0; ( i<data.getSizeTamN()) && (!stop); i++) {

           //generate two random matrices
            int n = data.getTamanyN(i);
            Matriu a = Matriu.generarMatriuRandom(n);
            Matriu b = Matriu.generarMatriuRandom(n);

            //System.out.println( a.sumar(b).toString());
            System.out.println("sumant n= " + n);
            time = System.nanoTime();
            a.sumar(b);
            if (!stop){
                time = System.nanoTime() - time;
                data.setTempsSuma(time);
                data.setMatriuN(n);
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

