package vista;


import controlador.Comunicar;
import controlador.Main;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
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

import java.util.Arrays;
import java.util.List;

public class Eixos3D extends JFXPanel implements Comunicar {
    private final Dades dades;

    private final Material puntBlau = new PhongMaterial(Color.BLUE);

    final int tamGraf = 1000;

    private Group root, puntGroup, camGroup, camGroup2, camGroup3;

    private Camera camera;



    private final Translate camTranslate = new Translate(0,0,-tamGraf*(4/3d));
    private final Translate camCenter = new Translate(0,0,0);
    private final double[] transVels = new double[] {0,0,0};

    private final Rotate camRotateY = new Rotate(45, Rotate.Y_AXIS);
    private final Rotate camRotateX = new Rotate(-20+ 180, Rotate.X_AXIS);
    private final double[] rotVels = new double[] {0,0};

    private void reset(){
        Arrays.fill(rotVels,0);
        Arrays.fill(transVels,0);
        camRotateY.setAngle(-45);
        camRotateX.setAngle(160);
        camCenter.setX(0);
        camCenter.setY(0);
        camTranslate.setZ(-tamGraf*(4/3d));
    }

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
        //camera.getTransforms().add(new Rotate(180, Rotate.Z_AXIS));
        scene.setCamera(camera);

        camGroup.getTransforms().addAll(camRotateY, camRotateX);
        camGroup2.getTransforms().addAll(camCenter);

        camGroup.getChildren().add(camGroup2);
        camGroup2.getChildren().add(camGroup3);
        camGroup3.getChildren().add(camera);

        addMouseControls(scene);
        root.getChildren().addAll(puntGroup, camGroup, eixGroup, new AmbientLight(Color.WHITE));

        return scene;
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera(true);

        camera.setNearClip(0.1);
        camera.setFarClip(Dades.RANG_PUNT*2);
        camera.getTransforms().addAll(camTranslate);

        return camera;
    }

    private void plotPunts(){
        puntGroup.getChildren().clear();


        List<Punt> punts =  dades.getPunts();
        if (punts== null || punts.isEmpty() || !(punts.getFirst() instanceof Punt3D)) {
            return;
        }


        for(Punt p : punts) {
            Punt3D punt = (Punt3D) p;
            Sphere puntS = new Sphere(5);
            puntS.setMaterial(puntBlau);
            puntS.setTranslateX(punt.x/(double)Dades.RANG_PUNT * tamGraf);
            puntS.setTranslateY(punt.y/(double)Dades.RANG_PUNT * tamGraf);
            puntS.setTranslateZ(punt.z/(double)Dades.RANG_PUNT * tamGraf);

            puntGroup.getChildren().add(puntS);
        }
    }

    private void genEixos() {
        final int tamGraf = this.tamGraf*2;
        final int tamEix = 5;
        final int boxSize = 20;
        Cylinder eixX = new Cylinder(tamEix, tamGraf);
        eixX.setMaterial(new PhongMaterial(Color.RED));

        Cylinder eixY = new Cylinder(tamEix, tamGraf);
        eixY.setMaterial(new PhongMaterial(Color.GREEN));
        eixY.setRotationAxis(Rotate.Z_AXIS);
        eixY.setRotate(90);

        Cylinder eixZ = new Cylinder(tamEix, tamGraf);
        eixZ.setMaterial(new PhongMaterial(Color.DEEPSKYBLUE));
        eixZ.setRotationAxis(Rotate.X_AXIS);
        eixZ.setRotate(90);

        Box redArrow = new Box(boxSize, boxSize, boxSize);   // Oriented along Y (red axis)
        redArrow.setMaterial(new PhongMaterial(Color.RED));
        redArrow.setTranslateY(tamGraf/2d);

        Box greenArrow = new Box(boxSize, boxSize, boxSize); // Oriented along X (green axis)
        greenArrow.setMaterial(new PhongMaterial(Color.GREEN));
        greenArrow.setTranslateX(tamGraf/2d);

        Box blueArrow = new Box(boxSize, boxSize, boxSize);  // Oriented along Z (blue axis)
        blueArrow.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        blueArrow.setTranslateZ(tamGraf/2d);

        final Color colorPlano = new Color(0.4, 0.4, 0.4, 0.05);


        Box planeXZ = new Box(tamGraf, 0.01, tamGraf);  // Plano en el plano XZ
        planeXZ.setMaterial(new PhongMaterial(colorPlano));

        eixGroup.getChildren().addAll(eixX, eixY, eixZ, redArrow, greenArrow, blueArrow, planeXZ);

    }

    private void updateCamera() {

    }

    private void addMouseControls(Scene scene) {

        //codi per a la rotació del gràfic
        scene.setOnMousePressed(event -> {
           lastMouse = new Point2D(event.getSceneX(), event.getSceneY());
            Arrays.fill(rotVels,0);
            Arrays.fill(transVels, 0);

        });

        scene.setOnMouseDragged(event -> {
            Point2D newPos = new Point2D(event.getSceneX(), event.getSceneY());
            double deltaX = newPos.getX() - lastMouse.getX();
            double deltaY = newPos.getY() - lastMouse.getY();

            if(event.isPrimaryButtonDown()){
                rotVels[1] -= deltaX * 0.2;
                rotVels[0] += deltaY * 0.2;
            }else if (event.isSecondaryButtonDown()){
                transVels[0] -= deltaX * 0.2;
                transVels[1] -= deltaY * 0.2;

            }

            lastMouse = newPos;
        });

        scene.setOnScroll(event -> {
            transVels[2] += event.getDeltaY() *1.25;
        });


        final int maxTVel = 20;
        final int maxRVel = 20;
        (new AnimationTimer() {

            @Override
            public void handle(long l) {
                for (int dim = 0; dim < transVels.length; dim++) {
                    transVels[dim] = Math.abs(transVels[dim]) > 0.05 ? Math.clamp(transVels[dim], -maxTVel, maxTVel): 0;
                }
                for (int dim = 0; dim < rotVels.length; dim++) {
                    rotVels[dim] = Math.abs(rotVels[dim]) > 0.05 ? Math.clamp(rotVels[dim], -maxRVel, maxRVel): 0;
                }

                camRotateY.setAngle(camRotateY.getAngle() + rotVels[1]);
                camRotateX.setAngle(camRotateX.getAngle() - rotVels[0]);

                camCenter.setX(camCenter.getX() + transVels[0]);
                camCenter.setY(camCenter.getY() + transVels[1]);
                camTranslate.setZ(camTranslate.getZ() + transVels[2]);

                for (int dim = 0; dim < transVels.length; dim++) {
                    transVels[dim] *= 0.95;
                }
                for (int dim = 0; dim < rotVels.length; dim++) {
                    rotVels[dim] *= 0.975;
                }

            }
        }).start();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case R:
                    reset();
                    break;
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
