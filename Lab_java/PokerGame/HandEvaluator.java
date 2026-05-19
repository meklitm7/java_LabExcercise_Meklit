import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.smartcardio.Card;

public class HandEvaluator {
  public static int countPairs(List<Card> hand) {
    Map<String, Integer> rankCounts = new HashMap<>();
    for (Card card : hand) {
      String rank = card.getRank();
      rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
    }

    int pairs = 0;
    for (int count : rankCounts.values()) {
      if (count == 2) {
        pairs += 1; 
      } else if (count == 3) {
        pairs += 1; 
      } else if (count == 4) {
        pairs += 2; 
      }

      else if (count == 5) {
        pairs += 2;
      } else if (count == 6) {
        pairs += 3;
      }
    }
    return pairs;
  }
}