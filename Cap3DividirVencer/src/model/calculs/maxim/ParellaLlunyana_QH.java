package model.calculs.maxim;

import javafx.geometry.Point3D;
import model.Resultat;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.calculs.maxim.quickhull3d.Point3d;
import model.calculs.maxim.quickhull3d.QuickHull3D;
import model.punts.*;

import java.util.*;
import java.util.stream.Collectors;

public class ParellaLlunyana_QH extends Calcul {



    public ParellaLlunyana_QH() {
        super();

        dades.setTipusCalcul(TipusCalcul.CH_MAX);


    }

    @Override
    public void run(){

    }

    protected Resultat calcular(){
        System.err.println("3D");
        List<Point3d> points = new ArrayList<>();
        for (Punt p : this.punts){
            points.add(new Point3d(p.getX(), p.getY(), p.getZ()));
        }

        QuickHull3D qh = new QuickHull3D(points.toArray(new Point3d[0]));
        Point3d[] res = qh.getVertices();
        List<Punt> res3d = new ArrayList<>();
        for (Point3d p: res){
            res3d.add(new Punt3D((int) p.x, (int) p.y, (int) p.z));
        }

        return ParellaLlunyana_fb.calc(res3d);
    }


}
