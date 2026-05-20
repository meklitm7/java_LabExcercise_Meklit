import javafx.application.Platform;
import javafx.scene.control.Label;

public class WordStats {

    private Label statusLabel;

    public WordStats(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void updateStats(String text) {
        if (text == null) {
            text = "";
        }

        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        String readingTime = calculateReadingTime(wordCount);

        Platform.runLater(() -> {
            statusLabel.setText(String.format("Words: %d  |  Sentences: %d  |  Reading time: %s",
                    wordCount, sentenceCount, readingTime));
        });
    }

    private int countWords(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    private int countSentences(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }
        String[] sentences = text.split("[.!?]+");
        int count = 0;
        for (String sentence : sentences) {
            if (!sentence.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private String calculateReadingTime(int wordCount) {
        double minutes = wordCount / 200.0; // Average reading speed: 200 WPM
        if (minutes < 1) {
            int seconds = (int) Math.ceil(minutes * 60);
            return seconds + " sec";
        } else {
            return String.format("%.1f min", minutes);
        }
    }
}
