package telran.bc.services;

import telran.bc.dto.Game;

public class GameFinisher extends Thread {
    private final Game game;
    private final Thread threadUser;

    public GameFinisher(Game game, Thread threadUser) {
        this.game = game;
        this.threadUser = threadUser;
    }

    public void run() {
        game.finishGame();
        threadUser.interrupt();
    }
}