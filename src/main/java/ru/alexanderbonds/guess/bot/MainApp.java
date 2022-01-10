package ru.alexanderbonds.guess.bot;


public class MainApp {
    public static void main(String[] args) {
        // Dummy Web Server to prevent Heroku app from sleeping
        new DummyWebServer().start();

        // Actual bot start
        new GuessNumberBot().serve();
    }
}
