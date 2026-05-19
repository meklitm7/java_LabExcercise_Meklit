import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("--- Custom Poker Game ---");

    System.out.print("Enter Player 1 name: ");
    String player1Name = scanner.nextLine();
    System.out.print("Enter Player 2 name: ");
    String player2Name = scanner.nextLine();

    PokerGame game = new PokerGame(player1Name, player2Name);
    game.play();

    System.out.print("\nPlay again? (yes/no): ");
    String replay = scanner.nextLine().trim().toLowerCase();
    if (replay.equals("yes")) {
      main(args);
    } else {
      System.out.println("Thanks for playing!");
    }
  }
}