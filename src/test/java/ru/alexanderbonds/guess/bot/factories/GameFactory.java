package ru.alexanderbonds.guess.bot.factories;

import ru.alexanderbonds.guess.bot.Game;

public class GameFactory {
    private static final int LOWEST_GUESS = 1;
    private static final int HIGHEST_GUESS = 100;

    public static Game getGame() {
        Game game = new Game();
        while (game.getNumberToGuess() < LOWEST_GUESS + 1 || game.getNumberToGuess() > HIGHEST_GUESS - 1) {
            game = new Game();
        }
        return game;
    }
}
