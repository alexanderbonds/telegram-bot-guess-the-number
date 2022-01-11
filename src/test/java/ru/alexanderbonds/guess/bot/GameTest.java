package ru.alexanderbonds.guess.bot;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameTest {

    @Test
    void guess_lowerNumber_mustReturnMinusOne() {
        // Config
        Game game = new Game();
        int numberToGuess = game.getNumberToGuess();
        int lowerNumber = new Random().nextInt(numberToGuess);

        // Call
        int tryGuess = game.guess(lowerNumber);

        // Verify
        assertEquals(-1, tryGuess);
    }

    @Test
    void guess_higherNumber_mustReturnPlusOne() {
        // Config
        Game game = new Game();
        int numberToGuess = game.getNumberToGuess();
        int higherNumber = numberToGuess + new Random().nextInt(numberToGuess);

        // Call
        int tryGuess = game.guess(higherNumber);

        // Verify
        assertEquals(1, tryGuess);
    }

    @Test
    void guess_exactNumber_mustReturnZero() {
        // Config
        Game game = new Game();
        int exactNumber = game.getNumberToGuess();

        // Call
        int tryGuess = game.guess(exactNumber);

        // Verify
        assertEquals(0, tryGuess);
    }

    @Test
    void guess_wrongNumber_mustIncrementAttemptsByOne() {
        // Config
        Game game = new Game();
        int wrongNumber = game.getNumberToGuess() + 1;
        int attempts = game.getAttempts();

        // Call
        game.guess(wrongNumber);

        // Verify
        assertEquals(attempts + 1, game.getAttempts());
    }

    @Test
    void guess_wrongNumber_mustKeepGameAlive() {
        // Config
        Game game = new Game();
        int wrongNumber = game.getNumberToGuess() + 1;

        // Call
        game.guess(wrongNumber);

        // Verify
        assertTrue(game.isAlive());
    }

    @Test
    void guess_exactNumber_mustTerminateGame() {
        // Config
        Game game = new Game();
        int exactNumber = game.getNumberToGuess();

        // Call
        game.guess(exactNumber);

        // Verify
        assertFalse(game.isAlive());
    }
}