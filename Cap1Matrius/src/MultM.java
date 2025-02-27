public class MultM extends Thread implements Comunicar{{}
    private final Main principal;

    public MultM(Main p) {
        System.out.println("Multi");
        principal = p;
    }

    @Override
    public void comunicar(String s) {
        System.out.println(s);
    }
}
