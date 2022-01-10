package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.util.Map;

public class HelpCommandHandler implements CommandHandler {
    @Override
    public BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats) {
        Long chatId = message.chat().id();

        return new SendMessage(chatId, "/start - starts new game;\n"
                + "/guess <number> - once game started, guess a number!\n"
                + "/stop - whenever you tired, stop running game;\n"
                + "/stat - show your last five results.");
    }

    @Override
    public String getCommand() {
        return "/help";
    }
}
