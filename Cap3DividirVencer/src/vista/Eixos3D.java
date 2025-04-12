package vista;


import com.sun.glass.events.WheelEvent;
import com.sun.javafx.geom.transform.Translate2D;
import controlador.Comunicar;
import controlador.Main;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
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
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
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

    private Translate originPoints;

    private Group root, puntGroup;

    private Camera camera;

    private final Rotate[] puntsRotate = {new Rotate(0, Rotate.Z_AXIS),
                                    new Rotate(0, Rotate.Y_AXIS),
                                    new Rotate(0, Rotate.X_AXIS),};
    private final double[] puntsRotVel = {0,0,0};

    private final Translate camTranslate = new Translate(0,0,-750);
    private final double[] camVel = {0,0,0};

    private Point2D lastMouse = new Point2D(0,0);

    //això impedeix que JAvaFX s'aturi tot sol
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
        puntGroup.getTransforms().addAll(puntsRotate[0], puntsRotate[1], puntsRotate[2]);


        Scene scene = new Scene(root, getWidth(), getHeight(),true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        camera = createCamera();
        scene.setCamera(camera);

        addMouseControls(scene);
        root.getChildren().addAll(puntGroup, new AmbientLight(Color.WHITE));

        return scene;
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera(true);

        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.getTransforms().add(camTranslate);

        return camera;
    }

    private void plotPunts(){
        puntGroup.getChildren().clear();

        Cylinder eixX = new Cylinder(1, 500);
        eixX.setMaterial(new PhongMaterial(Color.RED));

        Cylinder eixY = new Cylinder(1, 500);
        eixY.setMaterial(new PhongMaterial(Color.GREEN));
        eixY.setRotationAxis(Rotate.Z_AXIS);
        eixY.setRotate(90);

        Cylinder eixZ = new Cylinder(1, 500);
        eixZ.setMaterial(new PhongMaterial(Color.BLUE));
        eixZ.setRotationAxis(Rotate.X_AXIS);
        eixZ.setRotate(90);

        puntGroup.getChildren().addAll(eixX, eixY, eixZ);

        List<Punt> punts =  dades.getPunts();
        if (!(punts.getFirst() instanceof Punt3D)) {
            return;
        }
        System.err.println("Lista de Punts: " + punts.size());

        originPoints = new Translate((int) (getWidth() / 2d), (int) (getHeight() / 2d), 0) {
        };
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

    private void updateCamera() {

    }

    private void addMouseControls(Scene scene) {
        final double THRESHOLD = 0.3; // Constante para el delta mínimo
        final boolean[] isDragging = {false};

        //codi per a la rotació del gràfic
        scene.setOnMousePressed(event -> {
           lastMouse = new Point2D(event.getSceneX(), event.getSceneY());
           isDragging[0] = true;
        });

        scene.setOnMouseDragReleased(event -> {
            isDragging[0] = false;
        });

        scene.setOnMouseDragged(event -> {
            if(event.isPrimaryButtonDown()){
                Point2D newPos = new Point2D(event.getSceneX(), event.getSceneY());
                double deltaX = newPos.getX() - lastMouse.getX();
                double deltaY = newPos.getY() - lastMouse.getY();

                if(Math.abs(deltaY) >= THRESHOLD) {
                    puntsRotVel[0] += deltaY * 0.2;
                    //puntsRotVel[2] += deltaY * 0.2 * Math.sin(Math.toRadians(puntsRotate[1].getAngle()));

                }
                if(Math.abs(deltaX) >= THRESHOLD) {
                    puntsRotVel[1] += -deltaX*0.2;
                }

                lastMouse = newPos;
            }
            System.err.println(puntsRotate[0].getAngle() + ", "+puntsRotate[1].getAngle() + ", " + puntsRotate[2].getAngle());
        });


        //Codi per la roda del ratolí, apropa o allunya el grafic
        scene.setOnScroll(event -> {
            camVel[2] += event.getDeltaY() * 0.5;
        });

        final int maxCamVel = 50;
        final int maxRotVel = 20;
        (new AnimationTimer() {

            @Override
            public void handle(long l) {
                if (Math.abs(camVel[2]) < 0.5){
                    camVel[2] = 0;
                }
                camVel[2] = Math.clamp(camVel[2], -maxCamVel, maxCamVel);
                camTranslate.setZ(camTranslate.getZ() + camVel[2]);
                camVel[2] *= 0.9;

                for (int dim = 0; dim < puntsRotVel.length; dim++) {
                    if(Math.abs(puntsRotVel[dim]) < 0.0001){
                        puntsRotVel[dim] = 0;
                    }
                    puntsRotVel[dim] = Math.clamp(puntsRotVel[dim], -maxRotVel, maxRotVel);
                    puntsRotate[2-dim].setAngle(puntsRotate[2-dim].getAngle() + puntsRotVel[dim]);
                    puntsRotVel[dim] *= 0.95;
                }
            }
        }).start();
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
