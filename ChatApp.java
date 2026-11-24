
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage; 
import javafx.util.Duration;

public class ChatApp extends Application {
    private Stage primaryStage;
    private String username; 
   

    @Override 
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showWelcomeScreen();
    }

    
    public void showWelcomeScreen(){
        WelcomeScreen screen = new WelcomeScreen(this);
        primaryStage.setScene(screen.getScene());
        primaryStage.setTitle("Welcome");
        primaryStage.show();
    }

    public void showLoginScreen() {
        LoginScreen screen = new LoginScreen(this);
        primaryStage.setScene(screen.getScene());
    }

    public void showSignUpScreen() {
        SignUpScreen screen = new SignUpScreen(this);
        primaryStage.setScene(screen.getScene());
    } 

public void showGuestScreen() {
    GuestScreen screen = new GuestScreen(this);
    primaryStage.setScene(screen.getScene());
}
 
public void endGSession() {
    endGSession screen = new endGSession(this);
    primaryStage.setScene(screen.getScene());
}

    public void showChatScreen(String username) {
        ChatScreen screen = new ChatScreen(this, username); 
        primaryStage.setScene(screen.getScene());
    }
    
private Client client;
public void setClient(Client client) {
    this.client = client;
}

public Client getClient() {
    return this.client;
}
    public static void main(String[] args) {
        launch(args);
    }

}