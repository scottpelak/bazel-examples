package com.example;

import java.util.Scanner;

public class ProjectRunner {
    static boolean isPlaying = true;

    public static void main(String args[]) {
        
        try (Scanner keyboard = new Scanner(System.in)) {

            boolean isPlaying = true;

            while (isPlaying) {
                System.out.print("\033\143");
                System.out.println("Lets Play Euchre!");

                EuchreHand hand = new EuchreHand();
                hand.getPlayer().act();

                // TODO: WIP!

                System.out.println("");
                System.out.println("");
                System.out.println("Do you want to order " + hand.getTrumpCandidate().toString() + " as trump? [0] No [1] Yes");
                int isOrderingTrump = keyboard.nextInt();

                if (isOrderingTrump == 1) {
                    System.out.println("Ordering the dealer to pick up " + hand.getTrumpCandidate().toString());
                } else{
                    System.out.println("Player 1 passes");
                    System.out.println("");
                    isPlaying = false;
                }
            }

        }

    }
}
