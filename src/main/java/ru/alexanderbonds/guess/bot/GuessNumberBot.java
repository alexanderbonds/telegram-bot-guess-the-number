package ru.alexanderbonds.guess.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;


public class GuessNumberBot {
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    private final UpdateHandler updateHandler = new UpdateHandler();

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(u -> bot.execute(updateHandler.handle(u)));

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
