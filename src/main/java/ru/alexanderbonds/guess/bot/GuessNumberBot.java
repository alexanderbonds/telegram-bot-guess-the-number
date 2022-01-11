package ru.alexanderbonds.guess.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
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

public class GuessNumberBot {
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    private final Map<Long, Game> games = new ConcurrentHashMap<>();
    private final Map<Long, Map<LocalDateTime, Integer>> stats = new ConcurrentHashMap<>();
    private final Map<String, CommandHandler> handlers = new ConcurrentHashMap<>();

    public GuessNumberBot() {
        List.of(
                new StopCommandHandler(),
                new StartCommandHandler(),
                new HelpCommandHandler(),
                new StatCommandHandler(),
                new GuessCommandHandler()
        ).forEach(handler -> handlers.put(handler.getCommand(), handler));
    }

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::handleUpdate);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
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

        if (request != null) {
            bot.execute(request);
        }
    }
}
