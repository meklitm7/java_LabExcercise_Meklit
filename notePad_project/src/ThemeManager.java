import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class ThemeManager {

  public enum Theme {
    LIGHT("Light"),
    DARK("Dark"),
    BLUE("Blue"),
    GREEN("Green");

    private final String displayName;

    Theme(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }

  public static void applyTheme(Theme theme, Scene scene, TextArea textArea) {

    // 🔧 FIX 1: DO NOT accumulate old styles (this was your bug)
    // We fully reset style instead of appending

    String textColor = "-fx-text-fill: black;"; // default safe color

    switch (theme) {

      case DARK:
        scene.getRoot().setStyle("-fx-background-color: #2b2b2b;");

        textArea.setStyle(
            "-fx-control-inner-background: #1e1e1e;" +
            "-fx-highlight-fill: #264f78;" +
            "-fx-text-fill: #d4d4d4;" // FIX: light text for dark mode
        );
        break;

      case BLUE:
        scene.getRoot().setStyle("-fx-background-color: #1a237e;");

        textArea.setStyle(
            "-fx-control-inner-background: #0d1642;" +
            "-fx-highlight-fill: #3949ab;" +
            "-fx-text-fill: #e8eaf6;" // FIX
        );
        break;

      case GREEN:
        scene.getRoot().setStyle("-fx-background-color: #1b5e20;");

        textArea.setStyle(
            "-fx-control-inner-background: #0d3b11;" +
            "-fx-highlight-fill: #2e7d32;" +
            "-fx-text-fill: #e8f5e9;" // FIX
        );
        break;

      case LIGHT:
      default:
        scene.getRoot().setStyle("-fx-background-color: #f5f5f5;");

        textArea.setStyle(
            "-fx-control-inner-background: #ffffff;" +
            "-fx-highlight-fill: #3399ff;" +
            "-fx-text-fill: #000000;" // FIX
        );
        break;
    }
  }
}