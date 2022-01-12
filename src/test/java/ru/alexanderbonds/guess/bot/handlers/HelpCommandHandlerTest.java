package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alexanderbonds.guess.bot.models.MessageModel;


import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandHandlerTest {

    @Test
    @DisplayName("handle() must return help text")
    void handle_correctArguments_shouldReturnHelpText() {
        // Config
        final HelpCommandHandler handler = new HelpCommandHandler();
        final Message dummyMessage = MessageModel.getDummyMessage();
        final String expected =
                "/start - starts new game;\n" +
                "/guess <number> - once game started, guess a number!\n" +
                "/stop - whenever you tired, stop running game;\n" +
                "/stat - show your last five results.";

        // Call
        final BaseRequest request = handler.handle(dummyMessage, new HashMap<>(), new HashMap<>());

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("getCommand() should return /help")
    void getCommand_noArguments_shouldReturnHelpCommand() {
        // Config
        final HelpCommandHandler handler = new HelpCommandHandler();
        final String expected = "/help";

        // Call
        final String command = handler.getCommand();

        // Verify
        assertEquals(expected, command);
    }
}