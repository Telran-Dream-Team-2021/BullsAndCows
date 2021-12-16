package telran.bc.services;

import telran.bc.dto.Game;

public class GameFinisher extends Thread {
    private final Game game;
    private final Thread threadUser;
    private final BullsAndCowsOperations bullsAndCowsService;


    public GameFinisher(Game game, Thread threadUser, BullsAndCowsOperations bullsAndCowsService) {
        this.game = game;
        this.threadUser = threadUser;
        this.bullsAndCowsService = bullsAndCowsService;
    }

    public void run() {
        //TODO: check if game is active and make it asleep,
        // remove from currentGames and make fall asleep userThread
    }
}
