import java.io.IOException;
import java.net.Socket;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Application.STYLESHEET_MODENA;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;


public class GuestScreen {

    private ChatApp app;
    private Scene scene;
    public static String username;
    

    public GuestScreen(ChatApp app) {
        this.app = app;
        createScreen();
        
    }

    private void createScreen() {
        // 1. إعداد صورة الخلفية للجانب الأيسر
        Image leftImage = new Image("https://i.pinimg.com/736x/b5/fc/fe/b5fcfe133eba8e563a8c59123cb6959f.jpg");
        BackgroundImage bgImage = new BackgroundImage(
                leftImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        Region leftSide = new Region();
        leftSide.setBackground(new Background(bgImage));
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        // 2. الجانب الأيمن
        VBox rightSide = new VBox(30); // تقليل المسافة شوية عشان العناصر كتير
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(1021);
        rightSide.setPadding(new Insets(40));
        rightSide.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        HBox box2 = new HBox(leftSide, rightSide);
        

        // 3. المكونات
        Label lbl = new Label("Guest");
        lbl.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 50));
        lbl.setTranslateY(-20);

        Label lbl2 = new Label("You are using a temporary session. Please note that all chats and data will be permanently deleted once the session ends.");
        lbl2.setWrapText(true);
        lbl2.setTextAlignment(TextAlignment.CENTER);
        lbl2.setMaxWidth(500);

        Label lbl3 = new Label("Session will last only 60 minutes");
        lbl3.setTextFill(Color.RED);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
       

        Label lbl1 = new Label("User name");
        TextField t1 = new TextField();
        t1.setMinWidth(400);
        grid.add(lbl1, 0, 0);
        grid.add(t1, 1, 0);

        Button btn1 = new Button("Get Started");
        btn1.setPrefSize(500, 40);
        btn1.setTextFill(Color.WHITE);
        btn1.setStyle("-fx-background-color: #89a9e3; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 16px;");

      t1.setOnKeyPressed(event -> {
    // نختبر هل الزرار اللي تم الضغط عليه هو زرار انتر؟
    if (event.getCode() == KeyCode.ENTER) {
        
        // اكتبي هنا أي حاجة عايزاها تتنفذ
       btn1.fire();
        
    }
});  // برمجة الأزرار والروابط
       btn1.setOnAction(e -> {
    if (t1.getText().isEmpty()) {
        Label lblError = new Label("Please enter a guest name!");
        lblError.setTextFill(Color.RED);
        grid.add(lblError, 1, 1);
    } else {
        // 🚀 السطر السحري: بنضيف كلمة Guest قبل الاسم اللي اليوزر كتبه
                String finalGuestName = "[Guest] " + t1.getText();
        try {
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, finalGuestName);
            app.setClient(client);   
             app.showChatScreen (finalGuestName);
             Timeline hour = new Timeline(
             new KeyFrame (Duration.hours(1),event -> {
            app.endGSession();
    }),
            new KeyFrame (Duration.minutes(60.5),event -> {
            app.showWelcomeScreen();
             }));
    hour.play();
    
        } catch (IOException ex) {
            Label lblError = new Label("Server is not running!");
            lblError.setTextFill(Color.RED);
            grid.add(lblError, 1, 1);
            app.showChatScreen(finalGuestName);
        }
    }
});
        Hyperlink h1  = new Hyperlink("Log In");
        h1.setOnAction(e -> app.showLoginScreen());
        HBox loginBox = new HBox(5, new Label("Already have an account?"), h1);
        loginBox.setAlignment(Pos.CENTER);

        Hyperlink h2 = new Hyperlink("Sign up");
        h2.setOnAction(e -> app.showSignUpScreen());
        HBox signupBox = new HBox(5, new Label("Don't have an account?"), h2);
        signupBox.setAlignment(Pos.CENTER);
        
        VBox vbox =new VBox(8);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(loginBox,signupBox);
        // إضافة العناصر للـ rightSide
        rightSide.getChildren().addAll(lbl, lbl2, grid, lbl3 ,btn1, vbox);

        // 4. إنشاء المشهد (السطر الأهم)
        this.scene = new Scene(box2, 1440, 1024);
    }
 
    public void setUsername(String newName) {
        username = newName; // تخزين القيمة الجديدة  
    }
    
    public static String getUsername() {
        return username; 
    }
    
    public Scene getScene() {
        return scene;
    }
}