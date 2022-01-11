package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.util.Map;

public class GuessCommandHandler implements CommandHandler {
    @Override
    public BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats) {
        Long chatId = message.chat().id();
        Long senderId = message.from().id();
        Game game = games.get(senderId);
        String[] arguments = message.text().split(" ");

        if (game != null && game.isAlive()) {
            if (arguments.length > 1) {
                try {
                    int senderNumber = Integer.parseInt(arguments[1]);
                    int tryGuess = game.guess(senderNumber);

                    if (tryGuess == 0) {
                        stats.get(senderId).put(LocalDateTime.now(), game.getAttempts());
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
