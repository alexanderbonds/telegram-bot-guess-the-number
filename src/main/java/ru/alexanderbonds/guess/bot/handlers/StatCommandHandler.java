package ru.alexanderbonds.guess.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import ru.alexanderbonds.guess.bot.Game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;

public class StatCommandHandler implements CommandHandler {
    @Override
    public BaseRequest handle(Message message, Map<Long, Game> games, Map<Long, Map<LocalDateTime, Integer>> stats) {
        final Long chatId = message.chat().id();
        final Long senderId = message.from().id();

        final Map<LocalDateTime, Integer> statMap = stats.get(senderId);

        if (statMap != null) {
            final StringBuilder builder = new StringBuilder("Your last five games:\n")
                    .append("--------------------------------\n");
            statMap.keySet().stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(5)
                    .forEach(ldt -> builder
                            .append("at ")
                            .append(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(ldt))
                            .append(" you've won after ")
                            .append(statMap.get(ldt))
                            .append(" attempts\n"));
            return new SendMessage(chatId, builder.toString());
        } else {
            return new SendMessage(chatId, "You have no game records. Feel free to /start your first game.");
        }
    }

    @Override
    public String getCommand() {
        return "/stat";
    }
}
