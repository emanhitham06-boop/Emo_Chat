public class ImageMessage extends MediaMessage {
    public ImageMessage(String sender, String chatType, String recipient, byte[] fileData, String fileName) {
        super(sender, chatType, recipient, fileData, fileName);
    }
} 