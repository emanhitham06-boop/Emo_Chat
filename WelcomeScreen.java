
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class WelcomeScreen {

    private ChatApp app; // مرجع للكلاس الرئيسي للتنقل
    private Scene scene;

    public WelcomeScreen(ChatApp app) {
        this.app = app;
        createScreen();
    }

   private void createScreen() {
        // 1. إعداد صورة الخلفية
        Image leftImage = new Image("https://i.pinimg.com/736x/b5/fc/fe/b5fcfe133eba8e563a8c59123cb6959f.jpg");
        BackgroundImage bgImage = new BackgroundImage(
                leftImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        Region leftSide = new Region();
        leftSide.setBackground(new Background(bgImage)); // وضع الصورة هنا
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        VBox rightSide = new VBox(20);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(1021);
        rightSide.setPadding(new Insets(40));

        HBox box2 = new HBox(leftSide, rightSide);

        // --- الأزرار ---
        Button btn1 = new Button("Create account");
        Button btn2 = new Button("Log In");
        Button btn3 = new Button("Guest");

        // تنسيق الأزرار (نفس كودك)
        String btnStyle = "-fx-background-color: #89a9e3; -fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;";
        btn1.setStyle(btnStyle); btn1.setPrefSize(600, 40);
        btn2.setStyle(btnStyle); btn2.setPrefSize(600, 40);
        btn3.setStyle(btnStyle); btn3.setPrefSize(600, 40);

        // الربط
        btn1.setOnAction(e -> app.showSignUpScreen());
        btn2.setOnAction(e -> app.showLoginScreen());
        btn3.setOnAction(e -> app.showGuestScreen());

        rightSide.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // !!! مسموح فقط بوضع خلفية الـ rightSide هنا
        // السطر اللي كان بيمسح صورة الـ leftSide شيلناه من هنا خلاص

        Label lbl1 = new Label("WELCOME!!");
        lbl1.setFont(Font.font("System", FontWeight.BOLD, 30));

        Label lbl2 = new Label("You are using a temporary session. Please note that all chats and data will be permanently deleted once the session ends");
        lbl2.setWrapText(true);
        lbl2.setTextAlignment(TextAlignment.CENTER);

        Image img = new Image("https://360matchpro.com/wp-content/uploads/2024/07/The-7-Best-Signup-Platforms_Feature.png");
        ImageView m1 = new ImageView(img);
        m1.setFitWidth(250);
        m1.setPreserveRatio(true);

        rightSide.getChildren().addAll(lbl1, m1, btn1, btn2, lbl2, btn3);

        this.scene = new Scene(box2, 1440, 1024);
    }
    public Scene getScene() {
        return scene;
    }
}
