package vista;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class Eixos3D extends JFXPanel {
    public Eixos3D() {

    }

    public void initFX(){
        Scene scene = createScene();
        this.setScene(scene);
    }

    private Scene createScene(){
        Group root  =  new  Group();
        Scene  scene  =  new  Scene(root, Color.ALICEBLUE);

        Camera pCamera = new PerspectiveCamera();
        pCamera.setRotationAxis(new Point3D(0,1,0));
        //pCamera.setRotate(Math.PI);
        pCamera.setRotationAxis(new Point3D(0,0,1));
        //pCamera.setRotate(45);
        for (int i = 0; i < 10; i++) {
            Sphere sphere = new Sphere(4);
            sphere.setTranslateX(30);
            sphere.setTranslateY(620);
            sphere.setTranslateZ(20*i);
            root.getChildren().add(sphere);
        }

        PhongMaterial rojo = new PhongMaterial(Color.RED);
        PhongMaterial verde = new PhongMaterial(Color.GREEN);
        PhongMaterial azul = new PhongMaterial(Color.BLUE);

// Eje X
        Cylinder ejeX = new Cylinder(1, 100);
        ejeX.setMaterial(rojo);
        ejeX.setRotationAxis(Rotate.Z_AXIS);
        ejeX.setRotate(90);
        ejeX.setTranslateX(50);

// Eje Y
        Cylinder ejeY = new Cylinder(1, 100);
        ejeY.setMaterial(verde);
        ejeY.setTranslateY(-50);

// Eje Z
        Cylinder ejeZ = new Cylinder(1, 100);
        ejeZ.setMaterial(azul);
        ejeZ.setRotationAxis(Rotate.X_AXIS);
        ejeZ.setRotate(90);
        ejeZ.setTranslateZ(50);

// Agrupación y posición
        Group eje3D = new Group(ejeX, ejeY, ejeZ);
        eje3D.setTranslateX(20);
        eje3D.setTranslateY(600);
        eje3D.setTranslateZ(0);


        root.getChildren().add(eje3D);

        pCamera.setFarClip(Double.MAX_VALUE);
        pCamera.getTransforms().addAll(
                new Rotate(-45, Rotate.Y_AXIS),
                new Rotate(-45, Rotate.X_AXIS)
        );

        scene.setCamera(pCamera);
        return (scene);
    }
}
