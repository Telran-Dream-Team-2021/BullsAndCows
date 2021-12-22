package telran.bc.controller.actions;

import telran.bc.dto.*;
import telran.bc.services.BullsAndCowsOperations;
import telran.view.InputOutput;
import terlan.view.Item;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BullsAndCowsActions {
    static long userId;
    private static BullsAndCowsOperations bullsAndCowsService;

    public static ArrayList<Item> getItems(BullsAndCowsOperations bullsAndCowsService, InputOutput io) {
        BullsAndCowsActions.bullsAndCowsService = bullsAndCowsService;
        return getMenu();
    }

    private static ArrayList<Item> getMenu() {
        ArrayList<Item> menuItems = new ArrayList<>();
        menuItems.add(Item.of("Registration", t -> {
            try {
                registration(t);
            } catch (Exception e) {
                t.writeObjectLine(e.getMessage());
            }
        }));
        menuItems.add(Item.of("Start new game", t -> {
            try {
                startGame(t);
            } catch (Exception e) {
                t.writeObjectLine(e.getMessage());
            }
        }));
        menuItems.add(Item.of("Enter your number", t -> {
            try {
                move(t);
            } catch (Exception e) {
                t.writeObjectLine(e.getMessage());
            }
        }));
        menuItems.add(Item.of("Search user's games", t -> {
            try {
                searchGames(t);
            } catch (Exception e) {
                t.writeObjectLine(e.getMessage());
            }
        }));
        menuItems.add(Item.of("All user's games", t -> {
            try {
                getAllUserGames(t);
            } catch (Exception e) {
                t.writeObjectLine(e.getMessage());
            }
        }));
        menuItems.add(Item.of("Registration to competition", t -> {
            try {
                registrationToCompetition(t);
            } catch (Exception e) {
                t.writeObjectLine(e.getMessage());
            }
        }));
        menuItems.add(Item.of("Exit", e -> {
        }, true));

        return menuItems;
    }

    private static void registrationToCompetition(InputOutput io) throws Exception {
        if (userId == 0L) {
            setUser(getId(io), io);
        }

        List<LocalDateTime> competitions = bullsAndCowsService.getAllCompetitions();

        if (competitions.size() == 0) {
            throw new Exception("There are no scheduled competitions");
        }

        StringBuilder variants = new StringBuilder("");
        variants.append("Incoming competitions:\n");
        for (int i = 0; i < competitions.size(); i++) {
            variants.append(String.format("%d - %s\n", i + 1, competitions.get(i).toString()));
        }
        variants.append("Enter number of competition.\n");

        int userChoiсe = io.readInt(variants.toString(), 1, competitions.size());
        io.writeObjectLine(bullsAndCowsService.registerToCompetition(new RegistrationToCompetitionData(userId, competitions.get(userChoiсe - 1))));
    }

    private static void searchGames(InputOutput io) throws Exception {
        SearchGameDataRequest gameDataRequest = new SearchGameDataRequest(getId(io), getDateFrom(io), getDateTo(io));
        SearchGameDataResponce gameDataResponce = bullsAndCowsService.searchGames(gameDataRequest);

        if (gameDataResponce.games.size() > 0) {
            displayGames(gameDataResponce, io);
        } else {
            io.writeObjectLine("No games for specified period");
        }
    }

    private static void getAllUserGames(InputOutput io) throws Exception {
        SearchGameDataRequest gameDataRequest = new SearchGameDataRequest(getId(io), LocalDateTime.of(2000, 1, 1, 0, 0, 0), LocalDateTime.of(3000, 1, 1, 0, 0, 0));
        SearchGameDataResponce gameDataResponce = bullsAndCowsService.searchGames(gameDataRequest);

        if (gameDataResponce.games.size() > 0) {
            displayGames(gameDataResponce, io);
        } else {
            io.writeObjectLine("You don't have games");
        }
    }

    private static void displayGames(SearchGameDataResponce data, InputOutput io) {
        List<Game> games = data.games;
        io.writeObjectLine("_".repeat(60));
        io.writeObjectLine("");
        io.writeObjectLine(String.format("ID - %d, NAME - %s", data.userId, data.userName));
        io.writeObjectLine("_".repeat(60));
        io.writeObjectLine("");
        io.writeObjectLine("Count of games " + games.size());
        games.stream().filter(g -> {
            io.writeObjectLine("_".repeat(60));
            io.writeObjectLine("");

            io.writeObjectLine("GameId - " + g.getGameId() + "; Moves - " + g.getMoves().size() + "; Time end - "
                    + g.getTimeEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            io.writeObjectLine("_ _ ".repeat(15));
            io.writeObjectLine("");
            return true;
        }).flatMap(g -> g.getMoves().stream()).forEach(io::writeObjectLine);
        io.writeObjectLine("_".repeat(60));
    }

    private static LocalDateTime getDateFrom(InputOutput io) {
        return io.readDate("Enter Date From in format - [yyyy-MM-dd HH:mm]", "yyyy-MM-dd HH:mm");
    }

    private static LocalDateTime getDateTo(InputOutput io) {
        return io.readDate("Enter Date To in format - [yyyy-MM-dd HH:mm]", "yyyy-MM-dd HH:mm");
    }

    private static void clearConsole() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    private static void move(InputOutput io) {
        if (userId == 0L) {
            try {
                setUser(getId(io), io);
            } catch (Exception e) {
                io.writeObjectLine(e.getMessage());
            }
        }
        try {
            MoveData moveData = new MoveData(getNumber(io), userId);
            ArrayList<Move> moves = bullsAndCowsService.move(moveData);
            displayMoves(moves, io);
            while (bullsAndCowsService.currentGameIsActive(userId)) {
                moveData = new MoveData(getNumber(io), userId);
                moves = bullsAndCowsService.move(moveData);
                try {
                    clearConsole();
                } catch (Exception e) {
                }
                displayMoves(moves, io);
            }
        } catch (Exception e) {
            io.writeObjectLine(e.getMessage());
        }
    }

    private static void displayMoves(ArrayList<Move> moves, InputOutput io) {

        moves.forEach(m -> {
            String res = String.format("Your number - %s; Bulls - %d; Cows - %d", m.getUserNumber(), m.getBulls(),
                    m.getCows());
            io.writeObjectLine(res);
            if (m.isWinner()) {
                io.writeObjectLine("_".repeat(40));
                io.writeObjectLine("Congratulations! Secret number is - " + m.getUserNumber());
            }
        });

    }

    private static String getNumber(InputOutput io) {
        return io.readString("Enter number");
    }

    private static void startGame(InputOutput io) throws Exception {
        if (userId == 0L) {
            setUser(getId(io), io);
        }
        long gameId = bullsAndCowsService.start(userId);
        io.writeObjectLine("New Game! GameID - " + gameId);
    }

    private static void registration(InputOutput io) throws Exception {
        long id = getId(io);
        UserCodes res = bullsAndCowsService.registration(id, getName(io));
        if (UserCodes.OK == res) {
            setUser(id, io);
        }
        io.writeObjectLine(res.toString());
    }

    private static void setUser(long id, InputOutput io) throws Exception {

        UserCodes res = bullsAndCowsService.checkUser(id);
        if (UserCodes.OK == res) {
            userId = id;
        } else {
            io.writeObjectLine(res.toString());
        }

    }

    private static String getName(InputOutput io) {
        return io.readString("Enter name");
    }

    private static long getId(InputOutput io) {
        return io.readLong("Enter User Id");
    }
}
