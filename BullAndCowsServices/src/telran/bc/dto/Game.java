package telran.bc.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Game implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final ArrayList<Move> moves;
    private final String secretNumber;
    private boolean active;
    private final long gameId;
    private LocalDateTime timeEnd;

    public Game(long gameId) {
        this.secretNumber = generateSecretNumber();
        this.active = true;
        this.gameId = gameId;
        this.moves = new ArrayList<>();
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void displayMoves() {
        moves.forEach(System.out::println);
    }

    public long getGameId() {
        return gameId;
    }

    public boolean isActive() {
        return active;
    }

    private String generateSecretNumber() {
        String res = new Random()
                .ints(1, 10)
                .distinct()
                .limit(4)
                .boxed()
                .map(n -> Integer.toString(n))
                .collect(Collectors.joining());
        System.out.println("SecretNumber - " + res);
        return res;
    }

    public Move addMove(String number) {
        Move move = calculate(number);
        moves.add(move);
        return move;
    }

    private Move calculate(String number) {

        int bulls = 0;
        int cows = 0;
        int length = number.length() > 3 ? 4 : number.length();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 4; j++) {
                if (number.charAt(i) == secretNumber.charAt(j)) {
                    if (i == j) {
                        bulls++;
                    } else {
                        cows++;
                    }
                }
            }
        }

        if (bulls == 4 && cows == 0 && number.length() == 4) {
            finishGame();
            return new Move(bulls, cows, number, true);
        }
        return new Move(bulls, cows, number);
    }

    public void finishGame() {
        active = false;
        timeEnd = LocalDateTime.now();
    }
}
