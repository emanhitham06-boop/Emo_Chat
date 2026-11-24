import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIComponents {

    // ميثود إنشاء شكل الرسالة المرسلة (التي سببت الخطأ)
    public static HBox createSentMessage(String text, String time) {
        Label msgLabel = new Label(text);
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(300);
        msgLabel.setTextFill(Color.BLACK);
        msgLabel.setFont(new Font(16));

        Label correctLabel = new Label(time + " \u2713\u2713");
        correctLabel.setFont(new Font(11));
        correctLabel.setTextFill(Color.GREY);

        VBox bubbleContainer = new VBox(5);
        bubbleContainer.setAlignment(Pos.BOTTOM_RIGHT);
        bubbleContainer.setPadding(new Insets(10));
        bubbleContainer.getChildren().addAll(msgLabel, correctLabel);
        bubbleContainer.setBackground(new Background(new BackgroundFill(
        Color.valueOf("#dcf8c6"), new CornerRadii(15, 15, 0, 15, false), Insets.EMPTY)));

        HBox messageRow = new HBox();
        messageRow.setAlignment(Pos.CENTER_RIGHT);
        messageRow.setPadding(new Insets(5, 15, 5, 15));
        messageRow.getChildren().add(bubbleContainer);

        return messageRow;
    }

    // ميثود إنشاء عنصر في قائمة الأسماء (اللي بنستخدمها في الجنب)
    public static HBox createChatItem(String name) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 15, 10, 15));
        
        Circle profile = new Circle(25);
        profile.setFill(Color.LIGHTGRAY);
        profile.setStroke(Color.GRAY);
        
        Label lblName = new Label(name);
        lblName.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        item.getChildren().addAll(profile, lblName);
        item.setStyle("-fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;"); // خط بسيط بين الأسماء
        
        return item;
    }
}