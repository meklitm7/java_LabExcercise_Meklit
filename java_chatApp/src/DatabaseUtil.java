import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

 
public class DatabaseUtil {

    

    private static final String DB_URL = "jdbc:mysql://localhost:3306/chatapp";

    private static final String DB_USER = "root";

   // Read password from environment variable
    private static final String PASSWORD = System.getenv("MYSQL_PASSWORD");

     

    static {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("MySQL JDBC Driver Loaded Successfully!");

        } catch (ClassNotFoundException e) {

            System.err.println("JDBC DRIVER NOT FOUND!");

            e.printStackTrace();
        }
    }

     

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);
    }

     

    public static String generateGUID() {

        return UUID.randomUUID().toString();
    }

     

    public static boolean registerUser(
            String username,
            String password) {

         
        if (userExists(username)) {

            System.out.println("Username already exists!");

            return false;
        }

        String sql = "INSERT INTO users (guid, username, password) VALUES (?, ?, ?)";

        try (
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String guid = generateGUID();

            stmt.setString(1, guid);

            stmt.setString(2, username.trim());

            stmt.setString(3, password.trim());

            int rows = stmt.executeUpdate();

            if (rows > 0) {

                System.out.println("USER REGISTERED SUCCESSFULLY!");

                return true;
            }

        } catch (SQLException e) {

            System.err.println("REGISTER ERROR:");

            e.printStackTrace();
        }

        return false;
    }

    

    public static boolean userExists(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            System.err.println("USER EXISTS ERROR:");

            e.printStackTrace();
        }

        return false;
    }

    

    public static String validateLogin(
            String username,
            String password) {

        String sql = "SELECT guid FROM users WHERE username = ? AND password = ?";

        try (
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            stmt.setString(2, password.trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String guid = rs.getString("guid");

                System.out.println("LOGIN SUCCESS!");

                return guid;
            }

            System.out.println("INVALID USERNAME OR PASSWORD!");

        } catch (SQLException e) {

            System.err.println("LOGIN ERROR:");

            e.printStackTrace();
        }

        return null;
    }
 

    public static String getUserGUID(
            String username) {

        String sql = "SELECT guid FROM users WHERE username = ?";

        try (
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return rs.getString("guid");
            }

        } catch (SQLException e) {

            System.err.println("GET USER ERROR:");

            e.printStackTrace();
        }

        return null;
    }

     

    public static boolean saveMessage(
            String senderGuid,
            String receiverGuid,
            String content,
            byte[] imageData,
            String type) {

        String sql = "INSERT INTO messages " +
                "(sender_guid, receiver_guid, content, image_data, message_type) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, senderGuid);

            stmt.setString(2, receiverGuid);

            stmt.setString(3, content);

            stmt.setBytes(4, imageData);

            stmt.setString(5, type);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("SAVE MESSAGE ERROR:");

            e.printStackTrace();
        }

        return false;
    }

     

    public static List<String[]> getAllUsers(
            String excludeGuid) {

        List<String[]> users = new ArrayList<>();

        String sql = "SELECT guid, username FROM users WHERE guid != ?";

        try (
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, excludeGuid);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                users.add(new String[] {

                        rs.getString("guid"),

                        rs.getString("username")
                });
            }

        } catch (SQLException e) {

            System.err.println("GET USERS ERROR:");

            e.printStackTrace();
        }

        return users;
    }

    

    public static boolean testConnection() {

        try (
                Connection conn = getConnection()) {

            System.out.println(
                    "DATABASE CONNECTED SUCCESSFULLY!");

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "DATABASE CONNECTION FAILED!");

            e.printStackTrace();
        }

        return false;
    }

    

    public static void main(String[] args) {

        testConnection();
    }
}
