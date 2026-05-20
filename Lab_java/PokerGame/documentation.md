# Custom Poker Game Documentation

---

## 1. Overview

The **Custom Poker Game** is a **2-player turn-based card game** implemented in Java. Players take turns swapping cards between their private hands and a shared pool of community cards. The objective is to build the strongest hand by maximizing the number of **pairs based on matching card ranks**.

The game evaluates hands by counting cards with the same rank:

- **Pair**: Two cards of the same rank (e.g., two Aces)
- **Three of a Kind**: Counts as one pair
- **Four of a Kind**: Counts as two pairs

At the end of the game, the player with the highest pair count wins.

---

## 2. Objective

The main objective of the game is to:

- Collect as many **pairs** as possible.
- Strategically swap cards with the community pool.
- Build a stronger hand than the opponent.
- Win by having the highest pair count.

---

## 3. Significance

This project demonstrates several important programming concepts.

### Object-Oriented Programming (OOP)

- Classes and objects
- Encapsulation
- Methods and constructors
- Modular design

### Game Development Concepts

- Turn-based gameplay
- Card swapping mechanics
- Winner determination

### User Interaction

- Console-based input and output
- Player decision-making

### Software Design

- Separation of responsibilities across classes

### Educational Value

This project is useful for learning:

- Java programming
- Card game logic
- Object-oriented design
- Basic software architecture

---

## 4. Problem Statement

The game addresses several programming and design challenges.

### Card Management

- Represent cards with rank and suit.
- Create a standard 52-card deck.
- Shuffle and deal cards.

### Player Interaction

- Allow players to select cards to swap.
- Validate user input.

### Hand Evaluation

- Count pairs and related combinations.

### Game Flow

- Manage turns.
- Keep the community at exactly six cards.
- Determine the winner.

---

## 5. Flowchart

The game follows this logical sequence:

1. Start the game.
2. Create and shuffle a deck.
3. Deal six cards to each player.
4. Deal six community cards.
5. Player 1 swaps one card.
6. Player 2 swaps one card.
7. Evaluate both hands.
8. Determine the winner.
9. Ask whether players want to play again.
10. Restart or exit.

 
---

## 6. Implementation

### Scope

The game is a **console-based Java application** with the following characteristics:

- Supports exactly **2 players**
- Uses a standard **52-card deck**
- Deals **6 private cards** to each player
- Provides **6 community cards**
- Uses **pair counting** as the winning condition
- Supports replay after each game

### Key Features

| Feature | Description |
|------|------|
| Deck Management | Creates, shuffles, and deals a 52-card deck |
| Player Hands | Each player receives 6 private cards |
| Community Cards | 6 shared face-up cards |
| Card Swapping | Swap one hand card with one community card |
| Pair Counting | Counts pairs and related combinations |
| Turn-Based Gameplay | Players alternate turns |
| Replay Option | Allows players to start a new game |

---

## 7. High-Level Description

### Game Setup

1. A standard deck of 52 cards is created.
2. The deck is shuffled.
3. Each player receives 6 cards.
4. Six cards are placed in the community pool.

### Gameplay

#### Player 1 Turn

- Select one community card.
- Select one card from the hand.
- Swap the two cards.

#### Player 2 Turn

- Repeat the same process.

### Winner Determination

- Count the number of pairs in each hand.
- Compare the results.
- Declare the winner or a tie.

### Replay

- Ask players whether they want to play again.

---

### Class Responsibilities

| Class | Responsibility |
|------|------|
| `Card` | Represents a single playing card |
| `Deck` | Creates, shuffles, and deals cards |
| `Player` | Stores player name and hand |
| `HandEvaluator` | Counts pairs in a hand |
| `PokerGame` | Controls the game flow |
| `Main` | Program entry point and replay logic |

---

## 8. Code Explanation

---

### 8.1 Card.java

#### Purpose

Represents a single playing card.

#### Attributes

- `rank` (A, K, Q, J, 10, etc.)
- `suit` (Spades, Hearts, Diamonds, Clubs)

#### Key Methods

| Method | Description |
|------|------|
| `getRank()` | Returns the card rank |
| `getSuit()` | Returns the card suit |
| `toString()` | Returns a formatted card value such as `AS` |

---

### 8.2 Deck.java

#### Purpose

Creates and manages a standard 52-card deck.

#### Key Methods

| Method | Description |
|------|------|
| `Deck()` | Initializes and shuffles the deck |
| `dealCard()` | Removes and returns the top card |

---

### 8.3 Player.java

#### Purpose

Represents a player and stores their hand.

#### Key Methods

| Method | Description |
|------|------|
| `addCard(Card card)` | Adds a card to the hand |
| `replaceCard(int index, Card newCard)` | Replaces a card in the hand |
| `getHand()` | Returns the player's hand |
| `getName()` | Returns the player's name |

---

### 8.4 HandEvaluator.java

#### Purpose

Evaluates a hand by counting matching card ranks.

#### Key Method

| Method | Description |
|------|------|
| `countPairs(List<Card> hand)` | Counts the number of pairs |

#### Pair Counting Rules

| Combination | Pair Count |
|------|------|
| One Pair | 1 |
| Three of a Kind | 1 |
| Four of a Kind | 2 |

---

### 8.5 PokerGame.java

#### Purpose

Controls the complete game process.

#### Key Methods

| Method | Description |
|------|------|
| `dealInitialCards()` | Deals cards to players and community |
| `playTurn(Player player)` | Handles one player's swap |
| `displayHands()` | Shows cards on the console |
| `determineWinner()` | Evaluates and compares hands |
| `play()` | Runs the full game |

---

### 8.6 Main.java

#### Purpose

Serves as the entry point of the application.

#### Key Method

| Method | Description |
|------|------|
| `main(String[] args)` | Starts the game and manages replay |

---

## 9. Game Rules

### Setup

1. Each player receives 6 private cards.
2. Six community cards are placed face-up.
3. Players take turns swapping cards.

### Gameplay

#### Player Turn

1. Select one community card.
2. Select one hand card.
3. Swap the selected cards.

### What Counts as a Pair?

- **Pair**: Two cards of the same rank.
- **Three of a Kind**: Counts as one pair.
- **Four of a Kind**: Counts as two pairs.

#### Examples

| Hand | Result |
|------|------|
| `[AS, AH, KH, KD, QD, 10S]` | 2 pairs |
| `[AS, AH, AD, KH, QD, 10S]` | 1 pair |
| `[AS, AH, AD, AC, KH, QD]` | 2 pairs |

### Winner Determination

- Highest number of pairs wins.
- Equal pair counts result in a tie.

---

## 10. Conclusion

The **Custom Poker Game** is a simple but effective demonstration of object-oriented programming in Java. It combines modular design, user interaction, and game logic to create an engaging two-player card game.

### Achievements

- Implemented a shuffled 52-card deck
- Created a turn-based swapping system
- Evaluated hands using pair counting
- Determined the winner automatically
- Added replay functionality

### Limitations

- Console-based interface only
- Supports only two players
- Uses simplified poker rules
- No betting system

### Future Improvements

Possible enhancements include:

- Additional poker hands (Straight, Flush, Full House)
- Betting and scoring systems
- AI opponents
- Graphical user interface (GUI)
- Multiplayer support
- Save/load game functionality

### Lessons Learned

Through this project, important concepts were practiced:

- Java OOP design
- Collections and lists
- Randomization and shuffling
- Console input handling
- Modular software development
