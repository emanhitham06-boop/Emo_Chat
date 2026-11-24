
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
public class DatabaseConnection {
    // معلومات السيرفر اللي لسه عاملينه
    private static final String URL = "jdbc:mysql://localhost:3306/chat_app_db";
    private static final String USER = "root"; // اليوزر الافتراضي في XAMPP
    private static final String PASSWORD = ""; // الباسورد الافتراضي بيكون فاضي

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("تم الاتصال بقاعدة البيانات بنجاح! \u2714");
            return conn;
        } catch (SQLException e) {
            System.out.println("فشل الاتصال بقاعدة البيانات! \u274C");
            e.printStackTrace();
            return null;
        }
    }

// ميثود جديدة وظيفتها تاخد الاسم والباسورد وترميهم في الداتا بيز
public static boolean registerUser(String username, String password) {
    // كود الـ SQL اللي بيضيف البيانات في جدول users
    String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
    
    try {
        Connection conn = getConnection(); // نفتح الاتصال
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username); // نحط الاسم مكان علامة الاستفهام الأولى
        pstmt.setString(2, password); // نحط الباسورد مكان علامة الاستفهام التانية
        
        pstmt.executeUpdate(); // نفذ الأمر
        System.out.println("تم تسجيل المستخدم بنجاح في قاعدة البيانات!");
        return true; // نرجع true معناه إن التسجيل نجح
        
    } catch (SQLException e) {
        System.out.println("حصلت مشكلة أثناء التسجيل، ممكن الاسم يكون متسجل قبل كده.");
        e.printStackTrace();
        return false; // نرجع false معناه إن التسجيل فشل
    }
}

// ميثود جديدة للتحقق من بيانات الدخول
    public static boolean validateUser(String username, String password) {
        // كود الـ SQL اللي بيدور على اليوزر والباسورد مع بعض
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            // بنستخدم executeQuery عشان إحنا بنقرأ بيانات مش بنسجلها
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            // لو الـ ResultSet فيه نتيجة (يعني rs.next() بترجع true)، يبقى اليوزر موجود
            if (rs.next()) {
                System.out.println("نجاح: اليوزر والباسورد دول متسجلين عندنا!");
                return true; 
            } else {
                System.out.println("فشل: اليوزر ده مش موجود أو الباسورد غلط.");
                return false; 
            }
            
        } catch (SQLException e) {
            System.out.println("حصلت مشكلة أثناء التحقق من البيانات.");
            e.printStackTrace();
            return false;
        }
    }
    // ميثود جديدة لحفظ الرسائل في الداتا بيز
    public static boolean saveMessage(String sender, String message, String time) {
        String sql = "INSERT INTO messages (sender_name, message_text, msg_time) VALUES (?, ?, ?)";
        
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sender);  
            pstmt.setString(2, message); 
            pstmt.setString(3, time);  
            
            pstmt.executeUpdate();
            System.out.println("saved");
            return true;
        } catch (SQLException e) {
            System.out.println("error while saving");
            e.printStackTrace();
            return false;
        }
    }
    // ميثود لاسترجاع كل الرسائل من قاعدة البيانات
    public static ArrayList<String[]> getAllMessages() {
        ArrayList<String[]> messagesList = new ArrayList<>();
        String sql = "SELECT * FROM messages ORDER BY msg_id ASC"; // بنجيبهم بالترتيب
        
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String sender = rs.getString("sender_name");
                String text = rs.getString("message_text");
                String time = rs.getString("msg_time");
                
                // بنحط بيانات الرسالة في مصفوفة ونضيفها للستة
                messagesList.add(new String[]{sender, text, time});
            }
        } catch (SQLException e) {
            System.out.println("error");
            e.printStackTrace();
        }
        return messagesList;
    }
    // ميثود لجلب كل المستخدمين المسجلين في الداتا بيز
    public static java.util.ArrayList<String> getAllUsersFromDB() {
        java.util.ArrayList<String> usersList = new java.util.ArrayList<>();
        String sql = "SELECT username FROM users"; // بنجيب عمود الأسامي بس
        
        try {
            java.sql.Connection conn = getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                usersList.add(rs.getString("username"));
            }
        } catch (java.sql.SQLException e) {
            System.out.println("مشكلة في جلب أسامي المستخدمين!");
            e.printStackTrace();
        }
        return usersList;
    }
    // 🚀 1. ميثود حفظ كائن الرسالة بالكامل (نص أو صورة) في الداتا بيز
    public static void saveMessageObject(Message msg, String time) {
        String sql = "INSERT INTO serialized_messages (message_object, msg_time) VALUES (?, ?)";
        try (java.sql.Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // تحويل الكائن إلى مصفوفة بايتات (Byte Array) ليتم حفظه في الـ BLOB
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(msg);
            byte[] msgBytes = baos.toByteArray();
            
            pstmt.setBytes(1, msgBytes);
            pstmt.setString(2, time);
            pstmt.executeUpdate();
            System.out.println("🚀 تم حفظ الرسالة بنجاح في أرشيف الداتا بيز!");
        } catch (Exception e) {
            System.out.println("❌ فشل حفظ الرسالة في الداتا بيز");
            e.printStackTrace();
        }
    }
 
    // 🚀 2. ميثود استرجاع كل تاريخ الشات من الداتا بيز وترتيبه بالوقت
    public static java.util.ArrayList<Object[]> getMessageHistory() {
        java.util.ArrayList<Object[]> history = new java.util.ArrayList<>();
        String sql = "SELECT message_object, msg_time FROM serialized_messages ORDER BY id ASC";
        
        try (java.sql.Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                byte[] msgBytes = rs.getBytes("message_object");
                String time = rs.getString("msg_time");
                
                // إعادة تحويل البايتات إلى كائن Message حقيقي مجدداً
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(msgBytes);
                java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
                Message msg = (Message) ois.readObject();
                
                history.add(new Object[]{msg, time});
            }
        } catch (Exception e) {
            System.out.println("❌ فشل جلب تاريخ الرسايل");
            e.printStackTrace();
        }
        return history;
    }
}