package ru.alexanderbonds.guess.bot.decorators;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;

public class MessageWithCustomTextDecorator extends Message {
    private final User from;
    private final Chat chat;
    private final String text;

    public MessageWithCustomTextDecorator(Message message, String text) {
        this.from = message.from();
        this.chat = message.chat();
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public User from() {
        return from;
    }

    @Override
    public Chat chat() {
        return chat;
    }
}
