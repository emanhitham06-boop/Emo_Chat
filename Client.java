/*mport java.io.*;
import java.net.Socket;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private String currentChatType = "GROUP"; 
    private String currentRecipient = null;
    
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            sendUsername();
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage(String messageToSend) {
    try {
        String message = messageToSend;
        
        // ✅ استخدمي فكرتك هنا
        if (currentChatType.equals("PRIVATE")) {
            message = "PRIVATE|" + currentRecipient + "|" + messageToSend;
        }
        
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    } catch (IOException e) {
        closeEverything();
    }
}
    
    private void sendUsername() {
    try {
        bufferedWriter.write(username);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    } catch (IOException e) {
        closeEverything();
    }
}
    
     public void setCurrentChatType(String type) {
        this.currentChatType = type;
    }
    
    public void setCurrentRecipient(String recipient) {
        this.currentRecipient = recipient;
    }
    
    
    public void listenForMessage(ChatArea chatArea){ 
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                try {
                    while ((msgFromGroupChat = bufferedReader.readLine()) != null) {
                        final String message = msgFromGroupChat;
                        final String currentTime = java.time.LocalTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
                        
                        // 🚀 بنحلل الرسالة ونبعتها للـ ChatArea وهو هيدخلها الدرج الصح
                        String sender;
                        String content;
                        
                        if (message.startsWith("SERVER:")) {
                            sender = "SERVER";
                            content = message.substring(7);
                        } else if (message.contains(":")) {
                            int colonIndex = message.indexOf(":");
                            sender = message.substring(0, colonIndex);
                            content = message.substring(colonIndex + 2).trim();
                        } else {
                            sender = "Unknown";
                            content = message;
                        }
                        
                        chatArea.routeReceivedMessage(sender, content, currentTime);
                    }
                } catch (IOException e) {
                    chatArea.routeReceivedMessage("SERVER", "Connection lost!", 
                        java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")));
                    closeEverything();
                }
            }
        }).start();
    }
    
    public void closeEverything() {
    try {
        if (bufferedReader != null) bufferedReader.close();
        if (bufferedWriter != null) bufferedWriter.close();
        if (socket != null) socket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}}*/
        
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    private String currentChatType = "GROUP"; 
    private String currentRecipient = null;
    
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            // 🚀 أهم قاعدة: تعريف الـ Output وعمل Flush قبل الـ Input
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
            
            this.username = username;
            
            // بنبعت الاسم للسيرفر أول حاجة كتعارف
            out.writeObject(username);
            out.flush();
            
        } catch (IOException e) {
            closeEverything();
        }
    } 

    // 🚀 الميثود دي بقت بتاخد Object كامل مش نص
    public void sendMessage(Message messageToSend) {
        try {
            out.writeObject(messageToSend);
            out.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }
    
    public void setCurrentChatType(String type) {
        this.currentChatType = type;
    }
    
    public void setCurrentRecipient(String recipient) {
        this.currentRecipient = recipient;
    }

    // 🚀 الاستماع بقى بيستقبل Objects ويحولها لرسائل
    public void listenForMessage(ChatArea chatArea){
        new Thread(() -> {
            Message msgFromServer;
            try {
                // بنقرا الكائن اللي جاي من السيرفر
                while ((msgFromServer = (Message) in.readObject()) != null) {
                    final String sender = msgFromServer.getSender();
                    final String currentTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
                    
                    // بنشوف لو الرسالة نصية
                    if (msgFromServer instanceof TextMessage) {
                        TextMessage textMsg = (TextMessage) msgFromServer;
                        chatArea.routeReceivedMessage(sender, textMsg.getText(), currentTime ,msgFromServer.getChatType());
                    } 
                    // 🚀 بنشوف لو الرسالة دي صورة!
                    else if (msgFromServer instanceof ImageMessage) {
                        ImageMessage imgMsg = (ImageMessage) msgFromServer;
                        chatArea.routeReceivedImage(sender, imgMsg.getFileData(), currentTime, msgFromServer.getChatType());                    }
                }
            } catch (Exception e) {
                chatArea.routeReceivedMessage("SERVER", "Connection lost!", 
                    java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")), "GROUP");
                closeEverything();
            }
        }).start();
    }
    
    public void closeEverything() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ميثود عشان تجيب اسم اليوزر الحالي
    public String getUsername() {
        return username;
    }
}