package ru.alexanderbonds.guess.bot.factories;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Message;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class MessageFactory {
    private static final Gson gson = new Gson();
    private static final String MODEL_FILENAME = "src/test/resources/models/message.json";
    private static final String MODEL = readModelFromFile();

    public static Message getDummyMessage() {
        return gson.fromJson(MODEL, Message.class);
    }

    private static String readModelFromFile() {
        String model = "";
        try {
            File file = new File(MODEL_FILENAME);
            model = Files
                    .lines(file.toPath(), StandardCharsets.UTF_8)
                    .reduce((s, s2) -> s + s2)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }
}
