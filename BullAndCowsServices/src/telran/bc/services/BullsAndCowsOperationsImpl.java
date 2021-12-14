package telran.bc.services;

import java.io.*;
import java.time.*;
import java.util.*;
import telran.bc.dto.*;

public class BullsAndCowsOperationsImpl implements BullsAndCowsOperations, Serializable {
	private static final long serialVersionUID = 1L;
	private static final String filePath = "BCGameData.data";
	private HashMap<Long, User> users = new HashMap<>();
	private HashMap<User, Game> currentGames = new HashMap<>();
	private TreeMap<LocalDateTime, Competition> competitions = new TreeMap<>();
	CompetitionService competitionService = new CompetitionService();
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
			System.out.printf("New user created: Name-%s, ID-%d", name, userId);
		} catch (Exception e) {
			System.out.println("didn't saved");
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
			save(filePath);
			System.out.printf("New game was created! GameID-%d", game.getGameId());
		} catch (Exception e) {
			System.out.println("didn't saved");
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
				currentUser.saveGame(game, getSavePath());
				Thread.currentThread().sleep(1);
			} catch (Exception e) {
				System.out.println("didn't save game ID-" + game.getGameId());
				System.out.println(e.getMessage());
			}
		}

		return game.getMoves();
	}

	private String getSavePath() {
		return null;
	}

	@Override
	public SearchGameDataResponce searchGames(SearchGameDataRequest gameData) {
		User user = users.get(gameData.userId);
		if (user == null) {
			throw new IllegalArgumentException("User is not exists");
		}
		SearchGameDataResponce res = new SearchGameDataResponce(user.getId(), user.getName(),
				user.getGames(gameData.from, gameData.to));
		return res;
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
		
		if(startAtSeconds>finishAtSeconds) {
			throw new IllegalArgumentException("startAt cannot be more than finishAt");
		}
		if(startAtSeconds>localDateToLong(LocalDateTime.now())) {
			throw new IllegalArgumentException("startAt cannot be more than time now");
		}
		
		Competition comp = new Competition(startAtSeconds, finishAtSeconds, resultsPath,
				maxGameDuration);
		var res = competitions.putIfAbsent(finishAt, comp);
		competitionService.createCompititionSwitchers(comp, this);
		return res == null ? CompetitionCode.CREATED : CompetitionCode.ALREADY_EXISTS;

	}
	
	private long localDateToLong(LocalDateTime ldt) {
		ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
		return zdt.toInstant().getEpochSecond();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<LocalDateTime> getAllCompetitions() {
		return new ArrayList<LocalDateTime>(competitions.keySet().stream().filter(k -> k.compareTo(LocalDateTime.now()) > 0).toList());
	}

	@Override
	public CompetitionCode registerToCompetition(long userId, LocalDateTime localDateTime) throws Exception {
		return competitions.get(localDateTime).registerUser(userId);
	}
}
