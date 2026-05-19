import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokerGame {
  private final Deck deck;
  private final Player player1;
  private final Player player2;
  private final List<Card> communityCards;
  private final Scanner scanner;

  public PokerGame(String player1Name, String player2Name) {
    this.deck = new Deck();
    this.player1 = new Player(player1Name);
    this.player2 = new Player(player2Name);
    this.communityCards = new ArrayList<>();
    this.scanner = new Scanner(System.in);
  }

  public void dealInitialCards() {
    // Deal 6 private cards to each player
    for (int i = 0; i < 6; i++) {
      player1.addCard(deck.dealCard());
      player2.addCard(deck.dealCard());
    }

    // Deal 6 community cards
    for (int i = 0; i < 6; i++) {
      communityCards.add(deck.dealCard());
    }
  }

  public void playTurn(Player player) {
    System.out.println("\n" + player.getName() + "'s turn:");
    System.out.println("Your hand: " + player.getHand());
    System.out.println("Community cards: " + communityCards);

    // Choose a community card
    System.out.print("Pick a community card (0-5): ");
    int communityIndex = scanner.nextInt();
    Card selectedCommunityCard = communityCards.get(communityIndex);

    // Choose a card from hand to replace
    System.out.print("Pick a card from your hand to replace (0-5): ");
    int handIndex = scanner.nextInt();
    Card replacedCard = player.getHand().get(handIndex);

    // Replace the card in player's hand
    player.replaceCard(handIndex, selectedCommunityCard);

    // Return the replaced card to the community
    communityCards.set(communityIndex, replacedCard);
  }

  public void play() {
    dealInitialCards();

    // Player 1's turn
    playTurn(player1);

    // Player 2's turn
    playTurn(player2);

    // Evaluate final hands
    int player1Pairs = HandEvaluator.countPairs(player1.getHand());
    int player2Pairs = HandEvaluator.countPairs(player2.getHand());

    System.out.println("\n--- Final Results ---");
    System.out.println(player1.getName() + "'s hand: " + player1.getHand());
    System.out.println(player2.getName() + "'s hand: " + player2.getHand());

    System.out.println("\nPairs:");
    System.out.println(player1.getName() + ": " + player1Pairs + " pairs");
    System.out.println(player2.getName() + ": " + player2Pairs + " pairs");

    // Determine the winner
    if (player1Pairs > player2Pairs) {
      System.out.println("\nWinner: " + player1.getName());
    } else if (player1Pairs < player2Pairs) {
      System.out.println("\nWinner: " + player2.getName());
    } else {
      System.out.println("\nIt's a tie!");
    }
  }
}