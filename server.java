import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    private ServerSocket serverSocket;

    public server(ServerSocket serverSocket) { 
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            System.out.println("Server is running and waiting for clients...");
            while (!serverSocket.isClosed()) {
                // السيرفر بيستقبل اتصال جديد
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                // إنشاء الـ Handler وتمرير السوكيت له
                // الـ ClientHandler جوه الكونسلتركتور بتاعه هيستلم الاسم ويضيف نفسه للقائمة
                ClientHandler clientHandler = new ClientHandler(socket);

                // تشغيل الـ Thread الخاص بهذا العميل لضمان عدم تعليق السيرفر
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // فتح المنفذ 1234
        ServerSocket serverSocket = new ServerSocket(1234);
        server server = new server(serverSocket);
        server.startServer();
    }
}