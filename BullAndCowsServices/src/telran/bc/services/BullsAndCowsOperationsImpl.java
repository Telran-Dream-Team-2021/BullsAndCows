package telran.bc.services;

import telran.bc.dto.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BullsAndCowsOperationsImpl implements BullsAndCowsOperations, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String filePath = "BCGameData.data";
    private HashMap<Long, User> users = new HashMap<>();
    private HashMap<User, Game> currentGames = new HashMap<>();

    public static BullsAndCowsOperations getBullsAndCowsGame(String filePath) {
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filePath))) {
            BullsAndCowsOperationsImpl res = (BullsAndCowsOperationsImpl) reader.readObject();
            return res;
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
            save(filePath);
            System.out.printf("New user has been created: Name-%s, ID-%d", name, userId);
        } catch (Exception e) {
            System.out.println("Game has not been saved");
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
            throw new IllegalArgumentException("This user in not finished game. Game ID - "
                    + currentGames.get(currentUser).getGameId());
        }

        game = currentUser.startGame();
        currentGames.put(currentUser, game);
        try {
            save(filePath);
            System.out.printf("New game was created! GameID-%d", game.getGameId());
        } catch (Exception e) {
            System.out.println("Game has not been saved");
        }
        return game.getGameId();
    }

    @Override
    public ArrayList<Move> move(MoveData moveData) {
        long userId = moveData.userId;
        String number = moveData.number;
        User currentUser = users.get(userId);
        Game game = currentGames.get(currentUser);
        if (game == null || !game.isActive()) {
            throw new IllegalArgumentException("Game is not active");
        }
        game.addMove(number);

        try {
            save(filePath);
        } catch (Exception e) {
            System.out.println("didn't saved");
            System.out.println(e.getMessage());
        }

        if (!game.isActive()) {
            try {
                currentUser.saveGame(game);
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
        SearchGameDataResponce res = new SearchGameDataResponce(
                user.getId(),
                user.getName(),
                user.getGames(gameData.from, gameData.to)
        );
        return res;
    }

    @Override
    public void save(String filePath) throws Exception {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(filePath))) {
            writer.writeObject(this);
            System.out.println("game data was been saved");
        }
    }

}
