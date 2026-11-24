public class TextMessage extends Message {
    private String text;

    public TextMessage(String sender, String chatType, String recipient, String text) {
        super(sender, chatType, recipient);
        this.text = text;
    }

    public String getText() { return text; }
} 