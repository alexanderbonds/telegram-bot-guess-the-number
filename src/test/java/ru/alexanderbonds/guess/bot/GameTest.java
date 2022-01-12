package ru.alexanderbonds.guess.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameTest {

    @Test
    @DisplayName("guess() with lower number must return -1")
    void guess_lowerNumber_mustReturnMinusOne() {
        // Config
        Game game = new Game();
        int numberToGuess = game.getNumberToGuess();
        int lowerNumber = numberToGuess - 1;

        // Call
        int tryGuess = game.guess(lowerNumber);

        // Verify
        assertEquals(-1, tryGuess);
    }

    @Test
    @DisplayName("guess() with higher number must return 1")
    void guess_higherNumber_mustReturnPlusOne() {
        // Config
        Game game = new Game();
        int numberToGuess = game.getNumberToGuess();
        int higherNumber = numberToGuess + 1;

        // Call
        int tryGuess = game.guess(higherNumber);

        // Verify
        assertEquals(1, tryGuess);
    }

    @Test
    @DisplayName("guess() with exact number must return 0")
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
    @DisplayName("guess() with wrong number must increase attempts count by 1")
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
    @DisplayName("guess() with wrong number must keep game alive")
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
    @DisplayName("guess() with exact number must terminate game")
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