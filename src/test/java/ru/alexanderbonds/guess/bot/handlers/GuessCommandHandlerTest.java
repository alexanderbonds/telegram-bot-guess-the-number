package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.Game;
import ru.alexanderbonds.guess.bot.factories.GameFactory;
import ru.alexanderbonds.guess.bot.factories.MessageFactory;
import ru.alexanderbonds.guess.bot.decorators.MessageWithCustomTextDecorator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GuessCommandHandlerTest {

    @Test
    @DisplayName("handle() with no active games should return warning")
    void handle_noActiveGame_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message message = MessageFactory.getMessage();
        final String expected = "You have no games running.";

        // Call
        final BaseRequest request = handler.handle(message, new HashMap<>(), new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with incorrect number should return warning")
    void handle_wrongNumber_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                "/guess " + Integer.MAX_VALUE
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Please enter valid number from 1 to 100!";

        games.put(message.from().id(), GameFactory.getGame());

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() without number should return warning")
    void handle_noNumber_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                "/guess"
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Please enter number to guess!";

        games.put(message.from().id(), GameFactory.getGame());

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with text instead of number should return warning")
    void handle_textInsteadOfNumber_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                "/guess text_instead_of_number"
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Please enter valid number from 1 to 100!";

        games.put(message.from().id(), GameFactory.getGame());

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with lower number should return Try higher")
    void handle_lowerNumber_shouldReturnTryHigher() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Game game = GameFactory.getGame();
        final int lowerNumber = game.getNumberToGuess() - 1;
        final Message message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                "/guess " + lowerNumber
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Try higher!";

        games.put(message.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with higher number should return Try lower")
    void handle_higherNumber_shouldReturnTryLower() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Game game = GameFactory.getGame();
        final int higherNumber = game.getNumberToGuess() + 1;
        final Message message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                "/guess " + higherNumber
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Try lower!";

        games.put(message.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with exact number should stop active game and return You won")
    void handle_exactNumber_shouldStopActiveGameAndUpdateStatisticAndReturnYouWon() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Game game = GameFactory.getGame();
        final int exactNumber = game.getNumberToGuess();
        final Message message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                "/guess " + exactNumber
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final Map<Long, Map<LocalDateTime, Integer>> stat = new ConcurrentHashMap<>();
        final String expected = String.format(
                "You won after %d attempts! Want to /start one more game?",
                game.getAttempts() + 1
        );

        games.put(message.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(message, games, stat);

        // Verify
        assertAll(
                () -> assertEquals(expected, request.getParameters().get("text")),
                () -> assertFalse(games.containsKey(message.from().id())),
                () -> assertTrue(stat.containsKey(message.from().id())),
                () -> assertEquals(1, stat.get(message.from().id()).size())
        );
    }

    @Test
    @DisplayName("getCommand() should return /guess")
    void getCommand_noArguments_shouldReturnHelpCommand() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final String expected = "/guess";

        // Call
        final String command = handler.getCommand();

        // Verify
        assertEquals(expected, command);
    }
}
