package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.Game;
import ru.alexanderbonds.guess.bot.models.MessageModel;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StopCommandHandlerTest {

    @Test
    @DisplayName("handle() with no active games should start new game")
    void handle_noActiveGame_shouldStartNewGameAndReturnGreeting() {
        // Config
        final StopCommandHandler handler = new StopCommandHandler();
        final Message dummyMessage = MessageModel.getDummyMessage();
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final Map<Long, Map<LocalDateTime, Integer>> stats = new ConcurrentHashMap<>();
        final String expected = String.format("Hello, %s! New game started..\n"
                + "You need to guess number from 1 to 100 that I've chosen.\n"
                + "Please use /help for all commands. Good luck!", dummyMessage.from().username());

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, stats);

        // Verify
        assertAll(
                () -> assertTrue(games.containsKey(dummyMessage.from().id())),
                () -> assertTrue(stats.containsKey(dummyMessage.from().id())),
                () -> assertEquals(expected, request.getParameters().get("text"))
        );
    }

    @Test
    @DisplayName("handle() with active game should stop active game")
    void handle_hasActiveGame_shouldStopActiveGame() {
        // Config
        final StopCommandHandler handler = new StopCommandHandler();
        final Message dummyMessage = MessageModel.getDummyMessage();
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final Game game = new Game();
        final Map<Long, Map<LocalDateTime, Integer>> stats = new ConcurrentHashMap<>();
        final String expected = String.format("Your game terminated, correct answer was %d.", game.getNumberToGuess());

        games.put(dummyMessage.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, stats);

        // Verify
        assertAll(
                () -> assertFalse(games.containsKey(dummyMessage.from().id())),
                () -> assertFalse(stats.containsKey(dummyMessage.from().id())),
                () -> assertEquals(expected, request.getParameters().get("text"))
        );
    }

    @Test
    @DisplayName("getCommand() should return /stop")
    void getCommand_noArguments_shouldReturnStartCommand() {
        // Config
        final StopCommandHandler handler = new StopCommandHandler();
        final String expected = "/stop";

        // Call
        final String command = handler.getCommand();

        // Verify
        assertEquals(expected, command);
    }
}