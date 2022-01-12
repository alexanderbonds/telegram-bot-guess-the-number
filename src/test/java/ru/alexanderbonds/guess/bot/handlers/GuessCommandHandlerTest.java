package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.Game;
import ru.alexanderbonds.guess.bot.factories.GameFactory;
import ru.alexanderbonds.guess.bot.factories.MessageFactory;
import ru.alexanderbonds.guess.bot.decorators.MessageWithCustomTextDecorator;

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
        final Message dummyMessage = MessageFactory.getDummyMessage();
        final String expected = "You have no games running.";

        // Call
        final BaseRequest request = handler.handle(dummyMessage, new HashMap<>(), new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with incorrect number should return warning")
    void handle_wrongNumber_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message dummyMessage = new MessageWithCustomTextDecorator(
                MessageFactory.getDummyMessage(),
                "/guess " + Integer.MAX_VALUE
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Please enter valid number from 1 to 100!";

        games.put(dummyMessage.from().id(), GameFactory.getTestGame());

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() without number should return warning")
    void handle_noNumber_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message dummyMessage = new MessageWithCustomTextDecorator(
                MessageFactory.getDummyMessage(),
                "/guess"
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Please enter number to guess!";

        games.put(dummyMessage.from().id(), GameFactory.getTestGame());

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with text instead of number should return warning")
    void handle_textInsteadOfNumber_shouldReturnWarning() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Message dummyMessage = new MessageWithCustomTextDecorator(
                MessageFactory.getDummyMessage(),
                "/guess text_instead_of_number"
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Please enter valid number from 1 to 100!";

        games.put(dummyMessage.from().id(), GameFactory.getTestGame());

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with lower number should return Try higher")
    void handle_lowerNumber_shouldReturnTryHigher() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Game game = GameFactory.getTestGame();
        final int lowerNumber = game.getNumberToGuess() - 1;
        final Message dummyMessage = new MessageWithCustomTextDecorator(
                MessageFactory.getDummyMessage(),
                "/guess " + lowerNumber
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Try higher!";

        games.put(dummyMessage.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with higher number should return Try lower")
    void handle_higherNumber_shouldReturnTryLower() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Game game = GameFactory.getTestGame();
        final int higherNumber = game.getNumberToGuess() + 1;
        final Message dummyMessage = new MessageWithCustomTextDecorator(
                MessageFactory.getDummyMessage(),
                "/guess " + higherNumber
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "Try lower!";

        games.put(dummyMessage.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with exact number should return You won")
    void handle_exactNumber_shouldReturnYouWon() {
        // Config
        final GuessCommandHandler handler = new GuessCommandHandler();
        final Game game = GameFactory.getTestGame();
        final int exactNumber = game.getNumberToGuess();
        final Message dummyMessage = new MessageWithCustomTextDecorator(
                MessageFactory.getDummyMessage(),
                "/guess " + exactNumber
        );
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "You won after 1 attempts! Want to /start one more game?";

        games.put(dummyMessage.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
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
