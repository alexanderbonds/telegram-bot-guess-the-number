package ru.alexanderbonds.guess.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.factories.GameFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameTest {

    @Test
    @DisplayName("guess() with lower number must return -1")
    void guess_lowerNumber_mustReturnMinusOne() {
        // Config
        final Game game = GameFactory.getGame();
        final int numberToGuess = game.getNumberToGuess();
        final int lowerNumber = numberToGuess - 1;

        // Call
        final int tryGuess = game.guess(lowerNumber);

        // Verify
        assertEquals(-1, tryGuess);
    }

    @Test
    @DisplayName("guess() with higher number must return 1")
    void guess_higherNumber_mustReturnPlusOne() {
        // Config
        final Game game = GameFactory.getGame();
        final int numberToGuess = game.getNumberToGuess();
        final int higherNumber = numberToGuess + 1;

        // Call
        final int tryGuess = game.guess(higherNumber);

        // Verify
        assertEquals(1, tryGuess);
    }

    @Test
    @DisplayName("guess() with exact number must return 0")
    void guess_exactNumber_mustReturnZero() {
        // Config
        final Game game = GameFactory.getGame();
        final int exactNumber = game.getNumberToGuess();

        // Call
        final int tryGuess = game.guess(exactNumber);

        // Verify
        assertEquals(0, tryGuess);
    }

    @Test
    @DisplayName("guess() with wrong number must increase attempts count by 1")
    void guess_wrongNumber_mustIncrementAttemptsByOne() {
        // Config
        final Game game = GameFactory.getGame();
        final int wrongNumber = game.getNumberToGuess() + 1;
        final int attempts = game.getAttempts();

        // Call
        game.guess(wrongNumber);

        // Verify
        assertEquals(attempts + 1, game.getAttempts());
    }

    @Test
    @DisplayName("guess() with wrong number must keep game alive")
    void guess_wrongNumber_mustKeepGameAlive() {
        // Config
        final Game game = GameFactory.getGame();
        final int wrongNumber = game.getNumberToGuess() + 1;

        // Call
        game.guess(wrongNumber);

        // Verify
        assertTrue(game.isAlive());
    }

    @Test
    @DisplayName("guess() with exact number must terminate game")
    void guess_exactNumber_mustTerminateGame() {
        // Config
        final Game game = GameFactory.getGame();
        final int exactNumber = game.getNumberToGuess();

        // Call
        game.guess(exactNumber);

        // Verify
        assertFalse(game.isAlive());
    }
}