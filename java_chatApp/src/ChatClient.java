import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Chat Client - connects to MultiClientServer
 * For sending/receiving messages and photos
 */
public class ChatClient {

  private static final String SERVER_HOST = "localhost";
  private static final int SERVER_PORT = 5000;

  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  private String myGuid = "";
  private String username = "";

  private boolean connected = false;

   
  private MessageListener listener;

   
  public static void main(String[] args) {

    ChatClient client = new ChatClient();

    boolean success = client.connect();

    if (success) {
      System.out.println("Connected to server!");
    } else {
      System.out.println("Failed to connect!");
    }
  }

   
  public boolean connect(String host, int port) {

    try {

      socket = new Socket(host, port);

      in = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));

      out = new PrintWriter(
          socket.getOutputStream(), true);

      connected = true;

      System.out.println("Connected to chat server!");

       
      Thread listenerThread = new Thread(() -> listenForMessages());

      listenerThread.setDaemon(true);

      listenerThread.start();

      return true;

    } catch (IOException e) {

      System.err.println("Connection failed: " + e.getMessage());

      connected = false;

      return false;
    }
  }

   
  public boolean connect() {
    return connect(SERVER_HOST, SERVER_PORT);
  }

  
  public void setMessageListener(MessageListener listener) {
    this.listener = listener;
  }

  
  private void listenForMessages() {

    try {

      String message;

      while (connected && (message = in.readLine()) != null) {

        handleMessage(message);
      }

    } catch (IOException e) {

      System.err.println("Connection lost: " + e.getMessage());

    } finally {

      connected = false;
    }
  }

   
  private void handleMessage(String message) {

    System.out.println("Received: " + message);

    if (listener == null) {
      return;
    }

    if (message.startsWith("LOGIN_SUCCESS:")) {

      myGuid = message.substring(14);

      listener.onLoginSuccess(myGuid);

    } else if (message.startsWith("LOGIN_FAILED:")) {

      listener.onLoginFailed("Invalid credentials");

    } else if (message.startsWith("REGISTER_SUCCESS:")) {

      myGuid = message.substring(17);

      listener.onRegisterSuccess(myGuid);

    } else if (message.startsWith("REGISTER_FAILED:")) {

      listener.onRegisterFailed("Username already exists");

    } else if (message.startsWith("MSG:")) {

      String[] parts = message.substring(4).split(":", 2);

      if (parts.length == 2) {

        listener.onMessageReceived(parts[0], parts[1]);
      }

    } else if (message.startsWith("IMAGE:")) {

      String[] parts = message.substring(6).split(":", 2);

      if (parts.length == 2) {

        listener.onImageReceived(parts[0], parts[1]);
      }

    } else if (message.startsWith("USERS:")) {

      parseUsers(message.substring(6));

    } else if (message.startsWith("MSG_SENT:")
        || message.startsWith("IMAGE_SENT:")) {

      listener.onMessageSent(message.substring(9));
    }
  }

   
  private void parseUsers(String data) {

    List<String[]> users = new ArrayList<>();

    if (!data.isEmpty()) {

      String[] userPairs = data.split(";");

      for (String pair : userPairs) {

        String[] parts = pair.split(",");

        if (parts.length == 2) {

          users.add(parts);
        }
      }
    }

    if (listener != null) {

      listener.onUsersReceived(users);
    }
  }

   
  public void login(String username, String password) {

    if (!connected || out == null) {

      System.out.println("Server is not connected!");

      return;
    }

    this.username = username.trim();

    out.println("LOGIN:" + username.trim() + ":" + password.trim());
  }

   
  public void register(String username, String password) {

    if (!connected || out == null) {

      System.out.println("Server is not connected!");

      return;
    }

    this.username = username.trim();

    out.println("REGISTER:" + username.trim() + ":" + password.trim());
  }

   
  public void sendMessage(String targetGuid, String content) {

    if (!connected || out == null) {

      System.out.println("Cannot send message. Not connected.");

      return;
    }

    out.println("MSG:" + targetGuid + ":" + content);
  }

   
  public void sendImage(String targetGuid, String imageBase64) {

    if (!connected || out == null) {

      System.out.println("Cannot send image. Not connected.");

      return;
    }

    out.println("IMAGE:" + targetGuid + ":" + imageBase64);
  }

   
  public void getUsers() {

    if (!connected || out == null) {

      System.out.println("Cannot get users. Not connected.");

      return;
    }

    out.println("GETUSERS:" + myGuid);
  }

   
  public void logout() {

    try {

      if (out != null) {

        out.println("LOGOUT");
      }

      connected = false;

      if (socket != null && !socket.isClosed()) {

        socket.close();
      }

      System.out.println("Disconnected from server.");

    } catch (IOException e) {

      e.printStackTrace();
    }
  }

   
  public String getMyGuid() {
    return myGuid;
  }

   
  public boolean isConnected() {
    return connected;
  }

   
  public interface MessageListener {

    void onLoginSuccess(String guid);

    void onLoginFailed(String error);

    void onRegisterSuccess(String guid);

    void onRegisterFailed(String error);

    void onMessageReceived(String senderGuid, String content);

    void onImageReceived(String senderGuid, String imageBase64);

    void onMessageSent(String targetGuid);

    void onUsersReceived(List<String[]> users);
  }
}