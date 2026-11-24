
import java.io.IOException;
import java.net.Socket;
import static javafx.application.Application.STYLESHEET_MODENA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class LoginScreen {

    private ChatApp app;
    private Scene scene;
    private String username;

    public LoginScreen(ChatApp app) {
        this.app = app; // خطوة مهمة جداً لربط الشاشات ببعض
        createScreen();
    }

    private void createScreen() {
        // الجانب الأيسر
        // 1. تحميل الصورة من اللينك
        Image leftImage = new Image("https://i.pinimg.com/736x/b5/fc/fe/b5fcfe133eba8e563a8c59123cb6959f.jpg");
        BackgroundImage bgImage = new BackgroundImage(
                leftImage,
                BackgroundRepeat.NO_REPEAT, // عدم التكرار
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, // وضعها في المنتصف
                new BackgroundSize(100, 100, true, true, true, true) // جعلها تتمدد (Cover) لتملأ المساحة
        );
        Region leftSide = new Region();
        leftSide.setBackground(new Background(bgImage));
        HBox.setHgrow(leftSide, Priority.ALWAYS);
        // الجانب الأيمن
        VBox rightSide = new VBox(40);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(1021);
        rightSide.setPadding(new Insets(40));
        rightSide.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // العنوان
        Label lbl = new Label("Log In");
        lbl.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 50));
        lbl.setTranslateY(-60);

        // الجدول (Fields)
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(30);

        TextField t1 = new TextField();
        PasswordField t2 = new PasswordField();
        t2.setMinWidth(440);

        grid.add(new Label("User name"), 0, 0);
        grid.add(t1, 1, 0);
        grid.add(new Label("Password"), 0, 1);
        grid.add(t2, 1, 1);

        // الزرار
        Button btn1 = new Button("Log In");
        btn1.setTextFill(Color.WHITE);
        btn1.setPrefSize(550, 40);
        btn1.setStyle("-fx-font-size: 18px; "+"-fx-font-weight: bold;");
        btn1.setBackground(new Background(new BackgroundFill(Color.valueOf("#89a9e3"), new CornerRadii(20), Insets.EMPTY)));

        // برمجة زرار الدخول
        // 1. عرفي الـ Label ده "خارج" الـ handle عشان نتحكم فيه (نظهره ونخفيه)
        Label lblError = new Label();
        lblError.setTextFill(Color.RED);
        HBox errorContainer = new HBox(lblError);
        errorContainer.setAlignment(Pos.CENTER_LEFT); // خليه في النص عشان الرسالة طويلة

// ضيفيه للـ grid مرة واحدة فقط (مثلاً في الصف رقم 4)
        grid.add(errorContainer, 0, 4, 2, 1); // الـ 2 و 1 عشان ياخد مساحة الصف كله

        
        grid.add(lblError, 0, 3, 2, 1); 
         btn1.setOnAction(e -> {
            System.out.println("--- تم الضغط على زرار Log In ---");
            
            setUsername(t1.getText());
            String pass = t2.getText();
            
            if (username.isEmpty() || pass.isEmpty()) {
                lblError.setText("Please fill all fields!");
                lblError.setTextFill(Color.RED);
                grid.add(lblError, 0, 3);
            } else {
                boolean isValid = DatabaseConnection.validateUser(username, pass);
                
                if (isValid) {
                    try {
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, username);
            app.setClient(client);
            app.showChatScreen(t1.getText());
        } catch (IOException ex) {
            lblError.setText("Server is not running!");
        }
                }
    else {
        lblError.setText("Username or password isn't correct.");
    }
            }
        });
       
       btn1.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                btn1.fire();
            }
        });
       
       // 1. وإحنا في مربع اليوزر نيم: لو دسنا سهم لتحت
        t1.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DOWN:
                    t2.requestFocus();
                    break;
                case ENTER: // بالمرة لو داس انتر ينزل للباسورد
                    t2.requestFocus();
                    break;
            }
        });

        // 2. وإحنا في مربع الباسورد: لو دسنا لفوق نرجع لليوزر، ولو لتحت ننزل لزرار اللوجين
        t2.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    t1.requestFocus();
                    break;
                case DOWN:
                    btn1.requestFocus();
                    break;
                case ENTER: // لو داس انتر وهو بيكتب الباسورد، يعمل لوجين علطول!
                    btn1.fire();
                    break;
            }
        });

        // 3. وإحنا واقفين على زرار اللوجين: لو دسنا سهم لفوق نرجع للباسورد
        btn1.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    t2.requestFocus();
                    break;
                case ENTER: // لو داس انتر وهو محدد الزرار، ينفذ اللوجين
                    btn1.fire();
                    break;
            }
        });
        // هيبارلينك للرجوع للـ Sign Up
        Text signLog = new Text("Don't have an account? ");
        Hyperlink hBack = new Hyperlink("Sign Up");
        hBack.setOnAction(e -> app.showSignUpScreen());
        HBox signn = new HBox(15);
        signn.getChildren().addAll(signLog, hBack);
        signn.setAlignment(Pos.CENTER);

        Text guestSign = new Text("You are using a temporary session. Please note that all chats and data will be permanently deleted once the session ends");
        Hyperlink h3 = new Hyperlink("Guest");
        guestSign.setWrappingWidth(400);
        h3.setOnAction(e -> app.showGuestScreen());

        HBox guestt = new HBox(15);
        guestt.getChildren().addAll(guestSign, h3);
        guestt.setAlignment(Pos.CENTER);

        VBox options = new VBox(8);

        options.getChildren().addAll(guestt, signn);
        // --- الخطوة اللي كانت ناقصة عندك: إضافة العناصر للـ rightSide ---
        rightSide.getChildren().addAll(lbl, grid, btn1, options);

        // تجميع كل شيء في المشهد
        HBox mainLayout = new HBox(leftSide, rightSide);
        this.scene = new Scene(mainLayout, 1440, 1024);
    }

    public void setUsername(String username) {
         this.username = username;
    }
    
    public  String getUsername() {
         return username;
    }
   
    public Scene getScene() {
        return scene;
    }
}