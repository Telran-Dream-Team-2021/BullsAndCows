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
        //TODO: check if game is active and make it asleep,
        // remove from currentGames and make fall asleep userThread
        game.finishGame();
        threadUser.interrupt(); //to think how to kill it properly
    }
}