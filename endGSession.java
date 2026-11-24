import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

// قمنا بإزالة extends Application
public class endGSession {
    
    private ChatApp app;
    private Scene scene;

    public endGSession(ChatApp app) {
        this.app = app;
        createScreen();
    }

    private void createScreen() {
        Text endd = new Text("Your session has been ended \n We hope that you Enjoyed it");
        
        HBox hbox = new HBox();
        hbox.getChildren().add(endd);
        hbox.setAlignment(Pos.CENTER);
         
        // ربط الـ Scene بالمتغير الخاص بالكلاس وليس متغير جديد
        this.scene = new Scene(hbox, 1440, 1024);
        
        scene.setOnMouseClicked(event -> {
            app.showWelcomeScreen();
        });
         
    }

    // دالة مهمة جداً عشان الكلاس الرئيسي يقدر ياخد الـ Scene ده ويعرضه
    public Scene getScene() {
        return scene;
    }
}