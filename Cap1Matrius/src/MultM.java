import java.util.Random;

public class MultM extends Thread implements Comunicar{{}
    private final Main principal;
    private boolean stop;
    private Random rand;
    public MultM(Main p) {
        principal = p;
        rand = new Random();
    }

    public void run() {
        stop = false;

        Dades data = principal.getDades();
        long time;
        for (int i = 0;( i<data.getTamN()) && (!stop); i++) {

            time = System.nanoTime();
            //generate two random matrices
            int n = data.getTamanyN(i);
            Matriu a = new Matriu(n, n);
            Matriu b = new Matriu(n, n);
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    a.set(j, k, rand.nextInt(100000));
                    b.set(j, k, rand.nextInt(100000));
                }
            }
            System.out.println(a.multiplicar(b));
            if (!stop){
                time = System.nanoTime() - time;
                data.setTempsMult(time);
                data.setN(n);
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
