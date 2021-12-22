package telran.bc.services;

import telran.bc.dto.Game;

public class GameFinisher implements Runnable {
    private final Game game;
    private final Thread threadUser;

    public GameFinisher(Game game, Thread threadUser) {
        this.game = game;
        this.threadUser = threadUser;
    }
    
    @Override
    public void run() {
        game.finishGame();
        threadUser.interrupt();
    }
}