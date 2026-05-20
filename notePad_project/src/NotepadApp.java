import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

public class NotepadApp extends Application {

  private TextArea textArea;
  private Label statusLabel;
  private Stage primaryStage;

  private WordStats wordStats;
  private ThemeManager.Theme currentTheme = ThemeManager.Theme.LIGHT;
  private String currentFileName = "Untitled";
  private boolean isModified = false;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

    textArea = new TextArea();
    textArea.setWrapText(true);
    textArea.setFont(Font.font("Arial", 14));

    statusLabel = new Label("Words: 0 | Sentences: 0 | Reading time: 0 sec");
    statusLabel.setPadding(new Insets(5, 10, 5, 10));

    wordStats = new WordStats(statusLabel);

    setupTextAreaListeners();

    BorderPane root = new BorderPane();
    root.setTop(createMenuBar());
    root.setCenter(textArea);
    root.setBottom(createStatusBar());

    Scene scene = new Scene(root, 900, 600);
    ThemeManager.applyTheme(currentTheme, scene, textArea);

    primaryStage.setTitle("Smart Notepad - " + currentFileName);

    try {
      Image appIcon = new Image(
          new File("C:\\Users\\Hp-NoteBook\\OneDrive\\Desktop\\notePad_project\\resource\\note.png").toURI()
              .toString());
      primaryStage.getIcons().add(appIcon);
    } catch (Exception e) {
      System.err.println("Error loading app icon: " + e.getMessage());
    }

    primaryStage.setScene(scene);

    primaryStage.setOnCloseRequest(e -> {
      if (!confirmClose()) {
        e.consume();
      }
    });

    primaryStage.show();
  }

  private void setupTextAreaListeners() {
    textArea.textProperty().addListener((obs, oldText, newText) -> {
      isModified = true;
      updateTitle();
      wordStats.updateStats(newText);
    });
  }

  private MenuBar createMenuBar() {
    MenuBar menuBar = new MenuBar();

    // FILE MENU
    Menu fileMenu = new Menu("File");
    MenuItem newItem = new MenuItem("New");
    newItem.setOnAction(e -> newFile());
    MenuItem openItem = new MenuItem("Open...");
    openItem.setOnAction(e -> openFile());
    MenuItem saveItem = new MenuItem("Save");
    saveItem.setOnAction(e -> saveFile());
    MenuItem saveAsItem = new MenuItem("Save As...");
    saveAsItem.setOnAction(e -> saveFileAs());
    MenuItem exitItem = new MenuItem("Exit");
    exitItem.setOnAction(e -> {
      if (confirmClose()) {
        Platform.exit();
      }
    });
    fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, exitItem);

    // EDIT MENU
    Menu editMenu = new Menu("Edit");
    MenuItem cutItem = new MenuItem("Cut");
    cutItem.setOnAction(e -> textArea.cut());
    MenuItem copyItem = new MenuItem("Copy");
    copyItem.setOnAction(e -> textArea.copy());
    MenuItem pasteItem = new MenuItem("Paste");
    pasteItem.setOnAction(e -> textArea.paste());
    MenuItem selectAllItem = new MenuItem("Select All");
    selectAllItem.setOnAction(e -> textArea.selectAll());
    editMenu.getItems().addAll(cutItem, copyItem, pasteItem, selectAllItem);

    // FORMAT MENU
    Menu formatMenu = new Menu("Format");
    MenuItem boldItem = new MenuItem("Bold");
    boldItem.setOnAction(e -> toggleBold());
    MenuItem italicItem = new MenuItem("Italic");
    italicItem.setOnAction(e -> toggleItalic());
    MenuItem colorItem = new MenuItem("Text Color");
    colorItem.setOnAction(e -> showTextColorPicker());

    // Font Size Submenu
    Menu fontSizeMenu = new Menu("Font Size");
    int[] fontSizes = { 8, 10, 12, 14, 16, 18, 20 };
    for (int size : fontSizes) {
      MenuItem sizeItem = new MenuItem(String.valueOf(size));
      sizeItem.setOnAction(e -> textArea.setFont(Font.font("Arial", size)));
      fontSizeMenu.getItems().add(sizeItem);
    }
    formatMenu.getItems().addAll(boldItem, italicItem, colorItem, fontSizeMenu);

    // LIST MENU
    Menu listMenu = new Menu("List");
    MenuItem bullet = new MenuItem("Bullet");
    bullet.setOnAction(e -> insertBullet());
    listMenu.getItems().add(bullet);

    // VIEW MENU
    Menu viewMenu = new Menu("View");
    ToggleGroup group = new ToggleGroup();
    for (ThemeManager.Theme theme : ThemeManager.Theme.values()) {
      RadioMenuItem item = new RadioMenuItem(theme.getDisplayName());
      item.setToggleGroup(group);
      item.setSelected(theme == currentTheme);
      item.setOnAction(e -> {
        currentTheme = theme;
        ThemeManager.applyTheme(currentTheme, primaryStage.getScene(), textArea);
      });
      viewMenu.getItems().add(item);
    }

    menuBar.getMenus().addAll(fileMenu, editMenu, formatMenu, listMenu, viewMenu);
    return menuBar;
  }

  private HBox createStatusBar() {
    HBox bar = new HBox(statusLabel);
    bar.setAlignment(Pos.CENTER_LEFT);
    bar.setStyle("-fx-background-color:#e0e0e0;");
    return bar;
  }

  // FILE METHODS
  private void newFile() {
    if (!confirmClose())
      return;
    textArea.clear();
    currentFileName = "Untitled";
    isModified = false;
    updateTitle();
  }

  private void openFile() {
    FileChooser fc = new FileChooser();
    File file = fc.showOpenDialog(primaryStage);
    if (file != null) {
      try {
        textArea.setText(new String(Files.readAllBytes(file.toPath())));
        currentFileName = file.getName();
        isModified = false;
        updateTitle();
      } catch (IOException ex) {
        showError("Error", ex.getMessage());
      }
    }
  }

  private void saveFile() {
    if (currentFileName.equals("Untitled")) {
      saveFileAs();
    } else {
      try (PrintWriter pw = new PrintWriter(
          new File("C:\\Users\\Hp-NoteBook\\OneDrive\\Desktop\\notePad_project\\" + currentFileName))) {
        pw.print(textArea.getText());
        isModified = false;
        updateTitle();
      } catch (IOException ex) {
        showError("Error", ex.getMessage());
      }
    }
  }

  private void saveFileAs() {
    FileChooser fc = new FileChooser();
    File file = fc.showSaveDialog(primaryStage);
    if (file != null) {
      try (PrintWriter pw = new PrintWriter(file)) {
        pw.print(textArea.getText());
        currentFileName = file.getName();
        isModified = false;
        updateTitle();
      } catch (IOException ex) {
        showError("Error", ex.getMessage());
      }
    }
  }

  private boolean confirmClose() {
    if (!isModified)
      return true;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setContentText("Save changes?");
    ButtonType save = new ButtonType("Save");
    ButtonType cancel = new ButtonType("Cancel");
    ButtonType no = new ButtonType("No");
    alert.getButtonTypes().setAll(save, no, cancel);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == save) {
      saveFile();
      return true;
    }
    return result.isPresent() && result.get() == no;
  }

  private void updateTitle() {
    primaryStage.setTitle("Smart Notepad - " + currentFileName + (isModified ? "*" : ""));
  }

  // FORMAT FEATURES
  private void toggleBold() {
    Font f = textArea.getFont();
    textArea.setFont(Font.font(f.getName(),
        f.getStyle().contains("Bold") ? FontWeight.NORMAL : FontWeight.BOLD, f.getSize()));
  }

  private void toggleItalic() {
    Font f = textArea.getFont();
    textArea.setFont(Font.font(f.getName(),
        f.getStyle().contains("Italic") ? FontPosture.REGULAR : FontPosture.ITALIC, f.getSize()));
  }

  private void showTextColorPicker() {
    Dialog<Color> dialog = new Dialog<>();
    dialog.setTitle("Text Color");
    dialog.setHeaderText("Choose Text Color");

    ColorPicker cp = new ColorPicker();
    cp.setValue(Color.BLACK);

    ButtonType applyBtn = new ButtonType("Apply");
    ButtonType cancelBtn = new ButtonType("Cancel");
    dialog.getDialogPane().getButtonTypes().addAll(applyBtn, cancelBtn);

    VBox vbox = new VBox(10);
    vbox.getChildren().add(new Label("Select a color:"));
    vbox.getChildren().add(cp);
    dialog.getDialogPane().setContent(vbox);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == applyBtn)
        return cp.getValue();
      return null;
    });

    Optional<Color> result = dialog.showAndWait();
    result.ifPresent(color -> {
      String rgb = String.format("rgb(%d,%d,%d)",
          (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
      textArea.setStyle("-fx-text-fill:" + rgb + ";");
    });
  }

  private void insertBullet() {
    textArea.insertText(textArea.getCaretPosition(), "\n• ");
  }

  private void showError(String t, String m) {
    Alert a = new Alert(Alert.AlertType.ERROR);
    a.setTitle(t);
    a.setContentText(m);
    a.showAndWait();
  }
}