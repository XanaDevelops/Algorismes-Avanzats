package vista;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
        Text text  =  new  Text();

        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("Welcome JavaFX!");

        root.getChildren().add(text);

        return (scene);
    }
}
