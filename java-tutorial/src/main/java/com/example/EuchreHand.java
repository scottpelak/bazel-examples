package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EuchreHand {
    protected final Integer dealerIndex;
    protected final Scanner keyboard;
    protected final List<Card> deck;
    protected final Player player1;
    protected final Player player2;
    protected final Player player3;
    protected final Player player4;
    protected final Map<Integer, Player> playersByIndex;
    protected final List<Card> kiddie;
    protected Integer playerToAct;
    protected boolean isTrumpSelected;
    protected Integer team13Tricks = 0;
    protected Integer team24Tricks = 0;
    protected Integer playerIndexCalledTrump;

    protected Suit trump;

    public EuchreHand(Integer dealerIndex, Scanner keyboard) {
        this.dealerIndex = dealerIndex;
        this.keyboard = keyboard;
        playerToAct = (dealerIndex + 1) % 4;
        deck = new ArrayList<>();
        initDeck();

        kiddie = deck.subList(20, 24);
        trump = getTrumpCandidate().getSuit();

        playersByIndex = new HashMap<>();

        player1 = new Player(1, deck.subList(0, 5));
        player2 = new Player(2, deck.subList(5, 10));
        player3 = new Player(3, deck.subList(10, 15));
        player4 = new Player(4, deck.subList(15, 20));

        playersByIndex.put(player1.getIndex(), player1);
        playersByIndex.put(player2.getIndex(), player2);
        playersByIndex.put(player3.getIndex(), player3);
        playersByIndex.put(player4.getIndex(), player4);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlayer3() {
        return player3;
    }

    public Player getPlayer4() {
        return player4;
    }

    public Player getPlayer(Integer index) {
        return playersByIndex.get(index);
    }

    public Player getPlayer() {
        return getPlayer(playerToAct);
    }

    public Card getTrumpCandidate() {
        return kiddie.get(0);
    }

    protected List<Card> initDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }

        Collections.shuffle(deck);

        return deck;
    }

    public void println(String line) {
        System.out.println(line);
    }

    protected void printHeader() {
        println("╭─────────┬──────────┬──────────┬─────────╮");
        println("|         | Team 1/3 | Team 2/4 |  TRUMP  |");
        println("├─────────┼──────────┼──────────┼─────────┤");
        println("|   SCORE |     7    |     8    |         |");
        println("├─────────┼──────────┼──────────┼─────────┤");
        println(String.format(
            "|  TRICKS |     %s    |     %s    |  %s |",
            team13Tricks,
            team24Tricks,
            isTrumpSelected 
                ? trump + " (P" + playerIndexCalledTrump + ")"
                : "????? "
        ));
        println("├─────────┴──────────┴──────────┴─────────┤");
    }

    protected void printFooter() {
        println("│                                         |");
        println("╰─────────────────────────────────────────╯");
    }

    public class Player {
        final Integer index;
        final List<Card> originalHand;
        final List<Card> hand;
        final List<Card> cardsPlayed = new ArrayList<>();
        boolean orderedTrump;

        protected Player(Integer index, List<Card> hand) {
            this.index = index;
            this.originalHand = hand;
            this.hand = new ArrayList<>(hand);
            this.hand.sort(Card::compareTo);
        }

        protected Scanner getKeyboard() {
            return EuchreHand.this.keyboard;
        }

        public Integer getIndex() {
            return index;
        }

        protected Integer getPlayerIndex(Integer index) {
            return (index + 1) % 4;
        }

        public Integer getLeftPlayerIndex() {
            return getPlayerIndex(index + 1);
        }

        public Integer getPartnerPlayerIndex() {
            return getPlayerIndex(index + 2);
        }

        public Integer getRightPlayerIndex() {
            return getPlayerIndex(index + 3);
        }

        public Player getLeftPlayer() {
            return EuchreHand.this.getPlayer(getLeftPlayerIndex());
        }

        public Player getPartnerPlayer() {
            return EuchreHand.this.getPlayer(getPartnerPlayerIndex());
        }

        public Player getRightPlayer() {
            return EuchreHand.this.getPlayer(getRightPlayerIndex());
        }

        public boolean isDealer() {
            return EuchreHand.this.dealerIndex.equals(index);
        }

        public String getDealerPrintout() {
            return isDealer() ? "(Dealer)" : "        ";
        }

        public String getName() {
            return "Player " + getIndex();
        }

        public List<Card> getCardsPlayed() {
            return cardsPlayed;
        }

        public Card getLastCardPlayed() {
            return cardsPlayed.isEmpty() ? null : cardsPlayed.get(cardsPlayed.size() - 1);
        }

        public List<Card> getHand() {
            return hand;
        }

        public void println(String line) {
            EuchreHand.this.println(line);
        }

        @Override
        public String toString() {
            return hand.stream()
                .map(Card::toString)
                .collect(Collectors.joining(" "));
        }

        public boolean act() {
            EuchreHand.this.printHeader();
            printSelectTrump();
            EuchreHand.this.printFooter();

            int orderingTrump = getKeyboard().nextInt();

            if (orderingTrump == 1) {
                println("Ordering trump!");
            } else {
                println("Passing");
            }

            return false;
        }

        protected void printPartnerPlayer() {
            System.out.println(String.format(
                "│                 Player %s                |",
                getPartnerPlayer().getIndex()
            ));

            // Hard-code to choosing trump
            if (EuchreHand.this.isTrumpSelected) {

            } else {

            }
        }

        protected void printSelectTrump() {
            final List<String> candidatePrintout = isDealer()
                ? EuchreHand.this.getTrumpCandidate().printCard(5)
                : EuchreHand.this.getTrumpCandidate().printCard();

            final List<String> blankCard = getBlankCard();
            
            // Print parter player
            println(String.format(
                "│                 Player %s                |",
                getPartnerPlayer().getIndex()
            ));
            if (getPartnerPlayer().isDealer()) {
                println("│                 " + "╭─────╮" + "                 |");
                println("│                 " + candidatePrintout.get(1) + "                 |");
                println("│                 " + "╰─────╯" + "                 |");
                println("│                 " + getPartnerPlayer().getDealerPrintout() + "                |");
            } else {
                println("│                                         |");
                println("│                                         |");
                println("│                                         |");
                println("│                                         |");
            }

            // Print opponents: left/right players
            println(String.format(
                "│   Player %s                   Player %s   |",
                getLeftPlayer().getIndex(),
                getRightPlayer().getIndex()
            ));
            List<String> leftPlayerCard = 
                getLeftPlayer().isDealer()
                ? candidatePrintout
                : blankCard;
            List<String> rightPlayerCard = 
                getLeftPlayer().isDealer()
                ? candidatePrintout
                : blankCard;
            final String opponentFormat = "│   %s                    %s    |";
            for (int i = 0; i < 3; i ++) {
                println(String.format(
                    opponentFormat,
                    leftPlayerCard.get(i),
                    rightPlayerCard.get(i)
                ));                
            }
            println(String.format(
                "│   %s                   %s   │",
                getLeftPlayer().getDealerPrintout(),
                getRightPlayer().getDealerPrintout()
            ));

            // Print this player
            println(String.format(
                "│ Player %s %s                       │",
                getIndex(),
                getDealerPrintout()
            ));
            if (isDealer()) {
                // Print candidate card
                final String candidateFormat = "│                 %s                 |";
                for (int i = 0; i < 3; i ++) {
                    println(String.format(candidateFormat, candidatePrintout.get(i)));
                }
            }
            println("| ─────────────────────────────────────── │");
            println("| Order up trump? Press [0] No or [1] Yes │");
            printHand();
        }

        public void printHand() {
            List<List<String>> cards = List.of(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            );

            for (Card card : this.hand) {
                List<String> printout = card.printCard();
                cards.get(0).add(printout.get(0));
                cards.get(1).add(printout.get(1));
                cards.get(2).add(printout.get(2));
            }

            final int handSize = hand.size();
            if (handSize < 5) {
                List<String> blankCard = getBlankCard();
                for (int i = handSize; i < 5; i ++) {
                    cards.get(0).add(blankCard.get(0));
                    cards.get(1).add(blankCard.get(1));
                    cards.get(2).add(blankCard.get(2));
                }
            }
            final String prefix = "| ";
            final String suffix = " |";
            for (int i = 0; i < cards.size(); i ++) {
                println(prefix + cards.get(i).stream().collect(Collectors.joining(" ")) + suffix);
            }
        }
            
            
/*
╭─────────┬──────────┬──────────┬─────────╮
|         | Team 1/3 | Team 2/4 |  TRUMP  |
├─────────┼──────────┼──────────┼─────────┤
|   SCORE |     7    |     8    |         |
├─────────┼──────────┼──────────┼─────────┤
|  TRICKS |     1    |     2    |  ♥ (P2) |
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                |
│                                         |
│                                         |
│                                         |
│                                         |
│   Player 2                   Player 4   |
│                                         |
│                                         |
│                                         |
│                                         │
│                                         │
│ Player 1 (Dealer)                       │
│                 ╭─────╮                 |
│                 │  J♥ │                 |
│                 ╰─────╯                 |
| ─────────────────────────────────────── │
| Order up trump? Press [0] No or [1] Yes │
| ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ |
| │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ |
| ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ |
│                                         |
╰─────────────────────────────────────────╯
 */

/*
╭─────────┬──────────┬──────────┬─────────╮
|         | Team 1/3 | Team 2/4 |  TRUMP  |
├─────────┼──────────┼──────────┼─────────┤
|   SCORE |     7    |     8    |         |
├─────────┼──────────┼──────────┼─────────┤
|  TRICKS |     1    |     2    |  ♥ (P2) |
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                |
│                 ╭─────╮                 |
│                 │  J♥ │                 |
│                 ╰─────╯                 |
│                 (Dealer)                |
│   Player 2                   Player 4   |
│   ╭─────╮                    ╭─────╮    |
│   │  J♥ │                    │  J♥ │    |
│   ╰─────╯                    ╰─────╯    |
│   (Dealer)                   (Dealer)   │
│                                         │
│ Player 1                                │
| ─────────────────────────────────────── │
| Press the number of the card to play.   │
| ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ |
| │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ |
| │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ |
| ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ |
│                                         |
╰─────────────────────────────────────────╯
 */
/*
╭─────────┬──────────┬──────────┬─────────╮
|         | Team 1/3 | Team 2/4 |  TRUMP  |
├─────────┼──────────┼──────────┼─────────┤
|  TRICKS |     1    |     2    |  ♥ (P2) |
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                |
│                 ╭─────╮                 |
│                 │  J♥ │                 |
│                 ╰─────╯                 |
│                                         |
│   Player 2                   Player 4   |
│   ╭─────╮                    ╭─────╮    |
│   │  J♥ │                    │  J♥ │    |
│   ╰─────╯                    ╰─────╯    |
│                              Dealer     │
│                                         │
│ Player 1                                │
| ─────────────────────────────────────── │
| Press the number of the card to play    │
| ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ |
| │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ |
| │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ |
| ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ |
│                                         |
╰─────────────────────────────────────────╯
 */
/*
╭─────────┬──────────┬──────────┬─────────╮
|         | Team 1/3 | Team 2/4 |  TRUMP  |
├─────────┼──────────┼──────────┼─────────┤
|  TRICKS |     1    |     2    |  ?????  |
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                |
│                                         |
│                                         |
│                                         |
│                                         |
│   Player 2                   Player 4   |
│                              ╭─────╮    |
│                              │  J♥ │    |
│                              ╰─────╯    |
│                              (Dealer)   │
│                                         │
│ Player 1                                │
| ─────────────────────────────────────── │
| Order up trump? Press [0] No or [1] Yes │
| ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ |
| │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ |
| ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ |
│                                         |
╰─────────────────────────────────────────╯
 */
/*
╭─────────┬──────────┬──────────┬─────────╮
|         | Team 1/3 | Team 2/4 |  TRUMP  |
├─────────┼──────────┼──────────┼─────────┤
|  TRICKS |     1    |     2    |  ♥ (P2) |
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                |
│                                         |
│                                         |
│                                         |
│                                         |
│   Player 2                   Player 4   |
│                                         |
│                                         |
│                                         |
│                                         │
│                                         │
│ Player 1 (Dealer) ╭─────╮               │
│                   │  J♥ │               |
│                   ╰─────╯               |
| ─────────────────────────────────────── │
| Order up trump? Press [0] No or [1] Yes │
| ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ |
| │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ |
| ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ |
│                                         |
╰─────────────────────────────────────────╯
 */
/*
╭─────────┬──────────┬──────────┬─────────╮
|         | Team 1/3 | Team 2/4 |  TRUMP  |
├─────────┼──────────┼──────────┼─────────┤
|  TRICKS |     1    |     2    |  ♥ (P2) |
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                |
│                                         |
│                                         |
│                                         |
│                                         |
│   Player 2                   Player 4   |
│                                         |
│                                         |
│                                         |
│                                         │
│                                         │
│ Player 1 (Dealer) ╭─────╮               │
│                   │  J♥ │               |
|                   │ [5] │               |
│                   ╰─────╯               |
| ─────────────────────────────────────── │
| Press the number of the card to discard │
| ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ |
| │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ |
| │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ |
| ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ |
│                                         |
╰─────────────────────────────────────────╯
 */

        

    }

    public class Round {
        Card player1;
        Card player2;
        Card player3;
        Card player4;

        public Round(Integer playerToActIndex) {

        }
    }

    public class Card implements Comparable<Card> {

        protected final Suit suit;
        protected final Rank rank;

        protected Card(
            Suit suit,
            Rank rank
        ) {
            this.suit = suit;
            this.rank = rank;
        }

        public Suit getSuit() {
            return suit;
        }

        public Rank getRank() {
            return rank;
        }

        public boolean isTrump() {
            return EuchreHand.this.trump.equals(suit) || isLeftBower();
        }

        public boolean isRightBower() {
            return rank.equals(Rank.JACK) && EuchreHand.this.trump.equals(suit);
        }

        public boolean isLeftBower() {
            return rank.equals(Rank.JACK) && EuchreHand.this.trump.getOppositeSuit().equals(suit);
        }

        public Integer getEffectiveRank() {
            if (isRightBower()) {
                return 1;
            }
            if (isLeftBower()) {
                return 2;
            }
            return 10 + getRank().ordinal();
        }

        @Override
        public int hashCode() {
            // TODO: do we need this?
            return 0;
        }

        @Override
        public boolean equals(Object card) {
            return
                card instanceof Card
                && suit.equals(((Card) card).getSuit())
                && rank.equals(((Card) card).getRank());
        }

        @Override
        public int compareTo(Card card) {
            if (isTrump()) {
                if (card.isTrump()) {
                    return getEffectiveRank().compareTo(card.getEffectiveRank());
                }
                return -1;
            }
            if (card.isTrump()) {
                return 1;
            }
            if (getSuit().equals(card.getSuit())) {
                return getRank().compareTo(card.getRank());
            }
            return getSuit().compareTo(card.getSuit());
        }

        @Override
        public String toString() {
            return rank.toString() + suit.toString();       
        }

        /**
         * @return Printout of card, e.g.
         * ╭─────╮
         * │  J♥ │
         * ╰─────╯
         */
        public List<String> printCard() {
            return List.of(
                "╭─────╮",
                "│ " + toString() + " │",
                "╰─────╯"
            );
        }

        /**
         * @return Printout of card, e.g.
         * ╭─────╮
         * │  J♥ │
         * │ [1] │
         * ╰─────╯
         */
        public List<String> printCard(Integer index) {
            return List.of(
                "╭─────╮",
                "│ " + this + " │",
                "│ ["  + index + "] │",
                "╰─────╯"
            );
        }
    }

    protected List<String> getBlankCard() {
        return List.of(
            "       ",
            "       ",
            "       "
        );
    }

}
