package telran.bc.services;

import telran.bc.dto.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class BullsAndCowsOperationsImpl implements BullsAndCowsOperations, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "BCGameData.data";
    CompetitionService competitionService = new CompetitionService();
    private final HashMap<Long, User> users = new HashMap<>();
    private HashMap<User, Game> currentGames = new HashMap<>();
    private final TreeMap<LocalDateTime, Competition> competitions = new TreeMap<>();

    public static BullsAndCowsOperations getBullsAndCowsGame(String filePath) {
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filePath))) {
            return (BullsAndCowsOperationsImpl) reader.readObject();
        } catch (Exception e) {
            return new BullsAndCowsOperationsImpl();
        }
    }

    @Override
    public boolean currentGameIsActive(long userId) {
        User currentUser = users.get(userId);
        Game game = currentGames.get(currentUser);
        if (game == null) {
            return false;
        }
        return game.isActive();
    }

    @Override
    public UserCodes registration(long userId, String name) {
        User user = new User(userId, name);
        var res = users.putIfAbsent(userId, user);
        if (res != null) {
            return UserCodes.ALREADY_EXISTS;
        }
        try {
            save(FILE_PATH);
            System.out.printf("New user created: Name-%s, ID-%d", name, userId);
        } catch (Exception e) {
            System.out.println("The game has not been saved");
        }
        return UserCodes.OK;
    }

    public UserCodes checkUser(long userId) {
        User user = users.get(userId);
        if (user == null) {
            return UserCodes.USER_IS_NOT_EXISTS;
        } else {
            return UserCodes.OK;
        }
    }

    @Override
    public long start(long userId) {
        User currentUser = users.get(userId);
        if (currentUser == null) {
            throw new IllegalArgumentException("This user is not exist");
        }
        Game game = currentGames.get(currentUser);
        if (game != null && game.isActive()) {
            throw new IllegalArgumentException(
                    "This user in not finished game. Game ID - " + currentGames.get(currentUser).getGameId());
        }

        game = currentUser.startGame();
        currentGames.put(currentUser, game);
        try {
            save(FILE_PATH);
            System.out.printf("New game was created! GameID-%d", game.getGameId());
        } catch (Exception e) {
            System.out.println("The game has not been saved");
        }
        return game.getGameId();

		//TODO: add validation
    }

    @Override
    public ArrayList<Move> move(MoveData moveData) {
        long userId = moveData.userId;
        String number = moveData.number;
        User currentUser = users.get(userId);
        Game game = currentGames.get(currentUser);
        if (game == null || !game.isActive()) {
            throw new IllegalArgumentException("Game is not active. Start new game!");
        }
        game.addMove(number);

        try {
            save(FILE_PATH);
        } catch (Exception e) {
            System.out.println("didn't saved");
            System.out.println(e.getMessage());
        }

        if (!game.isActive()) {
            try {
                currentUser.saveGame(game, getSavePath());
                Thread.currentThread().sleep(1);
            } catch (Exception e) {
                System.out.println("didn't save game ID-" + game.getGameId());
                System.out.println(e.getMessage());
            }
        }
        return game.getMoves();
    }

    @Override
    public SearchGameDataResponce searchGames(SearchGameDataRequest gameData) {
        User user = users.get(gameData.userId);
        if (user == null) {
            throw new IllegalArgumentException("User is not exists");
        }
        return new SearchGameDataResponce(user.getId(),
                user.getName(),
                user.getGames(gameData.from, gameData.to));
    }

    @Override
    public void save(String filePath) throws Exception {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(filePath))) {
            writer.writeObject(this);
            System.out.println("game data was been saved");
        }
    }

    // server's methods
    public void clearCurrentGames() {
        currentGames.entrySet().forEach(e -> e.getValue().finishGame());
        currentGames = new HashMap<>();
    }

    public void deleteGame(User user) {
        currentGames.get(user).finishGame();
        currentGames.remove(user);
    }

    public CompetitionCode createNewCompetition(LocalDateTime startAt, LocalDateTime finishAt,
                                                String resultsPath, int maxGameDuration) {

        long startAtSeconds = localDateToLong(startAt);
        long finishAtSeconds = localDateToLong(finishAt);

        if (startAtSeconds > finishAtSeconds) {
            throw new IllegalArgumentException("startAt cannot be more than finishAt");
        }
        if (startAtSeconds < localDateToLong(LocalDateTime.now())) {
            throw new IllegalArgumentException("startAt cannot be less than time now");
        }

        Competition comp = new Competition(startAtSeconds, finishAtSeconds, resultsPath,
                maxGameDuration);
        var res = competitions.putIfAbsent(finishAt, comp);
        competitionService.createCompetitionSwitchers(comp, this);
        return res == null ? CompetitionCode.CREATED : CompetitionCode.ALREADY_EXISTS;

    }

    private long localDateToLong(LocalDateTime ldt) {
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        return zdt.toInstant().getEpochSecond();
    }

    private String getSavePath() {
        return null;
    }
}
