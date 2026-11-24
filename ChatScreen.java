/*import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class ChatScreen {
    private ChatApp app;
    private Scene scene;
    private String loggedInUser;
    private BorderPane rootkbeer;
    private ChatArea chatArea;
    private ChatList chatList;
    
    public ChatScreen(ChatApp app, String loggedInUser) {
        this.app = app;
        this.loggedInUser = loggedInUser;
        
        
        chatArea = new ChatArea(app, loggedInUser);
        chatList = new ChatList(app, loggedInUser);
        
      /*  
       chatList.setOnContactClick((contactItem, contact) -> {
    
    // ✅ شرط: لو الاسم هو "General" فقط
    if (contact.equals("General")) {
        
        // 1. إعادة تعيين لون كل الأسماء
        for (javafx.scene.Node node : chatList.getAccountsList().getChildren()) {
            node.setStyle("-fx-background-color: white;");
        }
        
        // 2. تلوين General باللون الأزرق
        contactItem.setStyle("-fx-background-color: #eef2f9;");
        
        // 3. تحديث رأس الشاشة
        chatArea.setHeaderName(contact);
        
        // 4. تغيير صورة البروفايل
        Circle clickedCircle = (Circle) contactItem.getChildren().get(0);
        chatArea.setHeaderProfile(clickedCircle);
        
        // ✅✅✅ 5. قولي للـ Client أنا في GROUP ✅✅✅
        if (app.getClient() != null) {
            app.getClient().setCurrentChatType("GROUP");
            app.getClient().setCurrentRecipient(null);
        }
        
        // 6. إظهار منطقة المحادثة
        rootkbeer.setCenter(chatArea.getRoot());
        
    } else {
        
        // ✅✅✅ الأسماء التانية = PRIVATE ✅✅✅
        
        // 1. إعادة تعيين لون كل الأسماء
        for (javafx.scene.Node node : chatList.getAccountsList().getChildren()) {
            node.setStyle("-fx-background-color: white;");
        }
        
        // 2. تلوين الاسم اللي ضغطتي عليه باللون الأزرق
        contactItem.setStyle("-fx-background-color: #eef2f9;");
        
        // 3. تحديث رأس الشاشة
        chatArea.setHeaderName(contact);
        
        // 4. تغيير صورة البروفايل
        Circle clickedCircle = (Circle) contactItem.getChildren().get(0);
        chatArea.setHeaderProfile(clickedCircle);
        
        // ✅✅✅ 5. قولي للـ Client أنا في PRIVATE مع هذا الشخص ✅✅✅
        if (app.getClient() != null) {
            app.getClient().setCurrentChatType("PRIVATE");
            app.getClient().setCurrentRecipient(contact);  // خزني اسم الشخص
        }
        
        // 6. إظهار منطقة المحادثة
        rootkbeer.setCenter(chatArea.getRoot());
    }
});
        
        // Setup empty welcome screen
        VBox holderBox = new VBox(20);
        holderBox.setAlignment(Pos.CENTER);
        holderBox.setStyle("-fx-background-color: #f0f2f5;");
        Label welcomeMsg = new Label("Welcome to Emy Chat");
        welcomeMsg.setFont(Font.font("System", FontWeight.BOLD, 30));
        welcomeMsg.setTextFill(Color.DARKGRAY);
        holderBox.getChildren().addAll(welcomeMsg);
        
        // Main layout
        rootkbeer = new BorderPane();
        rootkbeer.setLeft(chatList.getLeftSide());
        rootkbeer.setCenter(holderBox);
        
        this.scene = new Scene(rootkbeer, 1440, 1024);
    }
    
    public Text serverr(String x) {
        Text tt = new Text(x);
        tt.setFill(Color.GREY);
        return tt;
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public ChatArea getChatArea() {
        return chatArea;
    }
    
    public ChatList getChatList() {
        return chatList;
    }
}
*/
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ChatScreen {
    private ChatApp app;
    private Scene scene;
      
    public ChatScreen(ChatApp app, String loggedInUser) {
        this.app = app;
        
        BorderPane rootKbeer = new BorderPane();
        
        // 1. إنشاء منطقة الشات
        ChatArea chatArea = new ChatArea(app,loggedInUser);
        
        // 2. إنشاء القائمة الجانبية وتمرير منطقة الشات لها
        ChatList chatList = new ChatList(app, loggedInUser, chatArea);
        
        // 3. تجميعهم في الشاشة الكبيرة
        rootKbeer.setLeft(chatList.getRoot());
        rootKbeer.setCenter(chatArea.getRoot());
        
        // 4. إخبار الكلاينت بمكان استلام الرسايل
        if (app.getClient() != null) {
            app.getClient().listenForMessage(chatArea);
        }
        
        this.scene = new Scene(rootKbeer, 1200, 700);
    }
    
    public Scene getScene() {
        return scene;
    }
}