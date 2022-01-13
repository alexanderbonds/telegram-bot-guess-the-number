package ru.alexanderbonds.guess.bot.decorators;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import ru.alexanderbonds.guess.bot.factories.MessageFactory;

public class UpdateWithCustomMessageDecorator extends Update {
    private final Message message;

    public UpdateWithCustomMessageDecorator(String text) {
        this.message = new MessageWithCustomTextDecorator(
                MessageFactory.getMessage(),
                text
        );
    }

    @Override
    public Message message() {
        return message;
    }
}
