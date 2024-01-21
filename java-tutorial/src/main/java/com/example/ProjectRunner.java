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

                EuchreHand hand = new EuchreHand(4, keyboard);
                isPlaying = hand.getPlayer().act();
            }
        }
    }
}
