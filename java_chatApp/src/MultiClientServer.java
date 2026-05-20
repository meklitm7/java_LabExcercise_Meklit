import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.Base64;

/**
 * Multi Client Chat Server
 */
public class MultiClientServer {


    private static final int PORT = 5000;

    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    private static ServerSocket serverSocket;

    private static boolean running = true;

     

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("STARTING CHAT SERVER...");
        System.out.println("=================================");

        
        boolean dbOk = DatabaseUtil.testConnection();

        if (!dbOk) {

            System.out.println("DATABASE CONNECTION FAILED!");
            System.out.println("SERVER STOPPED!");

            return;
        }

        startServer(PORT);
    }

     

    public static void startServer(int port) {

        try {

            serverSocket = new ServerSocket(port);

            System.out.println("=================================");
            System.out.println("CHAT SERVER STARTED");
            System.out.println("PORT: " + port);
            System.out.println("WAITING FOR CLIENTS...");
            System.out.println("=================================");

            while (running) {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "NEW CLIENT CONNECTED: "
                                + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket);

                Thread thread = new Thread(handler);

                thread.start();
            }

        } catch (IOException e) {

            System.err.println("SERVER ERROR:");

            e.printStackTrace();
        }
    }

     

    public static void stopServer() {

        running = false;

        try {

            if (serverSocket != null) {

                serverSocket.close();
            }

            System.out.println("SERVER STOPPED!");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

     


    public static void sendToClient(
            String targetGuid,
            String message) {

        ClientHandler handler = clients.get(targetGuid);

        if (handler != null) {

            handler.sendMessage(message);

        } else {

            System.out.println(
                    "TARGET CLIENT NOT ONLINE: "
                            + targetGuid);
        }
    }

     
    static class ClientHandler implements Runnable {

        private Socket socket;

        private BufferedReader in;

        private PrintWriter out;

        private String clientGuid = "";

        private String username = "";
 

        public ClientHandler(Socket socket) {

            this.socket = socket;

            try {

                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

                out = new PrintWriter(
                        socket.getOutputStream(),
                        true);

            } catch (IOException e) {

                System.err.println(
                        "CLIENT STREAM ERROR:");

                e.printStackTrace();
            }
        }

        
        

        @Override
        public void run() {

            try {

                String message;

                while ((message = in.readLine()) != null) {

                    handleMessage(message);
                }

            } catch (IOException e) {

                System.out.println(
                        "CLIENT CONNECTION LOST: "
                                + username);

            } finally {

                disconnect();
            }
        }

        

        private void handleMessage(String message) {

            System.out.println("RECEIVED: " + message);

            try {

                if (message.startsWith("LOGIN:")) {

                    handleLogin(message.substring(6));

                } else if (message.startsWith("REGISTER:")) {

                    handleRegister(message.substring(9));

                } else if (message.startsWith("MSG:")) {

                    handleChatMessage(message.substring(4));

                } else if (message.startsWith("IMAGE:")) {

                    handleImageMessage(message.substring(6));

                } else if (message.startsWith("GETUSERS:")) {

                    handleGetUsers(message.substring(9));

                } else if (message.equals("LOGOUT")) {

                    disconnect();
                }

            } catch (Exception e) {

                System.err.println(
                        "MESSAGE HANDLE ERROR:");

                e.printStackTrace();
            }
        }

         
        

        private void handleLogin(String data) {

            String[] parts = data.split(":", 2);

            if (parts.length != 2) {

                out.println("LOGIN_FAILED:Invalid format");

                return;
            }

            String username = parts[0].trim();

            String password = parts[1].trim();

            if (username.isEmpty() || password.isEmpty()) {

                out.println("LOGIN_FAILED:Empty fields");

                return;
            }

            String guid = DatabaseUtil.validateLogin(
                    username,
                    password);

            if (guid != null) {

                this.clientGuid = guid;

                this.username = username;

                clients.put(guid, this);

                out.println("LOGIN_SUCCESS:" + guid);

                System.out.println(
                        "LOGIN SUCCESS: " + username);

            } else {

                out.println(
                        "LOGIN_FAILED:Invalid credentials");

                System.out.println(
                        "LOGIN FAILED: " + username);
            }
        }

        

        private void handleRegister(String data) {

            String[] parts = data.split(":", 2);

            if (parts.length != 2) {

                out.println("REGISTER_FAILED:Invalid format");

                return;
            }

            String username = parts[0].trim();

            String password = parts[1].trim();

            if (username.isEmpty() || password.isEmpty()) {

                out.println("REGISTER_FAILED:Empty fields");

                return;
            }

            boolean success = DatabaseUtil.registerUser(
                    username,
                    password);

            if (success) {

                String guid = DatabaseUtil.getUserGUID(username);

                this.clientGuid = guid;

                this.username = username;

                clients.put(guid, this);

                out.println(
                        "REGISTER_SUCCESS:" + guid);

                System.out.println(
                        "NEW USER REGISTERED: "
                                + username);

            } else {

                out.println(
                        "REGISTER_FAILED:Registration failed");

                System.out.println(
                        "REGISTER FAILED: "
                                + username);
            }
        }

        
        private void handleChatMessage(String data) {

            int colonIndex = data.indexOf(':');

            if (colonIndex <= 0) {

                return;
            }

            String targetGuid = data.substring(0, colonIndex);

            String content = data.substring(colonIndex + 1);

             
            DatabaseUtil.saveMessage(
                    clientGuid,
                    targetGuid,
                    content,
                    null,
                    "text");

            
            sendToClient(
                    targetGuid,
                    "MSG:"
                            + clientGuid
                            + ":"
                            + content);

             
            out.println(
                    "MSG_SENT:" + targetGuid);

            System.out.println(
                    "MESSAGE SENT FROM "
                            + username);
        }

         
        private void handleImageMessage(String data) {

            int colonIndex = data.indexOf(':');

            if (colonIndex <= 0) {

                return;
            }

            try {

                String targetGuid = data.substring(0, colonIndex);

                String imageData = data.substring(colonIndex + 1);

                byte[] imageBytes = Base64.getDecoder()
                        .decode(imageData);

                 
                DatabaseUtil.saveMessage(
                        clientGuid,
                        targetGuid,
                        "[IMAGE]",
                        imageBytes,
                        "image");

                
                sendToClient(
                        targetGuid,
                        "IMAGE:"
                                + clientGuid
                                + ":"
                                + imageData);

                out.println(
                        "IMAGE_SENT:" + targetGuid);

                System.out.println(
                        "IMAGE SENT FROM "
                                + username);

            } catch (Exception e) {

                System.err.println(
                        "IMAGE ERROR:");

                e.printStackTrace();
            }
        }

        

        private void handleGetUsers(String data) {

            if (data == null || data.isEmpty()) {

                return;
            }

            List<String[]> users = DatabaseUtil.getAllUsers(data);

            StringBuilder response = new StringBuilder("USERS:");

            for (String[] user : users) {

                response.append(user[0])
                        .append(",")
                        .append(user[1])
                        .append(";");
            }

            out.println(response.toString());
        }

         

        public void sendMessage(String message) {

            out.println(message);
        }

        

        private void disconnect() {

            try {

                if (!clientGuid.isEmpty()) {

                    clients.remove(clientGuid);

                    System.out.println(
                            "CLIENT DISCONNECTED: "
                                    + username);
                }

                if (socket != null &&
                        !socket.isClosed()) {

                    socket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}