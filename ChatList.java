/*import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class ChatList {
    private VBox leftSide;
    private VBox accountsList;
    private TextField searchField;
    private Label menuIcon;
    private ContextMenu contextMenu;
    private HBox general;
    private java.util.ArrayList<String> allContacts; // حفظ قائمة الجهات للبحث
    
    public ChatList(ChatApp app, String loggedInUser) {
        leftSide = new VBox();
        leftSide.setPrefWidth(400);
        leftSide.setStyle("-fx-background-color: #8FADE5;");
        
        // Top Bar
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-border-color: transparent transparent #000000 transparent; -fx-border-width: 1;");
        
        menuIcon = new Label("\u2630");
        menuIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff; -fx-cursor: hand;");
        
        searchField = new TextField();
        searchField.setPromptText("\uD83D\uDD0D search");
        searchField.setStyle("-fx-background-radius: 20; -fx-background-color: #f4f6fa;");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        topBar.getChildren().addAll(menuIcon, searchField);
        
        // Accounts List
        accountsList = new VBox();
        
        // ✅ إضافة "General" مرة واحدة فقط في البداية
        general = createItems("General", false);
        accountsList.getChildren().add(general);
        
        // ✅ جلب المستخدمين من قاعدة البيانات
        allContacts = DatabaseConnection.getAllUsersFromDB();
        
        // ✅ إضافة المستخدمين بعد "General"
        for (String contact : allContacts) {
            if (!contact.equals(loggedInUser)) {
                addContactToList(contact, accountsList);
            }
        }
        
        // ✅ إضافة خاصية البحث - مع الحفاظ على "General" في الأعلى
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            // مسح القائمة ولكن نحتفظ بـ "General"
            accountsList.getChildren().clear();
            accountsList.getChildren().add(general); // ✅ إعادة إضافة General أولاً
            
            // إضافة المستخدمين الذين يتطابقون مع البحث
            for (String contact : allContacts) {
                if (!contact.equals(loggedInUser) && contact.toLowerCase().contains(newVal.toLowerCase())) {
                    addContactToList(contact, accountsList);
                }
            }
        });
        
        ScrollPane leftScroll = new ScrollPane(accountsList);
        leftScroll.setFitToWidth(true);
        leftScroll.setPannable(true);
        leftScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(leftScroll, Priority.ALWAYS);
        leftSide.getChildren().addAll(topBar, leftScroll);
        
        // Menu Setup
        contextMenu = new ContextMenu();
        HBox profileHeader = new HBox(10, new Circle(15, Color.LIGHTGRAY), new Label(loggedInUser));
        profileHeader.setAlignment(Pos.CENTER_LEFT);
        profileHeader.setPadding(new Insets(10));
        CustomMenuItem profileItem = new CustomMenuItem(profileHeader);
        
        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setStyle("-fx-text-fill: red;");
        logoutItem.setOnAction(e -> { app.showWelcomeScreen(); });
        
        contextMenu.getItems().addAll(profileItem, new SeparatorMenuItem(), logoutItem);
        menuIcon.setOnMouseClicked(e -> contextMenu.show(menuIcon, e.getScreenX(), e.getScreenY()));
    }
    
    public HBox createItems(String name, boolean isSelected) {
        HBox item = new HBox(15, new Circle(20, Color.GRAY), new Label(name));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle(isSelected ? "-fx-background-color: #eef2f9;" : "-fx-background-color: white;");
        return item;
    }
    
    private void addContactToList(String contact, VBox accountsList) {
        HBox contactItem = createItems(contact, false);
        
        // Hover effect للمستخدم العادي
        contactItem.setOnMouseEntered(e -> {
            if (!contactItem.getStyle().contains("#eef2f9")) {
                contactItem.setStyle("-fx-background-color: #f8f9fa; -fx-cursor: hand;");
            }
        });
        
        contactItem.setOnMouseExited(e -> {
            if (!contactItem.getStyle().contains("#eef2f9")) {
                contactItem.setStyle("-fx-background-color: white;");
            }
        });
        
        accountsList.getChildren().add(contactItem);
    }
    
    // ✅ دالة لإعداد Hover effect لـ General فقط
    private void setupGeneralHover() {
        general.setOnMouseEntered(e -> {
            if (!general.getStyle().contains("#eef2f9")) {
                general.setStyle("-fx-background-color: #f8f9fa; -fx-cursor: hand;");
            }
        });
        
        general.setOnMouseExited(e -> {
            if (!general.getStyle().contains("#eef2f9")) {
                general.setStyle("-fx-background-color: white;");
            }
        });
    }
    
    public VBox getLeftSide() {
        return leftSide;
    }
    
    public VBox getAccountsList() {
        return accountsList;
    }
    
    public void setOnContactClick(java.util.function.BiConsumer<HBox, String> onClick) {
        // ✅ تفعيل Hover لـ General
        setupGeneralHover();
        
        // ✅ التعامل مع ضغط General
        general.setOnMouseClicked(e -> {
            // إعادة تعيين لون كل العناصر
            for (javafx.scene.Node node : accountsList.getChildren()) {
                node.setStyle("-fx-background-color: white;");
            }
            // تلوين General
            general.setStyle("-fx-background-color: #eef2f9;");
            // تنفيذ الـ onClick
            onClick.accept(general, "General");
        });
        
        // ✅ التعامل مع ضغط المستخدمين العاديين
        for (javafx.scene.Node node : accountsList.getChildren()) {
            if (node == general) continue; // نتخطى General
            
            HBox contactItem = (HBox) node;
            String contact = ((Label) contactItem.getChildren().get(1)).getText();
            
            contactItem.setOnMouseClicked(e -> {
                // إعادة تعيين لون كل العناصر
                for (javafx.scene.Node n : accountsList.getChildren()) {
                    n.setStyle("-fx-background-color: white;");
                }
                // تلوين العنصر المحدد
                contactItem.setStyle("-fx-background-color: #eef2f9;");
                // تنفيذ الـ onClick
                onClick.accept(contactItem, contact);
            });
            
        }
    }
    
    public void refreshContactList(String searchText, java.util.ArrayList<String> allContacts, String loggedInUser) {
        accountsList.getChildren().clear();
        
        // ✅ إعادة إضافة General أولاً
        accountsList.getChildren().add(general);
        
        // ✅ إضافة المستخدمين الذين يتطابقون مع البحث
        for (String contact : allContacts) {
            if (!contact.equals(loggedInUser) && contact.toLowerCase().contains(searchText.toLowerCase())) {
                addContactToList(contact, accountsList);
            }
        }
    }
}*/

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ChatList {
    private VBox root;
    private ChatApp app;
    private String loggedInUser;
    private ChatArea chatArea; // مرجع لمنطقة الشات
    private VBox accountsList;

    public ChatList(ChatApp app, String loggedInUser, ChatArea chatArea) {
        this.app = app;
        this.loggedInUser = loggedInUser;
        this.chatArea = chatArea;
        
        root = new VBox();
        root.setPrefWidth(400);
        root.setStyle("-fx-background-color: #8FADE5;");

        // شريط البحث
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-border-color: transparent transparent #000000 transparent; -fx-border-width: 1;");
        Label menuIcon = new Label("\u2630");
        menuIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff; -fx-cursor: hand;");
        TextField searchField = new TextField();
        searchField.setPromptText("\uD83D\uDD0D search");
        searchField.setStyle("-fx-background-radius: 20; -fx-background-color: #f4f6fa;");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        topBar.getChildren().addAll(menuIcon, searchField);

        // قائمة الأسماء
        accountsList = new VBox();
        
        // إضافة General
        HBox generalItem = createItems("General", true);
        generalItem.setOnMouseClicked(e -> handleContactClick("General", generalItem));
        accountsList.getChildren().add(generalItem);

        // جلب الأسامي من الداتا بيز
        java.util.ArrayList<String> allContacts = DatabaseConnection.getAllUsersFromDB();
        for (String contact : allContacts) {
            if (!contact.equals(loggedInUser) && !contact.equalsIgnoreCase("General")) {
                addContactToList(contact);
            }
        }

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            accountsList.getChildren().clear();
            accountsList.getChildren().add(generalItem);
            for (String contact : allContacts) {
                if (!contact.equals(loggedInUser) && !contact.equalsIgnoreCase("General") && contact.toLowerCase().contains(newVal.toLowerCase())) {
                    addContactToList(contact);
                }
            }
        });

        ScrollPane leftScroll = new ScrollPane(accountsList);
        leftScroll.setFitToWidth(true);
        leftScroll.setStyle("-fx-background: white; -fx-background-color: white;");
        VBox.setVgrow(leftScroll, Priority.ALWAYS);
         
        // المنيو
        ContextMenu contextMenu = new ContextMenu();
        HBox profileHeader = new HBox(10, new Circle(15, Color.LIGHTGRAY),new Label(loggedInUser));
        profileHeader.setAlignment(Pos.CENTER_LEFT); profileHeader.setPadding(new Insets(10));
        CustomMenuItem profileItem = new CustomMenuItem(profileHeader);
        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setStyle("-fx-text-fill: red;");
        logoutItem.setOnAction(e -> { app.showWelcomeScreen(); });
        contextMenu.getItems().addAll(profileItem, new SeparatorMenuItem(), logoutItem);
        menuIcon.setOnMouseClicked(e -> contextMenu.show(menuIcon, e.getScreenX(), e.getScreenY()));

        root.getChildren().addAll(topBar, leftScroll);
    }

    private void addContactToList(String contact) {
        HBox contactItem = createItems(contact, false);
        contactItem.setOnMouseEntered(e -> {
            if (!contactItem.getStyle().contains("#eef2f9")) contactItem.setStyle("-fx-background-color: #f8f9fa; -fx-cursor: hand;");
        });
        contactItem.setOnMouseExited(e -> {
            if (!contactItem.getStyle().contains("#eef2f9")) contactItem.setStyle("-fx-background-color: white;");
        });
        contactItem.setOnMouseClicked(e -> handleContactClick(contact, contactItem));
        accountsList.getChildren().add(contactItem);
    }

    private void handleContactClick(String contact, HBox contactItem) {
        for (javafx.scene.Node node : accountsList.getChildren()) {
            node.setStyle("-fx-background-color: white;");
        }
        contactItem.setStyle("-fx-background-color: #eef2f9;");
        
        Circle clickedCircle = (Circle) contactItem.getChildren().get(0);
        
        // 🚀 بنكلم الـ ChatArea هنا
        chatArea.switchChat(contact, (Color) clickedCircle.getFill());
        
        if (app.getClient() != null) {
            if (contact.equals("General")) {
                app.getClient().setCurrentChatType("GROUP");
                app.getClient().setCurrentRecipient(null);
            } else {
                app.getClient().setCurrentChatType("PRIVATE");
                app.getClient().setCurrentRecipient(contact);
            }
        }
    }

    private HBox createItems(String name, boolean isSelected) {
        HBox item = new HBox(15, new Circle(20, Color.GRAY), new Label(name));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle(isSelected ? "-fx-background-color: #eef2f9;" : "-fx-background-color: white;");
        return item;
    }

    public VBox getRoot() { return root; }
}