package vista;


import controlador.Comunicar;
import controlador.Main;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import model.Dades;
import model.punts.Punt;
import model.punts.Punt2D;
import model.punts.Punt3D;

import java.awt.*;
import java.util.List;

public class Eixos3D extends JFXPanel implements Comunicar {
    private final Dades dades;

    private final Material puntBlau = new PhongMaterial(Color.BLUE);

    private Punt3D originPoints;

    private Group root, puntGroup;

    private Rotate camRX = new Rotate(0, Rotate.X_AXIS);
    private Rotate camRY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate camRZ = new Rotate(0, Rotate.Z_AXIS);
    private Translate camTrans = new Translate(0,0,0);
    private double cameraDistance = -100;

    private Camera camera = new PerspectiveCamera(true);

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;

    //això impideix que JAvaFX s'aturi
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
        buildCamera();
        scene.setCamera(camera);

        addMouseControls(scene);
        root.getChildren().addAll(puntGroup);

        return scene;
    }

    private void buildCamera() {
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.setTranslateZ(cameraDistance);
        camera.getTransforms().addAll(camRX, camRY, camRZ, camTrans);
    }

    private void plotPunts(){
        puntGroup.getChildren().clear();

        List<Punt> punts =  dades.getPunts();
        if (!(punts.getFirst() instanceof Punt3D)) {
            return;
        }
        System.err.println("Lista de Punts: " + punts.size());

        originPoints = new Punt3D((int) (getWidth()/2d), (int) (getHeight()/2d), 0);
        System.err.println(getWidth());
        //plotPunts();
        //puntGroup.setTranslateX(originPoints.getX());
        //puntGroup.setTranslateY(originPoints.getY());
        //puntGroup.setTranslateZ(originPoints.getZ());

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

    private void addMouseControls(Scene subScene) {
        subScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });

        subScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                double deltaX = mousePosX - mouseOldX;
                double deltaY = mousePosY - mouseOldY;

                if (me.getButton() == MouseButton.PRIMARY) {
                    // Rotacion
                    camRY.setAngle(camRY.getAngle() + deltaX * 0.2);
                    camRX.setAngle(camRX.getAngle() - deltaY * 0.2);
                } else if (me.getButton() == MouseButton.SECONDARY) {
                    // Traslacion
                    camTrans.setX(camTrans.getX() + deltaX);
                    camTrans.setY(camTrans.getY() + deltaY);
                }
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                System.err.println(camRX.getAngle());
            }
        });

        subScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                cameraDistance += event.getDeltaY() * 0.5;
                camera.setTranslateZ(cameraDistance);
            }
        });
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
