package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class StartCommandHandler implements CommandHandler {
    @Override
    public BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats) {
        Long chatId = message.chat().id();
        Long senderId = message.from().id();
        String senderName = message.from().username();

        if (games.containsKey(senderId)) {
            return new SendMessage(chatId, "You already have active game, try to guess a number or /stop this game!");
        }

        games.put(senderId, new Game());
        stats.computeIfAbsent(senderId, k -> new LinkedHashMap<>());

        return new SendMessage(chatId, String.format("Hello, %s! New game started..\n"
                + "You need to guess number from 1 to 100 that I've chosen.\n"
                + "Please use /help for all commands. Good luck!", senderName));
    }

    @Override
    public String getCommand() {
        return "/start";
    }
}
