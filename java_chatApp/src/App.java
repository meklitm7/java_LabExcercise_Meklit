import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.collections.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
public class App extends Application {

  private Stage primaryStage;
  private BorderPane rootLayout;
  private ChatClient chatClient;
  private String myGuid = "";
  private String currentChatUser = "";
  private String currentChatUsername = "";

  // UI Components
  private TextArea chatArea;
  private TextField messageField;
  private ListView<String> userListView;
  private Button sendButton;
  private Button sendImageButton;
  private Label statusLabel;
  private ImageView currentChatImage;

  // Data
  private ObservableList<String> usersList = FXCollections.observableArrayList();
  private Map<String, String> userGuidMap = new HashMap<>();
  private Map<String, Image> receivedImages = new HashMap<>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    this.primaryStage = stage;
    stage.setTitle("Chat App ");
    try {
      Image appIcon = new Image(
          new File("C:\\Users\\Hp-NoteBook\\java_chatApp\\resource\\logo.png").toURI()
              .toString());
      primaryStage.getIcons().add(appIcon);
    } catch (Exception e) {
      System.err.println("Error loading app icon: " + e.getMessage());
    }
    stage.setWidth(800);
    stage.setHeight(600);

    
    chatClient = new ChatClient();
    chatClient.setMessageListener(new MyMessageListener());
    chatClient.connect();

    
    showLoginScreen();

    stage.show();
  }

  
  private void showLoginScreen() {
    VBox loginLayout = new VBox(20);
    loginLayout.setAlignment(Pos.CENTER);
    loginLayout.setPadding(new Insets(50));

    Label titleLabel = new Label("Welcome to Chat App");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    Label userLabel = new Label("Username:");
    TextField usernameField = new TextField();
    usernameField.setPromptText("Enter username");
    usernameField.setMaxWidth(250);

    Label passLabel = new Label("Password:");
    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Enter password");
    passwordField.setMaxWidth(250);

    Label errorLabel = new Label("");
    errorLabel.setStyle("-fx-text-fill: red;");

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);

    Button loginButton = new Button("Login");
    Button registerButton = new Button("Register");

    loginButton.setOnAction(e -> {
      String username = usernameField.getText();
      String password = passwordField.getText();

      if (username.isEmpty() || password.isEmpty()) {
        errorLabel.setText("Please enter username and password");
      } else {
        statusLabel = new Label("Logging in...");
        chatClient.login(username, password);
      }
    });

    registerButton.setOnAction(e -> {
      String username = usernameField.getText();
      String password = passwordField.getText();

      if (username.isEmpty() || password.isEmpty()) {
        errorLabel.setText("Please enter username and password");
      } else {
        statusLabel = new Label("Registering...");
        chatClient.register(username, password);
      }
    });

    buttonBox.getChildren().addAll(loginButton, registerButton);

    loginLayout.getChildren().addAll(titleLabel, userLabel, usernameField,
        passLabel, passwordField, errorLabel, buttonBox);

    Scene loginScene = new Scene(loginLayout, 400, 350);
    primaryStage.setScene(loginScene);
  }

   
  private void showMainScreen() {
    rootLayout = new BorderPane();

    // Left panel - User list
    VBox leftPanel = new VBox(10);
    leftPanel.setPadding(new Insets(10));
    leftPanel.setStyle("-fx-background-color: #f0f0f0;");

    Label usersLabel = new Label("Users");
    usersLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    userListView = new ListView<>(usersList);
    userListView.setPrefWidth(150);
    userListView.setOnMouseClicked(e -> {
      if (e.getClickCount() == 1) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
          selectUser(selected);
        }
      }
    });

    Button refreshButton = new Button("Refresh");
    refreshButton.setOnAction(e -> chatClient.getUsers());

    Button logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> {
      chatClient.logout();
      showLoginScreen();
    });

    leftPanel.getChildren().addAll(usersLabel, userListView, refreshButton, logoutButton);

    // Center panel - Chat area
    VBox centerPanel = new VBox(10);
    centerPanel.setPadding(new Insets(10));

    // Chat header
    HBox chatHeader = new HBox(10);
    Label chatWithLabel = new Label("Select a user to chat");
    chatWithLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    chatHeader.getChildren().add(chatWithLabel);

    // Chat messages area
    chatArea = new TextArea();
    chatArea.setEditable(false);
    chatArea.setWrapText(true);
    chatArea.setPrefHeight(400);

    // Image display
    currentChatImage = new ImageView();
    currentChatImage.setFitWidth(300);
    currentChatImage.setPreserveRatio(true);

    // Message input area
    HBox messageBox = new HBox(10);
    messageField = new TextField();
    messageField.setPromptText("Type a message...");
    messageField.setPrefWidth(400);
    messageField.setOnAction(e -> sendMessage());

    sendButton = new Button("Send");
    sendButton.setOnAction(e -> sendMessage());

    sendImageButton = new Button("Send Photo");
    sendImageButton.setOnAction(e -> sendPhoto());

    messageBox.getChildren().addAll(messageField, sendButton, sendImageButton);

    centerPanel.getChildren().addAll(chatHeader, chatArea, currentChatImage, messageBox);

    // Status bar at bottom
    Label statusText = new Label("Logged in as: " + myGuid);
    statusText.setPadding(new Insets(5));
    statusText.setStyle("-fx-background-color: #e0e0e0;");

    rootLayout.setLeft(leftPanel);
    rootLayout.setCenter(centerPanel);
    rootLayout.setBottom(statusText);

    Scene mainScene = new Scene(rootLayout, 800, 600);
    primaryStage.setScene(mainScene);

    
    chatClient.getUsers();
  }

   
  private void selectUser(String username) {
    currentChatUser = userGuidMap.get(username);
    currentChatUsername = username;

    chatArea.appendText("\n=== Chat with " + username + " ===\n");
    currentChatImage.setImage(null);
  }

   
  private void sendMessage() {
    if (currentChatUser.isEmpty()) {
      showAlert("Please select a user to chat with");
      return;
    }

    String message = messageField.getText();
    if (!message.isEmpty()) {
      chatClient.sendMessage(currentChatUser, message);
      chatArea.appendText("You: " + message + "\n");
      messageField.clear();
    }
  }

   
  private void sendPhoto() {
    if (currentChatUser.isEmpty()) {
      showAlert("Please select a user to chat with");
      return;
    }

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Image");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

    File file = fileChooser.showOpenDialog(primaryStage);
    if (file != null) {
      try {
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        chatClient.sendImage(currentChatUser, base64);
        chatArea.appendText("You sent an image\n");

        
        Image image = new Image(new ByteArrayInputStream(imageBytes));
        currentChatImage.setImage(image);

      } catch (IOException e) {
        showAlert("Error reading image: " + e.getMessage());
      }
    }
  }

   
  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(message);
    alert.show();
  }

   
  class MyMessageListener implements ChatClient.MessageListener {

    @Override
    public void onLoginSuccess(String guid) {
      myGuid = guid;
      Platform.runLater(() -> {
        System.out.println("Login successful: " + guid);
        showMainScreen();
      });
    }

    @Override
    public void onLoginFailed(String error) {
      Platform.runLater(() -> {
        showAlert("Login failed: " + error);
      });
    }

    @Override
    public void onRegisterSuccess(String guid) {
      myGuid = guid;
      Platform.runLater(() -> {
        System.out.println("Registration successful: " + guid);
        showAlert("Registration successful! Please login.");
        showMainScreen();
      });
    }

    @Override
    public void onRegisterFailed(String error) {
      Platform.runLater(() -> {
        showAlert("Registration failed: " + error);
      });
    }

    @Override
    public void onMessageReceived(String senderGuid, String content) {
      Platform.runLater(() -> {
        String senderName = getUsernameByGuid(senderGuid);
        chatArea.appendText(senderName + ": " + content + "\n");
      });
    }

    @Override
    public void onImageReceived(String senderGuid, String imageBase64) {
      Platform.runLater(() -> {
        try {
          byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
          Image image = new Image(new ByteArrayInputStream(imageBytes));
          currentChatImage.setImage(image);
          chatArea.appendText(getUsernameByGuid(senderGuid) + " sent an image\n");
        } catch (Exception e) {
          System.err.println("Error displaying image: " + e.getMessage());
        }
      });
    }

    @Override
    public void onMessageSent(String targetGuid) {
      Platform.runLater(() -> {
        chatArea.appendText("Message sent to " + getUsernameByGuid(targetGuid) + "\n");
      });
    }

    @Override
    public void onUsersReceived(List<String[]> users) {
      Platform.runLater(() -> {
        usersList.clear();
        userGuidMap.clear();

        for (String[] user : users) {
          String guid = user[0];
          String username = user[1];
          if (!guid.equals(myGuid)) {
            usersList.add(username);
            userGuidMap.put(username, guid);
          }
        }
      });
    }
  }

  
  private String getUsernameByGuid(String guid) {
    for (Map.Entry<String, String> entry : userGuidMap.entrySet()) {
      if (entry.getValue().equals(guid)) {
        return entry.getKey();
      }
    }
    return guid;
  }
}
