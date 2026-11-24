/*import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    // قائمة ثابتة لتتبع جميع المستخدمين المتصلين لإرسال الرسائل لهم جميعاً
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(clientUsername + ": " + messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try { 
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            // 🚀 نفس القاعدة: Output ثم Input
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
            
            // بنستقبل الاسم كأول حاجة مبعوتة
            this.clientUsername = (String) in.readObject();
            clientHandlers.add(this);
            
            // بنبلغ الكل إن في حد دخل (باستخدام TextMessage)
            TextMessage joinMsg = new TextMessage("SERVER", "GROUP", null, clientUsername + " has entered the chat!");
            broadcastMessage(joinMsg);
            
        } catch (Exception e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        Message messageFromClient;
        while (socket.isConnected()) {
            try {
                // 🚀 بنستقبل Object من الكلاينت
                messageFromClient = (Message) in.readObject();
                // 🚀🚀 السطر السحري: السيرفر بيحفظ الرسالة فوراً في الداتا بيز مع وقتها الحالي
                String currentTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
                DatabaseConnection.saveMessageObject(messageFromClient, currentTime);
                // التوجيه الذكي بدون شفرات النصوص
                if (messageFromClient.getChatType().equals("PRIVATE")) {
                    sendPrivateMessage(messageFromClient, messageFromClient.getRecipient());
                } else {
                    broadcastMessage(messageFromClient);
                }
                
            } catch (Exception e) {
                closeEverything();
                break;
            }
        }  
    }

    // 🚀 الإرسال العام
    public void broadcastMessage(Message messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(this.clientUsername)) {
                    clientHandler.out.writeObject(messageToSend);
                    clientHandler.out.flush();
                }
            } catch (IOException e) {
                closeEverything();
            }
        }
    }
    
    // 🚀 الإرسال الخاص
    public void sendPrivateMessage(Message messageToSend, String targetUser) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (clientHandler.clientUsername.equals(targetUser)) {
                    clientHandler.out.writeObject(messageToSend);
                    clientHandler.out.flush();
                    break;
                }
            } catch (IOException e) {
                closeEverything();
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        TextMessage leaveMsg = new TextMessage("SERVER", "GROUP", null, clientUsername + " has left the chat!");
        broadcastMessage(leaveMsg);
    }

    public void closeEverything() {
        removeClientHandler();
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}