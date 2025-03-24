package model.solvers;

import model.Dades;
import model.Tipus;
import principal.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TreeSolver extends RecursiveSolver implements Comunicar {
    private BufferedImage image;
    private Dades data;
    private Main main;
    Graphics2D g;

    private final int imgSize = 1024;
    private final int treeLogSize = 35;
    private final int treeLogAngle = 20;
    private long time;

    public TreeSolver(Main main, Dades data) {
        this.main = main;
        this.data = data;

        data.setTipus(Tipus.TREE);


        image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        this.data.setImage(image);

        g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imgSize, imgSize);

    }

    private void generarArbol(Graphics2D g, int x1, int y1, double angle, int p) {
       if(!aturar) {
           if (p == 0)
           {

               return;
           }

           int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * treeLogSize );
           int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * treeLogSize);
           g.setColor(data.getColor(p%data.getColors().length));

           g.drawLine(x1, y1, x2, y2);
           main.comunicar("pintar");
           esperar(0, 300);
           runThread(() -> generarArbol(g, x2, y2, angle - treeLogAngle, p - 1));
           runThread(() -> generarArbol(g, x2, y2, angle + treeLogAngle, p - 1));
       }

    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void run() {
        aturar = false;
        //suposant aqui, q
        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(2,data.getProfunditat());
        main.comunicar("tempsEsperat:"+ tempsEsperat);//??

        time = System.nanoTime();
        generarArbol(g, image.getHeight()/2, image.getHeight()-300, -90, data.getProfunditat());
    }

//    @Override
//    protected void end() {
//        time = System.nanoTime() - time - getSleepTime();
//        System.out.println("Temps real " + time + " nanosegons");
//        main.comunicar("tempsReal:"+ time);
//        g.dispose();
//        if (!aturar) {
//            main.comunicar("aturar");
//        }
//    }
@Override
protected void end() {
    long elapsedTime = System.nanoTime() - time;
    // Converteix a segons (double)
    double tempsReal = elapsedTime / 1_000_000_000.0;

    // Calcula la constant multiplicativa
    System.out.println(data.getProfunditat());
    double profunditatExp = Math.pow(2, data.getProfunditat());
    double constantMultiplicativa = tempsReal / profunditatExp;
    double tempsEsperat;
    if (data.getTipus() == Tipus.TREE && data.getConstantMultiplicativa()!=null) {
        //ja està inicialitzada
        tempsEsperat = data.getConstantMultiplicativa()*profunditatExp;
    }else{
        tempsEsperat = constantMultiplicativa*profunditatExp;

    }

    data.setConstantMultiplicativa(constantMultiplicativa);
    // Mostra els resultats
    main.comunicar("tempsEsperat:"+ String.format( "%.3f segons",tempsEsperat));

    main.comunicar("tempsReal:"+ String.format( "%.3f segons",tempsReal));
    g.dispose();
    if (!aturar) {
        main.comunicar("aturar");
    }
}


    @Override
    public void comunicar(String s) {
        switch (s){
            case "aturar":
                aturar();
                break;
        }

    }
    private void aturar() {
        if(aturar)return;
        aturar = true;
        executor.shutdown();
    }




}
