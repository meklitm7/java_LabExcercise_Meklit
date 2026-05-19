# 🎲 Custom Poker Game

A simple **2-player poker game** where players take turns swapping cards with the community to form the strongest hand.

---

## 📜 **Game Rules**

### **Setup**
1. Each player is dealt **6 private cards**.
2. **6 community cards** are placed face-up on the table.
3. Players take turns to **swap cards** between their hand and the community.

---

### **Gameplay**
1. **Player 1's Turn**:
   - Pick **1 card from the 6 community cards**.
   - Replace **1 card from your hand** with the selected community card.
   - The **replaced card goes back to the community** (keeping it at 6 cards).

2. **Player 2's Turn**:
   - Same as Player 1: pick a community card and replace one from your hand.

3. **Winner Determination**:
   - After both players finish their turns, the game **counts the number of pairs** in each player's hand.
   - The player with the **most pairs wins**!
   - If both have the same number of pairs, it's a **tie**.

---

### **What Counts as a Pair?**
- **Pair**: 2 cards of the same rank (e.g., two Aces).
- **Three of a Kind**: 3 cards of the same rank → counts as **1 pair** .
- **Four of a Kind**: 4 cards of the same rank → counts as **2 pair** .

> Example:
> - Hand: `[AS, AH, KH, KD, QD, 10S]` → **2 pairs** (Aces and Kings).
> - Hand: `[AS, AH, AD, KH, QD, 10S]` → **1 pair** (Three Aces count as 1 pair).

---

### **How the Winner is Decided**
1. The game counts the **number of pairs** in each player's final hand.
2. The player with the **highest number of pairs wins**.
3. If both players have the **same number of pairs**, the game is a **tie**.

---

 