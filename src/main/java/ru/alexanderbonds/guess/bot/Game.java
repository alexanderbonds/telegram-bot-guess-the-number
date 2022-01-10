package ru.alexanderbonds.guess.bot;

import java.util.Random;

public class Game {
    private final int RANDOM_BOUND = 100;
    private final int numberToGuess;
    private boolean alive;
    private int attempts;

    public Game() {
        numberToGuess = new Random().nextInt(RANDOM_BOUND);
        alive = true;
    }

    public int guess(int number) {
        if (number == numberToGuess) {
            alive = false;
            return 0;
        } else if (number < numberToGuess) {
            attempts++;
            return -1;
        } else {
            attempts++;
            return 1;
        }
    }

    public int getNumberToGuess() {
        return numberToGuess;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getAttempts() {
        return attempts;
    }
}
