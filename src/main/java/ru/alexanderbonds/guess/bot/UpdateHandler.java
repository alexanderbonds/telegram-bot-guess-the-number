package ru.alexanderbonds.guess.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.handlers.CommandHandler;
import ru.alexanderbonds.guess.bot.handlers.GuessCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.HelpCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.StartCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.StatCommandHandler;
import ru.alexanderbonds.guess.bot.handlers.StopCommandHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateHandler {
    private final Map<String, CommandHandler> handlers = new ConcurrentHashMap<>();
    private final Map<Long, Game> games = new ConcurrentHashMap<>();
    private final Map<Long, Map<LocalDateTime, Integer>> stats = new ConcurrentHashMap<>();

    public UpdateHandler() {
        List.of(
                new StopCommandHandler(),
                new StartCommandHandler(),
                new HelpCommandHandler(),
                new StatCommandHandler(),
                new GuessCommandHandler()
        ).forEach(handler -> handlers.put(handler.getCommand(), handler));
    }

    public BaseRequest handle(Update update) {
        Message message = update.message();

        BaseRequest request = null;

        if (message != null) {
            String text = message.text();

            if (text != null && text.startsWith("/")) {
                String command = text.split(" ")[0];

                if (handlers.containsKey(command)) {
                    CommandHandler handler = handlers.get(command);
                    request = handler.handle(message, games, stats);
                } else {
                    request = new SendMessage(message.chat().id(), "Command is not implemented, please refer to /help.");
                }
            }
        }

        return request;
    }
}
