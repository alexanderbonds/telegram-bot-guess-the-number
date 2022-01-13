package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.factories.MessageFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StatCommandHandlerTest {

    @Test
    @DisplayName("handle() with recorded games must return statistic")
    void handle_recordedGames_shouldReturnStatistic() {
        // Config
        final StatCommandHandler handler = new StatCommandHandler();
        final Message message = MessageFactory.getMessage();
        final Map<Long, Map<LocalDateTime, Integer>> stats = new ConcurrentHashMap<>();
        final LocalDateTime date = LocalDateTime.now();
        final String expected =
                "Your last five games:\n" +
                "--------------------------------\n" +
                "at " +
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(date) +
                " you've won after 1 attempts\n";

        stats.computeIfAbsent(message.from().id(), k -> {
            Map<LocalDateTime, Integer> map = new LinkedHashMap<>();
            map.put(date, 1);
            return map;
        });

        // Call
        final BaseRequest request = handler.handle(message, new HashMap<>(), stats);

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with no recorded games must return warning")
    void handle_noRecordedGames_shouldReturnWarning() {
        // Config
        final StatCommandHandler handler = new StatCommandHandler();
        final Message message = MessageFactory.getMessage();
        final Map<Long, Map<LocalDateTime, Integer>> stats = new ConcurrentHashMap<>();
        final String expected = "You have no game records. Feel free to /start your first game.";

        // Call
        final BaseRequest request = handler.handle(message, new HashMap<>(), stats);

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("getCommand() should return /stat")
    void getCommand_noArguments_shouldReturnStatCommand() {
        // Config
        final StatCommandHandler handler = new StatCommandHandler();
        final String expected = "/stat";

        // Call
        final String command = handler.getCommand();

        // Verify
        assertEquals(expected, command);
    }
}
