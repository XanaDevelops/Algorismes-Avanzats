package vista;


import controlador.Comunicar;
import controlador.Main;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import model.Dades;
import model.punts.Punt;
import model.punts.Punt3D;

import java.awt.*;
import java.util.List;

public class Eixos3D extends JFXPanel implements Comunicar {
    private final Dades dades;

    private final Material puntBlau = new PhongMaterial(Color.BLUE);

    private Punt3D originPoints;

    private Group root, puntGroup;



    //això impideix que JAvaFX s'aturi o algo
    static {
        Platform.setImplicitExit(false);
    }

    public Eixos3D() {
        super();
        dades = Main.instance.getDades();
        Platform.runLater(this::initFX);
    }

    public void initFX(){
        Scene scene = createScene();
        this.setScene(scene);
    }

    private Scene createScene(){
        root = new Group();
        puntGroup = new Group();
        Scene scene = new Scene(root, getWidth(), getHeight(), Color.BLACK);


        root.getChildren().addAll(puntGroup);

        return scene;
    }

    private void plotPunts(){
        List<Punt> punts =  dades.getPunts();
        System.err.println("Lista de Punts: " + punts.size());
        puntGroup.getChildren().clear();

        originPoints = new Punt3D((int) (getWidth()/2d), (int) (getHeight()/2d), 0);
        System.err.println(getWidth());
        //plotPunts();
        puntGroup.setTranslateX(originPoints.getX());
        puntGroup.setTranslateY(originPoints.getY());
        puntGroup.setTranslateZ(originPoints.getZ());

        for(Punt p : punts) {
            Punt3D punt = (Punt3D) p;
            Sphere puntS = new Sphere(5);
            puntS.setMaterial(puntBlau);
            puntS.setTranslateX(punt.x);
            puntS.setTranslateY(punt.y);
            puntS.setTranslateZ(punt.z);

            puntGroup.getChildren().add(puntS);
        }
    }

    @Override
    public void comunicar(String s) {
        switch(s){
            case "dibuixPunts":
            case "pintar":
                Platform.runLater(this::plotPunts);
                break;
            case "aturar":
                break;
        }
    }
}
