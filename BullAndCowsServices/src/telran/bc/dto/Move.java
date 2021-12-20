package telran.bc.dto;

import java.io.Serializable;

public class Move implements Serializable {

    private static final long serialVersionUID = 1L;
    private int bulls;
    private int cows;
    private String userNumber;
    private boolean winner;

    public Move(int bulls, int cows, String userNumber, boolean winner) {
        this.bulls = bulls;
        this.cows = cows;
        this.userNumber = userNumber;
        this.winner = winner;
    }

    public Move(int bulls, int cows, String userNumber) {
        this.bulls = bulls;
        this.cows = cows;
        this.userNumber = userNumber;
        this.winner = false;
    }

    public int getBulls() {
        return bulls;
    }

    public int getCows() {
        return cows;
    }

    public boolean isWinner() {
        return winner;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String toString() {
        return String.format("Your number - %s; Bulls - %d, Cows - %d", userNumber, bulls, cows);
    }
}
