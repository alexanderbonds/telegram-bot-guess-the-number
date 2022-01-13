package ru.alexanderbonds.guess.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.alexanderbonds.guess.bot.decorators.UpdateWithCustomMessageDecorator;
import ru.alexanderbonds.guess.bot.handlers.CommandHandler;
import ru.alexanderbonds.guess.bot.handlers.GuessCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.HelpCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.StartCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.StatCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.StopCommandHandler;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UpdateHandlerTest {

    @Test
    @DisplayName("handle() with empty text should return NULL")
    void handle_emptyText_shouldReturnNull() {
        // Config
        final UpdateHandler handler = new UpdateHandler();
        final Update dummyUpdate = new UpdateWithCustomMessageDecorator(Strings.EMPTY);

        // Call
        final BaseRequest request = handler.handle(dummyUpdate);

        // Verify
        assertNull(request);
    }

    @Test
    @DisplayName("handle() with wrong command should return Command not implemented")
    void handle_wrongCommand_shouldReturnWarning() {
        // Config
        final UpdateHandler handler = new UpdateHandler();
        final Update update = new UpdateWithCustomMessageDecorator("/command-that-not-exist");
        final String expected = "Command is not implemented, please refer to /help.";

        // Call
        final BaseRequest request = handler.handle(update);

        // Verify
        assertEquals(expected, request.getParameters().get("text"));
    }

    @Test
    @DisplayName("handle() with not-command message should return NULL")
    void handle_notCommandMessage_shouldReturnNull() {
        // Config
        final UpdateHandler handler = new UpdateHandler();
        final Update update = new UpdateWithCustomMessageDecorator("not-command-message");

        // Call
        final BaseRequest request = handler.handle(update);

        // Verify
        assertNull(request);
    }

    @ParameterizedTest(name = "handle() with {1} command should include only {2}")
    @MethodSource("provideSourcesFor_handleStreamOfHandlersAndCommandsCommandShouldInvokeOnlySpecificHandler")
    // handlerName used only in @ParametrizedTest name generation
    void handle_streamOfHandlersAndCommands_commandShouldInvokeOnlySpecificHandler(List<CommandHandler> handlers, String text, String handlerName) {
        // Config
        final UpdateHandler handler = new UpdateHandler(handlers);
        final Update update = new UpdateWithCustomMessageDecorator(text);

        // Call
        handler.handle(update);

        // Verify
        handlers.forEach(h -> {
            int times = 0;
            if (h.getCommand().equals(text)) times = 1;
            verify(h, times(times)).handle(any(), any(), any());
        });
    }

    private static Stream<Arguments> provideSourcesFor_handleStreamOfHandlersAndCommandsCommandShouldInvokeOnlySpecificHandler() {
        return Stream.of(
                Arguments.of(getNewListOfCommandHandlerSpies(), "/start", "StartCommandHandler"),
                Arguments.of(getNewListOfCommandHandlerSpies(), "/stop", "StopCommandHandler"),
                Arguments.of(getNewListOfCommandHandlerSpies(), "/help", "HelpCommandHandler"),
                Arguments.of(getNewListOfCommandHandlerSpies(), "/stat", "StatCommandHandler"),
                Arguments.of(getNewListOfCommandHandlerSpies(), "/guess", "GuessCommandHandler")
        );
    }

    private static List<CommandHandler> getNewListOfCommandHandlerSpies() {
        return List.of(
                spy(new StartCommandHandler()),
                spy(new StopCommandHandler()),
                spy(new HelpCommandHandler()),
                spy(new StatCommandHandler()),
                spy(new GuessCommandHandler())
        );
    }
}
