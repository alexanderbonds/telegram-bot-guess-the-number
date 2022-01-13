package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.Game;
import ru.alexanderbonds.guess.bot.factories.MessageFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StartCommandHandlerTest {

    @Test
    @DisplayName("handle() with no active games should start new game")
    void handle_noActiveGame_shouldStartNewGameAndReturnGreeting() {
        // Config
        final StartCommandHandler handler = new StartCommandHandler();
        final Message message = MessageFactory.getMessage();
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = String.format("Hello, %s! New game started..\n"
                + "You need to guess number from 1 to 100 that I've chosen.\n"
                + "Please use /help for all commands. Good luck!", message.from().username());

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertAll(
                () -> assertTrue(games.containsKey(message.from().id())),
                () -> assertEquals(expected, request.getParameters().get("text"))
        );
    }

    @Test
    @DisplayName("handle() with active game should return warning")
    void handle_hasActiveGame_shouldNotStartNewGameAndReturnWarning() {
        // Config
        final StartCommandHandler handler = new StartCommandHandler();
        final Message message = MessageFactory.getMessage();
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "You already have active game, try to guess a number or /stop this game!";

        games.put(message.from().id(), new Game());

        // Call
        final BaseRequest request = handler.handle(message, games, new HashMap<>());

        // Verify
        assertAll(
                () -> assertTrue(games.containsKey(message.from().id())),
                () -> assertEquals(expected, request.getParameters().get("text"))
        );
    }

    @Test
    @DisplayName("getCommand() should return /start")
    void getCommand_noArguments_shouldReturnStartCommand() {
        // Config
        final StartCommandHandler handler = new StartCommandHandler();
        final String expected = "/start";

        // Call
        final String command = handler.getCommand();

        // Verify
        assertEquals(expected, command);
    }
}
