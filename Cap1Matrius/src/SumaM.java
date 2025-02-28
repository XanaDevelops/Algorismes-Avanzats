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
        for (int i = 0;( i<data.getTamN()) && (!stop); i++) {

            time = System.nanoTime();
           //generate two random matrices
            int n = data.getTamanyN(i);
            Matriu ma1 = new Matriu(n, n);
            Matriu ma2 = new Matriu(n, n);
            ma1 = ma1.generarMatriu(n);
            ma2 = ma2.generarMatriu(n);
            System.out.println( ma1.sumar(ma2).toString());
            if (!stop){
                time = System.nanoTime() - time;
                data.setTempsSuma(time);
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

