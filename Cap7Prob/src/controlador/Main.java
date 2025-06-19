package controlador;

import model.ClassHSV;
import model.Dades;
import model.Solver;
import model.XarxaSolver;
import vista.Finestra;



public class Main implements Comunicar {
    private static Main instance;

    private Dades dades;
    private Finestra finestra;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        if(instance == null){
            instance = this;
        }else{
            return;
        }

        dades = new Dades();
        finestra = new Finestra();
        finestra.setVisible(true);

    }

    public Dades getDades(){
        return dades;
    }

    public Comunicar getFinestra(){
        return finestra;
    }

    public static Main getInstance(){
        return instance;
    }

    @Override
    public void comunicar(String msg) {
        System.err.println(msg);
    }


    @Override
    public void classificarHSV() {
        ClassHSV classHSV = new ClassHSV();
        classHSV.run();
        finestra.actualitzarFinestra();

    }

    public void classificarXarxa(){
        XarxaSolver xarxaSolver = new XarxaSolver();
    }

}
