package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.util.Map;

public interface CommandHandler {
    BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats);
    String getCommand();
}
