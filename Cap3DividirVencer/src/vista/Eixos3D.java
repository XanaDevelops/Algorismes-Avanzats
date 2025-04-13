package vista;


import controlador.Comunicar;
import controlador.Main;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import model.Dades;
import model.punts.Punt;
import model.punts.Punt3D;

import java.util.List;

public class Eixos3D extends JFXPanel implements Comunicar {
    private final Dades dades;

    private final Material puntBlau = new PhongMaterial(Color.BLUE);

    private Translate originPoints;

    private Group root, puntGroup, camGroup, camGroup2, camGroup3;

    private Camera camera;



    private final Translate camTranslate = new Translate(0,0,-750);
    private final Rotate camRotateY = new Rotate(-20, Rotate.Y_AXIS);
    private final Rotate camRotateX = new Rotate(-20, Rotate.X_AXIS);

    private Point2D lastMouse = new Point2D(0,0);

    //això impedeix que JAvaFX s'aturi tot sol
    static {
        Platform.setImplicitExit(false);
    }

    private Group eixGroup;

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
        eixGroup = new Group();
        camGroup = new Group();
        camGroup2 = new Group();
        camGroup3 = new Group();

        genEixos();


        Scene scene = new Scene(root, getWidth(), getHeight(),true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        camera = createCamera();
        scene.setCamera(camera);

        camGroup.getTransforms().addAll(camRotateY, camRotateX);

        camGroup.getChildren().add(camGroup2);
        camGroup2.getChildren().add(camGroup3);
        camGroup3.getChildren().add(camera);

        addMouseControls(scene);
        root.getChildren().addAll(puntGroup, camGroup, eixGroup);

        return scene;
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera(true);

        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.getTransforms().addAll(camTranslate);

        return camera;
    }

    private void plotPunts(){
        puntGroup.getChildren().clear();


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

    private void genEixos() {
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

        Box redArrow = new Box(1, 3, 1);   // Oriented along Y (red axis)
        redArrow.setMaterial(new PhongMaterial(Color.RED));
        redArrow.setTranslateY(255);

        Box greenArrow = new Box(3, 1, 1); // Oriented along X (green axis)
        greenArrow.setMaterial(new PhongMaterial(Color.GREEN));
        greenArrow.setTranslateX(255);

        Box blueArrow = new Box(1, 1, 3);  // Oriented along Z (blue axis)
        blueArrow.setMaterial(new PhongMaterial(Color.BLUE));
        blueArrow.setTranslateZ(255);

        final Color colorPlano = new Color(0.4, 0.4, 0.4, 0.05);


        Box planeXZ = new Box(600, 1, 600);  // Plano en el plano XZ
        planeXZ.setMaterial(new PhongMaterial(colorPlano));

        eixGroup.getChildren().addAll(eixX, eixY, eixZ, redArrow, greenArrow, blueArrow, planeXZ);

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

                camRotateY.setAngle(camRotateY.getAngle() + deltaX * 0.2);
                camRotateX.setAngle(camRotateX.getAngle() - deltaY * 0.2);

                lastMouse = newPos;
            }
        });


        //Codi per la roda del ratolí, apropa o allunya el grafic
        scene.setOnScroll(event -> {

        });

        final int maxCamVel = 50;
        final int maxRotVel = 20;
        (new AnimationTimer() {

            @Override
            public void handle(long l) {

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
