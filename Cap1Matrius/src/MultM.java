import java.util.Random;

public class MultM extends Thread implements Comunicar{{}
    private final Main principal;

    public MultM(Main p) {
        System.out.println("Multi");
        principal = p;
        this.start();
    }

    @Override
    public void run() {
        int n = principal.getDades().getN();

        Random rand = new Random();

        Matriu a = new Matriu(n, n);
        Matriu b = new Matriu(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a.set(i, j, rand.nextInt(100000));
                b.set(i, j, rand.nextInt(100000));
            }
        }

        long start = System.currentTimeMillis();
        Matriu c = a.multiplicar(b);
        long end = System.currentTimeMillis();
        long time = (end - start);
        System.out.println("Multi time: " + time + " ms");
        principal.getDades().setTempsMult(time);
        principal.getDades().setTamanyN(n);
        principal.getFinestra().comunicar("pintar");
    }

    @Override
    public void comunicar(String s) {
        System.out.println(s);
    }
}
