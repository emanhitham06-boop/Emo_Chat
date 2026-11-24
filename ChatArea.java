/*import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ChatArea {
    private BorderPane root;
    private Label headerName;
    private Circle headerProfile;
    private VBox messagesContainer;
    private ScrollPane scrollPane;
    private HBox sendBar;
    private TextField txtMessage;
    private Button btnSend;
    private String text;
    
    public ChatArea(ChatApp app, String loggedInUser) {
        root = new BorderPane();
        
        // Chat Header
        HBox chatHeader = new HBox(15);
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        chatHeader.setPadding(new Insets(15, 20, 15, 20));
        chatHeader.setStyle("-fx-background-color:#ceeaf300; -fx-background-radius: 0 0 5 5;");
        headerProfile = new Circle(22);
        headerProfile.setFill(Color.GREY);
        headerProfile.setFill(Color.valueOf("#4A90E2"));
        headerName = new Label();
        headerName.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        headerName.setTextFill(Color.BLACK);
        chatHeader.getChildren().addAll(headerProfile, headerName);
        
        // Messages Container
        messagesContainer = new VBox(10);
        scrollPane = new ScrollPane(messagesContainer);
        scrollPane.vvalueProperty().bind(messagesContainer.heightProperty());
        scrollPane.setFitToWidth(true);
        
        // Send Components
        btnSend = new Button("Send");
        btnSend.setMinWidth(100);
        txtMessage = new TextField();
        txtMessage.setMinWidth(800);
        txtMessage.setPromptText("Type your message...");
        
        btnSend.setOnAction(e -> {
            String text = txtMessage.getText();
            if (!text.trim().isEmpty()) {
                if (app.getClient() != null) {
                    app.getClient().sendMessage(text);
                  
                }
                String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                messagesContainer.getChildren().add(transMessage(text, currentTime));
                txtMessage.clear();
            }
        });
        
        
        txtMessage.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                btnSend.fire();
            }
        });
        
        sendBar = new HBox(10);
        sendBar.getChildren().addAll(txtMessage, btnSend);
        sendBar.setPadding(new Insets(10));
        
        root.setCenter(scrollPane);
        root.setBottom(sendBar);
        root.setTop(chatHeader);
        
        if (app.getClient() != null) {
            app.getClient().listenForMessage(messagesContainer, this);
        }
    }
    
    public String privMsg(String text){
        this.text =("PRIVATE|" + text);
        return text;
    }
    
    public HBox transMessage(String text, String date) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_RIGHT);
        vbox.setMaxWidth(Region.USE_PREF_SIZE);
        
        Text msgContent = new Text(text);
        msgContent.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        if (text.length() > 50) {
            msgContent.setWrappingWidth(400);
        }
        
        Text msgTime = new Text(date);
        msgTime.setFill(Color.GRAY);
        msgTime.setFont(javafx.scene.text.Font.font(10));
        
        vbox.getChildren().addAll(msgContent, msgTime);
        vbox.setPadding(new Insets(10));
        vbox.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#D8E5FF"),
                new CornerRadii(20, 20, 0, 20, false),
                Insets.EMPTY)
        ));
        
        HBox container = new HBox(vbox);
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setPadding(new Insets(5));
        return container;
    }
    
    public HBox recMessage(String User, String text, String date) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setMaxWidth(Region.USE_PREF_SIZE);
        
        Text user = new Text(User);
        user.setFill(Color.GRAY);
        user.setFont(javafx.scene.text.Font.font(10));
        
        Text msgContent = new Text(text);
        msgContent.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        if (text.length() > 50) {
            msgContent.setWrappingWidth(400);
        }
        
        Text msgTime = new Text(date);
        msgTime.setFill(Color.GRAY);
        msgTime.setFont(javafx.scene.text.Font.font(10));
        
        vbox.getChildren().addAll(user, msgContent, msgTime);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: white; "
                + "-fx-background-radius: 20 20 20 0; "
                + "-fx-border-color: #FFF0E8; "
                + "-fx-border-radius: 20 20 20 0; "
                + "-fx-border-width: 2;");
        
        HBox container = new HBox(vbox);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(5, 10, 5, 10));
        return container;
    }
    
    public BorderPane getRoot() {
        return root;
    }
    
    public void setHeaderName(String name) {
        headerName.setText(name);
    }
    
    public void setHeaderProfile(Circle profile) {
        headerProfile.setFill(profile.getFill());
    }
    
    public VBox getMessagesContainer() {
        return messagesContainer;
    }
    
    public Text serverr(String x) {
    Text tt = new Text(x);
    tt.setFill(Color.GREY);
    return tt;
}
    
}*/

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.HashMap;

public class ChatArea {
    private BorderPane root;
    private Label headerName;
    private Circle headerProfile;
    private ScrollPane scrollPane;
    private HashMap<String, VBox> chatBoxes;
    private String currentActiveChat = "General";
    private ChatApp app;
    private String loggedInUser; 

    public ChatArea(ChatApp app, String loggedInUser) {
        this.app = app;
        this.loggedInUser = loggedInUser; // حفظ اسم المستخدم الحالي لتمييز رسائله
        root = new BorderPane();
        chatBoxes = new HashMap<>();
        
        // 1. الهيدر
        HBox chatHeader = new HBox(15);
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        chatHeader.setPadding(new Insets(15, 20, 15, 20));
        chatHeader.setStyle("-fx-background-color:#ceeaf300; -fx-background-radius: 0 0 5 5;");
        headerProfile = new Circle(22, Color.GRAY);
        headerName = new Label("General");
        headerName.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        chatHeader.getChildren().addAll(headerProfile, headerName);
        
        // 2. تجهيز الشات العام كبداية
        VBox generalBox = new VBox(10);
        chatBoxes.put("General", generalBox);
        scrollPane = new ScrollPane(generalBox);
        scrollPane.vvalueProperty().bind(generalBox.heightProperty());
        scrollPane.setFitToWidth(true);
        // 🚀🚀 استدعاء ميثود تحميل التاريخ فور فتح الأبلكيشن
        loadChatHistory();
        // 3. شريط الإرسال
        Button btnSend = new Button("Send");
        btnSend.setMinWidth(100);
        TextField txtMessage = new TextField();
        HBox.setHgrow(txtMessage, Priority.ALWAYS);
        
        txtMessage.setPromptText("Type your message...");
        
        btnSend.setOnAction(e -> {
            String text = txtMessage.getText();
            if (!text.trim().isEmpty()) {
                if (app.getClient() != null) {
                    //app.getClient().sendMessage(text);
                    // بنجهز الرسالة كـ TextMessage ونبعتها
    String chatType = (currentActiveChat.equals("General")) ? "GROUP" : "PRIVATE";
    String recipient = (currentActiveChat.equals("General")) ? null : currentActiveChat;
    
    TextMessage textMessage = new TextMessage(app.getClient().getUsername(), chatType, recipient, text);
    app.getClient().sendMessage(textMessage);
                }
                String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                chatBoxes.get(currentActiveChat).getChildren().add(transMessage(text, currentTime));
                txtMessage.clear();
            }
        });

        txtMessage.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) btnSend.fire();
        });
        
        // 🚀 زرار المرفقات (Attach)
        Button btnAttach = new Button("📎");
        //btnAttach.setStyle("-fx-font-size: 18px; -fx-cursor: hand");
         
        btnAttach.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Choose an Image");
            fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            
            java.io.File selectedFile = fileChooser.showOpenDialog(null);
            
            if (selectedFile != null) {
                try {
                    // 1. بنحول الصورة لـ Bytes عشان تتبعت
                    byte[] fileData = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                    String fileName = selectedFile.getName();
                    
                    // 2. بنجهز كائن الرسالة الصورة
                    String chatType = (currentActiveChat.equals("General")) ? "GROUP" : "PRIVATE";
                    String recipient = (currentActiveChat.equals("General")) ? null : currentActiveChat;
                    ImageMessage imgMsg = new ImageMessage(app.getClient().getUsername(), chatType, recipient, fileData, fileName);
                    
                    // 3. بنبعت الكائن للسيرفر
                    app.getClient().sendMessage(imgMsg);
                    
                    // 4. بنعرض الصورة عندنا في الشات عشان نعرف إنها اتبعتت
                    String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                    chatBoxes.get(currentActiveChat).getChildren().add(transImage(fileData, currentTime));
                    
                } catch (Exception ex) {
                    System.out.println("حصلت مشكلة في إرسال الصورة!");
                    ex.printStackTrace();
                }
            }
        });

        HBox sendBar = new HBox(10);
        sendBar.getChildren().addAll(btnAttach,txtMessage, btnSend);
        sendBar.setPadding(new Insets(10));
        
        // 4. تجميع الواجهة
        root.setTop(chatHeader);
        root.setCenter(scrollPane);
        root.setBottom(sendBar);
    }
    
    // الميثود اللي بتغير الشاشة والهيدر
    public void switchChat(String contactName, Color profileColor) {
        this.currentActiveChat = contactName;
        headerName.setText(contactName);
        headerProfile.setFill(profileColor);
        
        if (!chatBoxes.containsKey(contactName)) {
            chatBoxes.put(contactName, new VBox(10));
        }
        
        VBox activeBox = chatBoxes.get(contactName);
        scrollPane.setContent(activeBox);
        scrollPane.vvalueProperty().unbind();
        scrollPane.vvalueProperty().bind(activeBox.heightProperty());
    }

    // الميثود اللي بتستقبل الرسايل
    public void routeReceivedMessage(String sender, String content, String time, String chatType) {
        javafx.application.Platform.runLater(() -> {
            // التوجيه الذكي: لو الشات برايفت يروح للراسل، لو لأ يروح للـ General
            String targetChat = (chatType != null && chatType.equals("PRIVATE")) ? sender : "General";
           // String targetChat = sender.equals("SERVER") ? "General" : sender;
           
           // رسايل السيرفر دايماً في الـ General
            if (sender.equals("SERVER")) {
                targetChat = "General";
            }
            if (!chatBoxes.containsKey(targetChat)) {
                chatBoxes.put(targetChat, new VBox(10));
            }
            if (sender.equals("SERVER")) {
                Text tt = new Text(content); tt.setFill(Color.GREY);
                chatBoxes.get(targetChat).getChildren().add(tt);
            } else {
                chatBoxes.get(targetChat).getChildren().add(recMessage(sender, content, time));
            }
        });
    }

    private HBox transMessage(String text, String date) {
        VBox vbox = new VBox(5); 
        vbox.setAlignment(Pos.CENTER_RIGHT);
        Text msgContent = new Text(text); msgContent.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        if(text.length() > 50) msgContent.setWrappingWidth(400);
        Text msgTime = new Text(date); msgTime.setFill(Color.GRAY); msgTime.setFont(Font.font(10));
        vbox.getChildren().addAll(msgContent, msgTime);
        vbox.setPadding(new Insets(10));
        vbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#D8E5FF"), new CornerRadii(20, 20, 0, 20, false), Insets.EMPTY)));
        HBox container = new HBox(vbox); container.setAlignment(Pos.CENTER_RIGHT); container.setPadding(new Insets(5));
        return container;
    }

    private HBox recMessage(String User, String text, String date) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        Text user = new Text(User); user.setFill(Color.GRAY); user.setFont(Font.font(10));
        Text msgContent = new Text(text); msgContent.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        if(text.length() > 50) msgContent.setWrappingWidth(400);
        Text msgTime = new Text(date); msgTime.setFill(Color.GRAY); msgTime.setFont(Font.font(10));
        vbox.getChildren().addAll(user, msgContent, msgTime);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: white; -fx-background-radius: 20 20 20 0; -fx-border-color: #FFF0E8; -fx-border-radius: 20 20 20 0; -fx-border-width: 2;");
        HBox container = new HBox(vbox); container.setAlignment(Pos.CENTER_LEFT); container.setPadding(new Insets(5, 10, 5, 10));
        return container;
    }
    
    // 🚀 ميثود الاستلام وتوزيع الصور في الدرج الصح
    public void routeReceivedImage(String sender, byte[] imageData, String time, String chatType) {
        javafx.application.Platform.runLater(() -> {
        String targetChat = (chatType != null && chatType.equals("PRIVATE")) ? sender : "General";             if (!chatBoxes.containsKey(targetChat)) {
                chatBoxes.put(targetChat, new VBox(10));
            }
            chatBoxes.get(targetChat).getChildren().add(recImage(sender, imageData, time));
        });
    }

    // 🚀 ميثود تصميم الصورة المرسلة (بتاعتي)
    private HBox transImage(byte[] imageData, String date) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_RIGHT);
        
        // تحويل البايتس لصورة الجافا
        javafx.scene.image.Image img = new javafx.scene.image.Image(new java.io.ByteArrayInputStream(imageData));
        javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView(img);
        imgView.setFitWidth(250); // عرض مناسب عشان الصورة متمليش الشاشة
        imgView.setPreserveRatio(true);
        
        Text msgTime = new Text(date); 
        msgTime.setFill(Color.GRAY); 
        msgTime.setFont(Font.font(10));
        
        vbox.getChildren().addAll(imgView, msgTime);
        vbox.setPadding(new Insets(10));
        vbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#D8E5FF"), new CornerRadii(20, 20, 0, 20, false), Insets.EMPTY)));
        
        HBox container = new HBox(vbox); 
        container.setAlignment(Pos.CENTER_RIGHT); 
        container.setPadding(new Insets(5));
        return container;
    }

    // 🚀 ميثود تصميم الصورة المستلمة (بتاعت الشخص التاني)
    private HBox recImage(String User, byte[] imageData, String date) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        
        Text user = new Text(User); 
        user.setFill(Color.GRAY); 
        user.setFont(Font.font(10));
        
        javafx.scene.image.Image img = new javafx.scene.image.Image(new java.io.ByteArrayInputStream(imageData));
        javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView(img);
        imgView.setFitWidth(250);
        imgView.setPreserveRatio(true);
        
        Text msgTime = new Text(date); 
        msgTime.setFill(Color.GRAY); 
        msgTime.setFont(Font.font(10));
        
        vbox.getChildren().addAll(user, imgView, msgTime);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: white; -fx-background-radius: 20 20 20 0; -fx-border-color: #FFF0E8; -fx-border-radius: 20 20 20 0; -fx-border-width: 2;");
        
        HBox container = new HBox(vbox); 
        container.setAlignment(Pos.CENTER_LEFT); 
        container.setPadding(new Insets(5, 10, 5, 10));
        return container;
    }
    // 🚀 الميثود السحرية اللي بتقرأ الأرشيف وترسم الشاشات القديمة كلها
    private void loadChatHistory() {
        java.util.ArrayList<Object[]> history = DatabaseConnection.getMessageHistory();
        
        for (Object[] item : history) {
            Message msg = (Message) item[0];
            String time = (String) item[1];
            String sender = msg.getSender();
            
            // تحديد الشات المستهدف (General أو اسم الشخص الآخر)
            String targetChat = "General";
            if (msg.getChatType().equals("PRIVATE")) {
                targetChat = sender.equals(loggedInUser) ? msg.getRecipient() : sender;
            }
            
            // تأمين وجود الحاوية (الدرج) في الـ HashMap
            if (!chatBoxes.containsKey(targetChat)) {
                chatBoxes.put(targetChat, new VBox(10));
            }
            VBox box = chatBoxes.get(targetChat);
            
            // رسم الرسالة بناءً على نوعها (نص / صورة) ومصدرها (أنا / الآخر)
            if (msg instanceof TextMessage) {
                 TextMessage tm = (TextMessage) msg;
                if (sender.equals("SERVER")) {
                  // رسم رسالة السيرفر باللون الرمادي مباشرة بدون ميثود خارجية
                    Text serverText = new Text(tm.getText());
                    serverText.setFill(Color.GREY);
                    box.getChildren().add(serverText);
                } else if (sender.equals(loggedInUser)) {
                    box.getChildren().add(transMessage(tm.getText(), time));
                } else {
                    box.getChildren().add(recMessage(sender, tm.getText(), time));
                }
            } else if (msg instanceof ImageMessage) {
                ImageMessage im = (ImageMessage) msg;
                if (sender.equals(loggedInUser)) {
                    box.getChildren().add(transImage(im.getFileData(), time));
                } else {
                    box.getChildren().add(recImage(sender, im.getFileData(), time));
                }
            }
        }
    }
   

    public BorderPane getRoot() { return root; }
}