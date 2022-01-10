package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.util.Map;

public class StopCommandHandler implements CommandHandler {
    @Override
    public BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats) {
        Long chatId = message.chat().id();
        Long senderId = message.from().id();
        Game game = games.get(senderId);

        if (game != null && game.isAlive()) {
            int numberToGuess = game.getNumberToGuess();
            games.remove(senderId);
            return new SendMessage(chatId, String.format("Your game terminated, correct answer was %d.", numberToGuess));
        } else {
            return new SendMessage(chatId, "You have no games running.");
        }
    }

    @Override
    public String getCommand() {
        return "/stop";
    }
}
