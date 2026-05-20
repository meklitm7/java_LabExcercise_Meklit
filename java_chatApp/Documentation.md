# Chat Application Documentation

---

## 1. Overview

The **Chat Application** is a **client-server-based messaging system** built in Java. It allows multiple users to register, log in, and communicate in real time by sending text messages and images.

The application uses:

- **JavaFX** for the graphical user interface (GUI)
- **Java Sockets** for network communication
- **MySQL** for database storage

 

A batch file named `run_javafx.bat` ensures the correct JDK is used for JavaFX.

---

## 2. Objectives

The main objectives of this project are:

1. Enable **real-time communication** between users.
2. Support **multiple clients** connecting simultaneously.
3. Implement **secure user authentication** using MySQL.
4. Store users, messages, and images in a **structured database**.
5. Provide an intuitive **JavaFX GUI**.
6. Ensure **cross-JDK compatibility** using `run_javafx.bat`.

---

## 3. Significance

This project demonstrates several important software development concepts.

### Network Programming
- Uses Java Sockets for client-server communication.

### Database Management
- Integrates MySQL for persistent storage.

### GUI Development
- Uses JavaFX to build a responsive interface.

### Multi-Threading
- Handles multiple clients concurrently.

 

### Real-World Applications
This project can serve as a foundation for:

- Enterprise chat systems (Slack-like applications)
- Team collaboration tools
- Educational demonstrations of client-server architecture

---

## 4. Problem Statement

### 4.1 Core Problems Addressed

#### Real-Time Communication
Traditional messaging systems may not update instantly. This project uses Java Sockets to provide immediate message delivery.

#### Multi-User Support
Many basic chat systems support only two users. This application supports multiple clients through a central server.

#### Data Persistence
Messages and user data are permanently stored in MySQL.

#### Image Sharing
Images are transmitted by encoding them as Base64 strings.

 

---

### 4.2 Challenges and Solutions

| Challenge | Solution |
|------|------|
| Multiple JDK versions | Used `run_javafx.bat` to explicitly set the JavaFX JDK path |
| Real-time JavaFX updates | Used `Platform.runLater()` for UI updates from background threads |
| Database connection issues | Verified MySQL server status and credentials in `DatabaseUtil.java` |
| Managing multiple clients | Used `ConcurrentHashMap` and threads in `MultiClientServer.java` |
| Image transmission | Encoded images in Base64 |

---

## 5. Scope

### In Scope

- User registration and login
- Real-time text messaging
- Image sharing
- Multi-client support
- MySQL database storage
- JavaFX GUI
- JDK compatibility management

### Out of Scope

- End-to-end encryption
- Group chats
- Loading previous message history
- User profiles
- Cross-platform scripts

---

## 6. Key Features

| Feature | Description |
|------|------|
| User Registration | New users can create accounts |
| User Login | Existing users can log in |
| Real-Time Messaging | Instant text communication |
| Image Sharing | Send and receive images |
| User List | Displays all available users |
| Message History Storage | Stores messages in MySQL |
| Multi-Client Support | Supports many users simultaneously |
| JDK Compatibility | Uses `run_javafx.bat` with JDK 25 |

---

## 7. High-Level Description

### 7.1 Architecture

The application follows a **client-server architecture**.

#### Client (`App.java` and `ChatClient.java`)
- Provides the GUI
- Connects to the server
- Sends and receives messages

#### Server (`MultiClientServer.java`)
- Accepts client connections on port 5000
- Handles authentication and message routing
- Saves data to the database

#### Database (MySQL)
- Stores user accounts
- Stores messages and images

---

### 7.2 Data Flow

#### Client to Server
The client sends commands such as:

- `LOGIN:username:password`
- `REGISTER:username:password`
- `MSG:targetGuid:content`

#### Server to Database
The server uses `DatabaseUtil.java` to:

- Validate logins
- Register users
- Save messages

#### Server to Client
The server forwards messages and images to recipients.

#### Client UI Updates
The client updates the interface based on server responses.

---

### 7.3 Threading Model

#### Server
Each client connection is managed by a separate `ClientHandler` thread.

#### Client
A background thread listens for incoming messages.

#### JavaFX UI
Uses `Platform.runLater()` to safely update GUI components.

---

## 8. Code Explanation

---

### 8.1 App.java (JavaFX GUI)

#### Purpose
Provides the graphical interface and handles user interactions.

#### Main Components

| Component | Description |
|------|------|
| `primaryStage` | Main application window |
| `rootLayout` | Main `BorderPane` layout |
| `chatClient` | Handles server communication |
| `userListView` | Displays online users |
| `chatArea` | Shows text messages |
| `currentChatImage` | Displays received images |
| `MyMessageListener` | Processes server responses |

#### Key Methods

| Method | Description |
|------|------|
| `start(Stage stage)` | Initializes the application |
| `showLoginScreen()` | Displays login and registration form |
| `showMainScreen()` | Displays chat interface |
| `sendMessage()` | Sends text messages |
| `sendPhoto()` | Sends images |
| `selectUser(String)` | Selects a user to chat with |

#### Layout Structure

- **Left Panel:** Online user list
- **Center Panel:**
  - Chat header
  - Message display area
  - Image view
  - Message input and buttons
- **Bottom Panel:** Status bar showing user GUID

---

### 8.2 ChatClient.java (Client Network Handler)

#### Purpose
Manages the socket connection between client and server.

#### Main Components

| Component | Description |
|------|------|
| `SERVER_HOST` | Server hostname (`localhost`) |
| `SERVER_PORT` | Port number (`5000`) |
| `socket` | Client socket |
| `in/out` | Input and output streams |
| `myGuid` | Logged-in user's GUID |
| `listener` | Callback interface |

#### Key Methods

| Method | Description |
|------|------|
| `connect()` | Connects to the server |
| `listenForMessages()` | Reads incoming messages |
| `handleMessage()` | Processes received messages |
| `login()` | Sends login request |
| `register()` | Sends registration request |
| `sendMessage()` | Sends text message |
| `sendImage()` | Sends image |
| `getUsers()` | Requests user list |

---

### Message Protocol

| Message Type | Format | Description |
|------|------|------|
| Login | `LOGIN:username:password` | Authenticate user |
| Register | `REGISTER:username:password` | Create account |
| Text Message | `MSG:targetGuid:content` | Send text |
| Image | `IMAGE:targetGuid:base64ImageData` | Send image |
| Get Users | `GETUSERS:myGuid` | Request user list |
| Logout | `LOGOUT` | Disconnect |

---

### 8.3 DatabaseUtil.java (Database Operations)

#### Purpose
Handles all MySQL database interactions.

#### Database Configuration

| Component | Description |
|------|------|
| `DB_URL` | `jdbc:mysql://localhost:3306/chatapp` |
| `DB_USER` | MySQL username (`root`) |
| `DB_PASSWORD` | MySQL password |

#### Key Methods

| Method | Description |
|------|------|
| `getConnection()` | Opens database connection |
| `registerUser()` | Inserts a new user |
| `userExists()` | Checks if username exists |
| `validateLogin()` | Verifies credentials |
| `getUserGUID()` | Retrieves user's GUID |
| `saveMessage()` | Stores text or image |
| `getAllUsers()` | Retrieves all users except current user |
| `testConnection()` | Tests database connectivity |

---

### Database Schema

```sql
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(36) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    avatar LONGBLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_guid VARCHAR(36) NOT NULL,
    receiver_guid VARCHAR(36) NOT NULL,
    content TEXT,
    image_data LONGBLOB,
    message_type ENUM('text', 'image') DEFAULT 'text',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_guid) REFERENCES users(guid),
    FOREIGN KEY (receiver_guid) REFERENCES users(guid)
);
```

---

### 8.4 MultiClientServer.java (Server Application)

#### Purpose
The server application:

- Accepts client connections
- Handles authentication
- Routes messages
- Saves data to MySQL

#### Main Components

| Component | Description |
|------|------|
| `PORT` | Server port (`5000`) |
| `clients` | `ConcurrentHashMap` of connected users |
| `serverSocket` | Accepts incoming connections |
| `running` | Controls the server loop |

#### Key Methods

| Method | Description |
|------|------|
| `main()` | Starts the server |
| `startServer()` | Begins listening for connections |
| `stopServer()` | Stops the server |
| `sendToClient()` | Sends data to a specific user |
| `ClientHandler` | Inner class for handling a single client |

---

### ClientHandler Methods

| Method | Description |
|------|------|
| `run()` | Reads client messages |
| `handleMessage()` | Parses commands |
| `handleLogin()` | Processes login |
| `handleRegister()` | Processes registration |
| `handleChatMessage()` | Saves and forwards text messages |
| `handleImageMessage()` | Saves and forwards images |
| `handleGetUsers()` | Returns user list |
| `disconnect()` | Removes client and closes socket |

---

## 9. Conclusion

### 9.1 Summary

This Chat Application successfully demonstrates:

- Client-server architecture
- Real-time communication
- Database integration
- JavaFX GUI development
- Multi-threading
- JDK compatibility management

---

### 9.2 Achievements

- Functional chat system with registration and login
- Real-time text and image messaging
- Multi-client support
- Persistent storage using MySQL
- Resolution of JDK compatibility issues

---

### 9.3 Limitations

- No encryption (messages are plaintext)
- No group chats
- No loading of previous messages
- Windows-specific launcher script

---

### 9.4 Future Enhancements

Possible improvements include:

- End-to-end encryption (AES/RSA)
- Group chat support
- Message history loading
- User profiles and avatars
- Cross-platform launch scripts

---

### 9.5 Lessons Learned

During this project, the following skills were developed:

- Designing client-server applications
- Implementing multi-threading
- Building JavaFX GUIs
- Integrating MySQL databases
- Managing multiple JDK environments