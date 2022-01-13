package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuessCommandHandler implements CommandHandler {
    @Override
    public BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats) {
        final Long chatId = message.chat().id();
        final Long senderId = message.from().id();
        final Game game = games.get(senderId);
        final String[] arguments = message.text().split(" ");

        if (game != null && game.isAlive()) {
            if (arguments.length > 1) {
                try {
                    final int senderNumber = Integer.parseInt(arguments[1]);

                    if (senderNumber < 1 || senderNumber > 100) {
                        return new SendMessage(chatId, "Please enter valid number from 1 to 100!");
                    }

                    final int tryGuess = game.guess(senderNumber);

                    if (tryGuess == 0) {
                        stats.computeIfAbsent(
                                senderId,
                                k -> {
                                    Map<LocalDateTime, Integer> map = new LinkedHashMap<>();
                                    map.put(LocalDateTime.now(), game.getAttempts());
                                    return map;
                                }
                        );
                        games.remove(senderId);
                        return new SendMessage(chatId, String.format("You won after %d attempts! Want to /start one more game?", game.getAttempts()));
                    } else if (tryGuess > 0) {
                        return new SendMessage(chatId, "Try lower!");
                    } else {
                        return new SendMessage(chatId, "Try higher!");
                    }

                } catch (NumberFormatException e) {
                    return new SendMessage(chatId, "Please enter valid number from 1 to 100!");
                }
            } else {
                return new SendMessage(chatId, "Please enter number to guess!");
            }
        } else {
            return new SendMessage(chatId, "You have no games running.");
        }
    }

    @Override
    public String getCommand() {
        return "/guess";
    }
}
