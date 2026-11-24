
import java.io.IOException;
import java.net.Socket;
import static javafx.application.Application.STYLESHEET_MODENA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SignUpScreen {

    private ChatApp app;
    private Scene scene;

    public SignUpScreen(ChatApp app) {
        this.app = app;
        createScreen();
    }

    private void createScreen() {
        // الجانب الأيسر الملون
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

        // الجانب الأيمن الأبيض
        VBox rightSide = new VBox(40);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(1021);
        rightSide.setPadding(new Insets(40, 40, 40, 40));
        rightSide.setBackground(new Background(new BackgroundFill(Color.valueOf("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));

        // حاوية الشاشة كاملة
        HBox box2 = new HBox(leftSide, rightSide);

        // جدول المدخلات
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(30);

        Label lbl = new Label("Sign Up");
        lbl.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 50));
        lbl.setTextFill(Color.BLACK);
        lbl.setTranslateY(-60);

        TextField t1 = new TextField();
        PasswordField t2 = new PasswordField();
        t2.setMinWidth(440);
        PasswordField t3 = new PasswordField();

        grid.add(new Label("User name"), 0, 0);
        grid.add(t1, 1, 0);
        grid.add(new Label("Password"), 0, 1);
        grid.add(t2, 1, 1);
        grid.add(new Label("Confirm Password"), 0, 2);
        grid.add(t3, 1, 2);

        Button btn1 = new Button("Sign Up");
        btn1.setPrefSize(600, 40);
        btn1.setTextFill(Color.WHITE);
        btn1.setStyle("-fx-font-size: 18px; "+"-fx-font-weight: bold;");
        btn1.setBackground(new Background(new BackgroundFill(Color.valueOf("#89a9e3"), new CornerRadii(20), Insets.EMPTY)));

        // التعامل مع الضغط على الزرار
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (t1.getText().isEmpty() || t2.getText().isEmpty() || t3.getText().isEmpty()) {
                    Label lblError2 = new Label("Please fill all fields!");
                    lblError2.setTextFill(Color.RED);
                    grid.add(lblError2, 0, 3);
                } else if (!t2.getText().equals(t3.getText())) {
                    Label lblError1 = new Label("Passwords do not match!");
                    lblError1.setTextFill(Color.RED);
                    grid.add(lblError1, 0, 4);
                } else {
                    boolean isRegistered = DatabaseConnection.registerUser(t1.getText(), t2.getText());
        if (isRegistered) {
            try {
                Socket socket = new Socket("localhost", 1234);
                Client client = new Client(socket, t1.getText());
                app.setClient(client);
                app.showChatScreen(t1.getText());
            } catch (IOException ex) {
                Label lblError = new Label("Server is not running!");
                lblError.setTextFill(Color.RED);
                grid.add(lblError, 0, 5);
            }
        } else {
            Label lblError = new Label("Username already exists!");
            lblError.setTextFill(Color.RED);
            grid.add(lblError, 0, 5);
        }
    }
            }
           
});
        // 2. تفعيل اختصارات الكيبورد (الأسهم والإنتر)
        t1.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DOWN: case ENTER:
                    t2.requestFocus();
                    break;
            }
        });
        t2.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    t1.requestFocus();
                    break;
                case DOWN: case ENTER:
                    t3.requestFocus();
                    break;
            }
        });
        t3.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    t2.requestFocus();
                    break;
                case DOWN: 
                    btn1.requestFocus();
                    break;
                case ENTER: 
                    btn1.fire();
                    break;
            }
        });
        btn1.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    t3.requestFocus();
                    break;
                case ENTER:
                    btn1.fire();
                    break;
            }
        });
        // زرار الرجوع للوج إن (إضافي عشان اليوزر ميتزنقش هنا)
        Text loginn = new Text("Already have an account?");
        Hyperlink h2 = new Hyperlink("Log In");
        h2.setOnAction(e -> app.showLoginScreen());

        HBox loginSign = new HBox(15);
        loginSign.setAlignment(Pos.CENTER);
        loginSign.getChildren().addAll(loginn, h2);

        Text guestSign = new Text("You are using a temporary session. Please note that all chats and data will be permanently deleted once the session ends");
        Hyperlink h3 = new Hyperlink("Guest");
        guestSign.setWrappingWidth(400);
        h3.setOnAction(e -> app.showGuestScreen());

        HBox guestt = new HBox(15);
        guestt.getChildren().addAll(guestSign, h3);
        guestt.setAlignment(Pos.CENTER);

        VBox options = new VBox(8);

        options.getChildren().addAll(guestt, loginSign);
        // تجميع العناصر في الجانب الأيمن
        rightSide.getChildren().addAll(lbl, grid, btn1, options);

        // أهم سطر: إنشاء المشهد وحفظه في متغير الكلاس
        this.scene = new Scene(box2, 1440, 1024);
    }

    public Scene getScene() {
        return scene;
    }
}