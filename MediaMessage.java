public abstract class MediaMessage extends Message {
    protected byte[] fileData; // دي اللي هتشيل الصورة متحولة لكود عشان تمشي في السلك
    protected String fileName;

    public MediaMessage(String sender, String chatType, String recipient, byte[] fileData, String fileName) {
        super(sender, chatType, recipient);
        this.fileData = fileData;
        this.fileName = fileName;
    }

    public byte[] getFileData() { return fileData; }
    public String getFileName() { return fileName; }
} 