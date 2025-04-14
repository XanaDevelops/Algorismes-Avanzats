package vista;


import controlador.Comunicar;
import controlador.Main;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import model.Dades;
import model.punts.Punt;
import model.punts.Punt3D;

import java.util.Arrays;
import java.util.List;

public class Eixos3D extends JFXPanel implements Comunicar {
    private final Dades dades;

    //color per defecte del punt
    private final Material puntBlau = new PhongMaterial(Color.BLUE);
    private final Material puntGroc = new PhongMaterial(Color.GOLD);
    //tamany del graf en 3D
    final int tamGraf = 1000;

    private Group root, puntGroup, camGroup, camGroup2, camGroup3;
    private Group eixGroup;

    private Camera camera;

    //Transformacions necessaries per a la rotació i traslació
    private final Translate camTranslate = new Translate(0,0,-tamGraf*(4/3d));
    private final Translate camCenter = new Translate(0,0,0);
    private final double[] transVels = new double[] {0,0,0};

    private final Rotate camRotateY = new Rotate(45, Rotate.Y_AXIS);
    private final Rotate camRotateX = new Rotate(-20+ 180, Rotate.X_AXIS);
    private final double[] rotVels = new double[] {0,0};

    /**
     * Reestableix la posició
     */
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

    //això impedeix que JavaFX s'aturi tot sol
    static {
        Platform.setImplicitExit(false);
    }

    public Eixos3D() {
        super();
        dades = Main.instance.getDades();
        System.out.println("NO WARNING: ignorar l'altre warning de JavaFX, funciona perfectament");
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

        //configura les transformacions i relacions
        camGroup.getTransforms().addAll(camRotateY, camRotateX);
        camGroup2.getTransforms().addAll(camCenter);

        camGroup.getChildren().add(camGroup2);
        camGroup2.getChildren().add(camGroup3);
        camGroup3.getChildren().add(camera);

        addMouseControls(scene);
        root.getChildren().addAll(puntGroup, camGroup, eixGroup, new AmbientLight(Color.WHITE));

        return scene;
    }

    /**
     * Configura la camara
     * @return camara
     */
    private Camera createCamera() {
        Camera camera = new PerspectiveCamera(true);

        camera.setNearClip(0.1);
        camera.setFarClip(Dades.RANG_PUNT*2);
        camera.getTransforms().addAll(camTranslate);

        return camera;
    }

    /**
     * Crea una esfera per visualitzar el Punt3D
     * @param punt punt
     * @param mat color
     * @return Sphere
     */
    private Sphere createPunt(Punt3D punt, Material mat) {
        Sphere puntS = new Sphere(5); //els punts en 3D son esferes
        puntS.setMaterial(mat);
        //fer coordenades relatives
        puntS.setTranslateX(punt.x / (double) Dades.RANG_PUNT * tamGraf);
        puntS.setTranslateY(punt.y / (double) Dades.RANG_PUNT * tamGraf);
        puntS.setTranslateZ(punt.z / (double) Dades.RANG_PUNT * tamGraf);

        return puntS;
    }
    /**
     * Dibuixa els punts en 3D
     */
    private void plotPunts(){
        puntGroup.getChildren().clear();

        List<Punt> punts =  dades.getPunts();
        if (punts== null || punts.isEmpty() || !(punts.getFirst() instanceof Punt3D)) {
            return;
        }

        for(Punt p : punts) {
            Punt3D punt = (Punt3D) p;
            puntGroup.getChildren().add(createPunt(punt, puntBlau));
        }
    }

    private Shape3D createLine(Punt3D a, Punt3D b, Material mat){
        Point3D p1 = new Point3D(a.x / (double) Dades.RANG_PUNT * tamGraf,a.y/ (double) Dades.RANG_PUNT * tamGraf,a.z/ (double) Dades.RANG_PUNT * tamGraf);
        Point3D p2 = new Point3D(b.x/ (double) Dades.RANG_PUNT * tamGraf,b.y/ (double) Dades.RANG_PUNT * tamGraf,b.z/ (double) Dades.RANG_PUNT * tamGraf);

        Point3D diff = p2.subtract(p1);
        double height = diff.magnitude();

        // Create the cylinder with the computed height
        Cylinder cylinder = new Cylinder(2, height);
        cylinder.setMaterial(mat);

        // Position the cylinder at the midpoint of p1 and p2
        Point3D mid = p1.midpoint(p2);
        cylinder.setTranslateX(mid.getX());
        cylinder.setTranslateY(mid.getY());
        cylinder.setTranslateZ(mid.getZ());

        // Align the cylinder to the vector between p1 and p2
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        angle = Math.toDegrees(angle);

        if (axisOfRotation.magnitude() > 0) {
            cylinder.getTransforms().add(new Rotate(-angle, axisOfRotation));
        }
        return cylinder;
    }

    /**
     * Mostra el resultat d'una operació
     */
    private void mostrarResultat(String algorisme){
        Dades.Resultat r;

        switch (algorisme){
            case "Força Bruta":
                r = dades.getLastResultatFB().getValue();
                break;
            case "Dividir i vèncer":
                r = dades.getLastResultatDV().getValue();
                break;
            case "Kd-Arbre":
                r = dades.getLastResultatKD().getValue();
                break;
            default:
                System.err.println("Eixos3D algorisme?? "+algorisme);
                return;
        }
        if (r.getTipus().equalsIgnoreCase("min")){
            puntGroup.getChildren().add(createPunt((Punt3D) r.getP1(), puntGroc));
            puntGroup.getChildren().add(createPunt((Punt3D) r.getP2(), puntGroc));
        }else{ //max
            // TODO: IMPLEMENTAR CORRECTAMENT ALGORISME DISTANCIA!!
            puntGroup.getChildren().add(createLine((Punt3D) dades.getPunts().getFirst(), (Punt3D) dades.getPunts().getLast(), puntGroc));
        }
    }

    /**
     * Mostra els eixos
     */
    private void genEixos() {
        final int tamGraf = this.tamGraf*2;
        final int tamEix = 5;
        final int boxSize = 20;

        //els eixos com a tal
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

        //simbolitzen la direcció positiva
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

    /**
     * Configura la interacció amb la grafica
     * Utilitza velocitats per donar la sensació de moviment més responsiu i natural
     * @param scene
     */
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

            if(event.isPrimaryButtonDown()){ //rotació
                rotVels[1] -= deltaX * 0.2;
                rotVels[0] += deltaY * 0.2;
            }else if (event.isSecondaryButtonDown()){ //translació
                transVels[0] -= deltaX * 0.2;
                transVels[1] -= deltaY * 0.2;

            }

            lastMouse = newPos;
        });

        scene.setOnScroll(event -> {
            transVels[2] += event.getDeltaY() *1.25; //el zoom
        });

        final int maxTVel = 20;
        final int maxRVel = 20;

        //Executa a cada frame
        (new AnimationTimer() {
            @Override
            public void handle(long l) {
                //limita velocitat
                for (int dim = 0; dim < transVels.length; dim++) {
                    transVels[dim] = Math.abs(transVels[dim]) > 0.05 ? Math.clamp(transVels[dim], -maxTVel, maxTVel): 0;
                }
                for (int dim = 0; dim < rotVels.length; dim++) {
                    rotVels[dim] = Math.abs(rotVels[dim]) > 0.05 ? Math.clamp(rotVels[dim], -maxRVel, maxRVel): 0;
                }

                //fa els moviments
                camRotateY.setAngle(camRotateY.getAngle() + rotVels[1]);
                camRotateX.setAngle(camRotateX.getAngle() - rotVels[0]);

                camCenter.setX(camCenter.getX() + transVels[0]);
                camCenter.setY(camCenter.getY() + transVels[1]);
                camTranslate.setZ(camTranslate.getZ() + transVels[2]);

                //redueix la velocitat
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
        String[] args = s.split(":");
        switch(args[0]){
            case "dibuixPunts":
            case "pintar":
                Platform.runLater(this::plotPunts);
                break;
            case "dibuixDistancia":
                Platform.runLater(() -> this.mostrarResultat(args[1]));
            case "aturar":
                break;
        }
    }
}
