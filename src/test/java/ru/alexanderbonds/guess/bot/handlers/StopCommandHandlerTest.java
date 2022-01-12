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

class StopCommandHandlerTest {

    @Test
    @DisplayName("handle() with no active games should return warning")
    void handle_noActiveGame_shouldReturnWarning() {
        // Config
        final StopCommandHandler handler = new StopCommandHandler();
        final Message dummyMessage = MessageFactory.getDummyMessage();
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final String expected = "You have no games running.";

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with active game should stop active game")
    void handle_hasActiveGame_shouldStopActiveGame() {
        // Config
        final StopCommandHandler handler = new StopCommandHandler();
        final Message dummyMessage = MessageFactory.getDummyMessage();
        final Map<Long, Game> games = new ConcurrentHashMap<>();
        final Game game = new Game();
        final String expected = String.format("Your game terminated, correct answer was %d.", game.getNumberToGuess());

        games.put(dummyMessage.from().id(), game);

        // Call
        final BaseRequest request = handler.handle(dummyMessage, games, new HashMap<>());

        // Verify
        assertAll(
                () -> assertFalse(games.containsKey(dummyMessage.from().id())),
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
