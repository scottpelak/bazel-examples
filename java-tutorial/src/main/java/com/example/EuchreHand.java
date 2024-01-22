package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EuchreHand {

    protected static Integer getPlayerIndex(Integer index) {
        return ((index - 1) % 4) + 1;
    }

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
    protected boolean isSelectingTrump;
    protected boolean isDiscardingTrump;
    protected boolean isTrumpSelected;
    protected Integer team13Tricks = 0;
    protected Integer team24Tricks = 0;
    protected Integer playerIndexCalledTrump;

    protected Suit trump;

    public EuchreHand(Integer dealerIndex, Scanner keyboard) {
        this.dealerIndex = dealerIndex;
        this.keyboard = keyboard;
        playerToAct = getPlayerIndex(dealerIndex + 1);
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

        isSelectingTrump = true;
        isDiscardingTrump = false;
        isTrumpSelected = false;

        while(getPlayer().selectTrump() == 0) {
            incrementPlayerToAct();
        }
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

    protected void incrementPlayerToAct() {
        playerToAct = getPlayerIndex(playerToAct + 1);
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
        println("│         │ Team 1/3 │ Team 2/4 │  TRUMP  │");
        println("├─────────┼──────────┼──────────┼─────────┤");
        println("│   SCORE │     7    │     8    │         │");
        println("├─────────┼──────────┼──────────┼─────────┤");
        println(String.format(
            "│  TRICKS │     %s    │     %s    │  %s │",
            team13Tricks,
            team24Tricks,
            isTrumpSelected 
                ? trump + " (P" + playerIndexCalledTrump + ")"
                : "????? "
        ));
        println("├─────────┴──────────┴──────────┴─────────┤");
    }

    protected void printFooter() {
        println("│                                         │");
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

        public boolean isDealer() {
            return EuchreHand.this.dealerIndex.equals(index);
        }

        public String getDealerPrintout() {
            return isDealer() ? DEALER_LABEL : DEALER_BLANK;
        }

        public String getName() {
            return "Player " + getIndex();
        }
        
        /**
         * 
         * @param isRightPlayer If the player is the right player which reverses order of dealer indicator
         * @return 17 character player name with dealer indicator
         * "Player 2 (Dealer)"
         * "Player 2         "
         * "(Dealer) Player 2"
         * "         Player 2"
         */
        public String getName(boolean isRightPlayer) {
            String name = "Player " + index;
            return isRightPlayer
                ? getDealerPrintout() + " " + getName()
                : getName() + " " + getDealerPrintout();
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

        public List<Card> getCardsPlayed() {
            return cardsPlayed;
        }

        public Card getLastCardPlayed() {
            return cardsPlayed.isEmpty() ? null : cardsPlayed.get(cardsPlayed.size() - 1);
        }

        public List<Card> getHand() {
            return hand;
        }

        public Card getTrumpCandidate() {
            return isDealer() ? EuchreHand.this.getTrumpCandidate() : null;
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
            





            int orderingTrump = getKeyboard().nextInt();
            
            // Demo: 
            return orderingTrump != 1;
        }

        public int selectTrump() {
            printBoard(
                getLeftPlayer().getTrumpCandidate(),
                getPartnerPlayer().getTrumpCandidate(),
                getRightPlayer().getTrumpCandidate(),
                getTrumpCandidate()
            );
            println("Order up trump? Press [0] No or [1] Yes: ");
            return getKeyboard().nextInt();
        }

        /**
         * /* FINAL
         * ╭─────────┬──────────┬──────────┬─────────╮
         * │         │ Team 1/3 │ Team 2/4 │  TRUMP  │
         * ├─────────┼──────────┼──────────┼─────────┤
         * │   SCORE │     7    │     8    │         │
         * ├─────────┼──────────┼──────────┼─────────┤
         * │  TRICKS │     1    │     2    │  ♥ (P2) │
         * ├─────────┴──────────┴──────────┴─────────┤
         * │                 Player 3 (Dealer)       │
         * │                 ╭─────╮                 │
         * │                 │  J♥ │                 │
         * │                 │     │                 │
         * │                 ╰─────╯                 │
         * │   Player 2 (Dealer) (Dealer) Player 4   │
         * │   ╭─────╮                    ╭─────╮    │
         * │   │  J♥ │                    │  J♥ │    │
         * │   │     │                    │     │    │
         * │   ╰─────╯                    ╰─────╯    │
         * │                 ╭─────╮                 │
         * │                 │  J♥ │                 │
         * │   Player 1      │     │                 │
         * │   (Dealer)      ╰─────╯                 │
         * │ ─────────────────────────────────────── │
         * │ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
         * │ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
         * │ │     │ │     │ │     │ │     │ │     │ │
         * │ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
         * │                                         │
         * ╰─────────────────────────────────────────╯
         * Order up trump? Press [0] No or [1] Yes: 
         * 
         * @param playerCard
         * @param leftPlayerCard
         * @param partnerPlayerCard
         * @param rightPlayerCard
         */
        public void printBoard(
            Card leftPlayerCard,
            Card partnerPlayerCard,
            Card rightPlayerCard,
            Card playerCard
        ) {
            Player leftPlayer = getLeftPlayer();
            Player partnerPlayer = getPartnerPlayer();
            Player rightPlayer = getRightPlayer();
            
            // Print header
            EuchreHand.this.printHeader();

            // Print partner
            println(String.format(
                PARTNER_NAME,
                partnerPlayer.getName(false)
            ));
            List<String> parterCardPrintout = getCardPrintout(partnerPlayerCard);
            for (String line : parterCardPrintout) {
                println(String.format(PARTNER_CARD, line));
            }

            // Print opponents
            println(String.format(
                OPPONENT_NAME,
                leftPlayer.getName(false),
                rightPlayer.getName(true)
            ));
            List<String> leftCardPrintout = getCardPrintout(leftPlayerCard);
            List<String> rightCardPrintout = getCardPrintout(rightPlayerCard);
            final int opponentPrintoutSize = leftCardPrintout.size();
            for (int i = 0; i < opponentPrintoutSize; i ++) {
                println(String.format(
                    OPPONENT_CARD,
                    leftCardPrintout.get(i),
                    rightCardPrintout.get(i)
                ));
            }

            // Print player
            List<String> playerCardPrintout = getCardPrintout(
                playerCard, 
                isDealer() && EuchreHand.this.isDiscardingTrump ? DEALER_CANDIDATE_INDEX : null
            );
            println(String.format(PARTNER_CARD, playerCardPrintout.get(0)));
            println(String.format(PARTNER_CARD, playerCardPrintout.get(1)));
            println(String.format(PLAYER_CARD, getName(), playerCardPrintout.get(2)));
            println(String.format(PLAYER_CARD, getDealerPrintout(), playerCardPrintout.get(3)));

            // Print player hand
            List<List<String>> cards = List.of(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            );

            final int handSize = hand.size();
            for (int i = 0 ; i < 5; i ++) {
                Integer cardIndex = EuchreHand.this.isSelectingTrump ? null : i;
                List<String> printout = i < handSize
                    ? hand.get(i).printCard(cardIndex)
                    : getBlankCard();

                cards.get(0).add(printout.get(0));
                cards.get(1).add(printout.get(1));
                cards.get(2).add(printout.get(2));
                cards.get(3).add(printout.get(3));
            }

            // Print out the hand
            println(PLAYER_SEPARATOR);
            for (int i = 0; i < cards.size(); i ++) {
                println(PLAYER_HAND_START + cards.get(i).stream().collect(Collectors.joining(" ")) + PLAYER_HAND_END);
            }

            // Print footer
            EuchreHand.this.printFooter();
        }


   
/* FINAL
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│   SCORE │     7    │     8    │         │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3 (Dealer)       │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│   Player 2 (Dealer) (Dealer) Player 4   │
│   ╭─────╮                    ╭─────╮    │
│   │  J♥ │                    │  J♥ │    │
│   │     │                    │     │    │
│   ╰─────╯                    ╰─────╯    │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│   Player 1      │     │                 │
│   (Dealer)      ╰─────╯                 │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │     │ │     │ │     │ │     │ │     │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Order up trump? Press [0] No or [1] Yes: 
 */


            
/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│   SCORE │     7    │     8    │         │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│   Player 2                   Player 4   │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│ Player 1 (Dealer)                       │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │     │ │     │ │     │ │     │ │     │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Order up trump? Pres [0] No or [1] Yes: 
 */

/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│   SCORE │     7    │     8    │         │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│   Player 2                   Player 4   │
│   ╭─────╮                    ╭─────╮    │
│   │  J♥ │                    │  J♥ │    │
│   │     │                    │     │    │
│   ╰─────╯                    ╰─────╯    │
│                                         │
│ Player 1                                │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Press the number of the card to play: 
*/
/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│                                         │
│   Player 2                   Player 4   │
│   ╭─────╮                    ╭─────╮    │
│   │  J♥ │                    │  J♥ │    │
│   │     │                    │     │    │
│   ╰─────╯                    ╰─────╯    │
│                              Dealer     │
│                                         │
│ Player 1                                │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Press the number of the card to play:  
*/
/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ?????  │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                                         │
│                                         │
│                                         │
│                                         │
│   Player 2                   Player 4   │
│                              ╭─────╮    │
│                              │  J♥ │    │
│                              ╰─────╯    │
│                              (Dealer)   │
│                                         │
│ Player 1                                │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Order up trump? Press [0] No or [1] Yes: 
*/
/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│                                         │
│   Player 2          (Dealer) Player 4   │
│   ╭─────╮                    ╭─────╮    │
│   │  J♥ │                    │  J♥ │    │
│   │     │                    │     │    │
│   ╰─────╯                    ╰─────╯    │
│                                         │
│ Player 1                                │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Press the number of the card to play: 

╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3 (Dealer)       │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│                                         │
│   Player 2 (Dealer)          Player 4   │
│   ╭─────╮                    ╭─────╮    │
│   │  J♥ │                    │  J♥ │    │
│   │     │                    │     │    │
│   ╰─────╯                    ╰─────╯    │
│                                         │
│ Player 1 (Dealer)                       │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Press the number of the card to play: 


╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3 (Dealer)       │
│                 ╭─────╮                 │
│                 │  J♥ │                 │
│                 │     │                 │
│                 ╰─────╯                 │
│                                         │
│   Player 2 (Dealer)          Player 4   │
│   ╭─────╮                    ╭─────╮    │
│   │  J♥ │                    │  J♥ │    │
│   │     │                    │     │    │
│   ╰─────╯                    ╰─────╯    │
│                                         │
│ Player 1 (Dealer)                       │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Press the number of the card to play: 


 */
/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                                         │
│                                         │
│                                         │
│                                         │
│   Player 2                   Player 4   │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│ Player 1 (Dealer) ╭─────╮               │
│                   │  J♥ │               │
│                   │     │               │
│                   ╰─────╯               │
│ ─────────────────────────────────────── │
│ Order up trump? Press [0] No or [1] Yes │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │     │ │     │ │     │ │     │ │     │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
 */
/*
╭─────────┬──────────┬──────────┬─────────╮
│         │ Team 1/3 │ Team 2/4 │  TRUMP  │
├─────────┼──────────┼──────────┼─────────┤
│  TRICKS │     1    │     2    │  ♥ (P2) │
├─────────┴──────────┴──────────┴─────────┤
│                 Player 3                │
│                                         │
│                                         │
│                                         │
│                                         │
│   Player 2                   Player 4   │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│ Player 1 (Dealer) ╭─────╮               │
│                   │  J♥ │               │
│                   │ [5] │               │
│                   ╰─────╯               │
│ ─────────────────────────────────────── │
│ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ ╭─────╮ │
│ │  A♦ │ │  Q♦ │ │  A♧ │ │  K♥ │ │  Q♥ │ │
│ │ [0] │ │ [1] │ │ [2] │ │ [3] │ │ [4] │ │
│ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ ╰─────╯ │
│                                         │
╰─────────────────────────────────────────╯
Press the number of the card to discard: 
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
         * │     │
         * ╰─────╯
         */
        public List<String> printCard() {
            return List.of(
                CARD_TOP,
                String.format(CARD_VALUE, this),
                CARD_SPACE,
                CARD_BOTTOM
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
            if (index == null) {
                return printCard();
            }
            return List.of(
                CARD_TOP,
                String.format(CARD_VALUE, this),
                String.format(CARD_INDEX, index),
                CARD_BOTTOM
            );
        }
    }

    protected static final String DEALER_LABEL = "(Dealer)";
    protected static final String DEALER_BLANK = "        ";
    protected static final String CARD_TOP    = "╭─────╮";
    protected static final String CARD_VALUE  = "│ %s │";
    protected static final String CARD_INDEX  = "│ [%s] │";
    protected static final String CARD_SPACE  = "│     │";
    protected static final String CARD_BOTTOM = "╰─────╯";
    protected static final String CARD_BLANK  = "       ";
    protected static final String PLAYER_SEPARATOR =  "│ ─────────────────────────────────────── │";
    protected static final String PARTNER_CARD =      "│                 %s                 │";
    protected static final String PARTNER_NAME =      "│                 %s       │";
    protected static final String OPPONENT_CARD =     "│   %s                    %s    │";
    protected static final String OPPONENT_NAME =     "│   %s %s   │";
    protected static final String PLAYER_CARD =       "│   %s      %s                 │";
    protected static final String PLAYER_HAND_START = "│ ";
    protected static final String PLAYER_HAND_END = " │";
    protected static final Integer DEALER_CANDIDATE_INDEX = 5;
    protected static List<String> getBlankCard() {
        return List.of(
            CARD_BLANK, 
            CARD_BLANK, 
            CARD_BLANK, 
            CARD_BLANK
        );
    }

    protected static List<String> getCardPrintout(Card card, Integer index) {
        return card == null 
            ? getBlankCard() 
            : card.printCard(index);
    }

    protected static List<String> getCardPrintout(Card card) {
        return getCardPrintout(card, null);
    }

}
