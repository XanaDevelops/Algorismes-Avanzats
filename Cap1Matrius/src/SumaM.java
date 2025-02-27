public class SumaM extends Thread implements Comunicar{
    private final Main principal;

    public SumaM(Main p) {
        principal = p;
    }

    @Override
    public void comunicar(String s) {
        System.out.println(s);
    }
}

