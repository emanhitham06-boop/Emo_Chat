import java.io.Serializable;

public abstract class Message implements Serializable {
    private String sender;
    private String chatType; // هيشيل كلمة "GROUP" أو "PRIVATE"
    private String recipient; // اسم المستلم لو برايفت

    public Message(String sender, String chatType, String recipient) {
        this.sender = sender;
        this.chatType = chatType;
        this.recipient = recipient;
    }

    public String getSender() { return sender; }
    public String getChatType() { return chatType; }
    public String getRecipient() { return recipient; }
}