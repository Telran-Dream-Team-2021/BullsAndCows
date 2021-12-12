package telran.bc.net;

import java.time.Instant;
import java.util.ArrayList;

import telran.bc.dto.CompetitionCode;
import telran.bc.dto.Move;
import telran.bc.dto.MoveData;
import telran.bc.dto.SearchGameDataRequest;
import telran.bc.dto.SearchGameDataResponce;
import telran.bc.dto.User;
import telran.bc.dto.UserCodes;
import telran.bc.services.BullsAndCowsOperations;
import telran.net.TcpJavaClient;
import static telran.bc.api.RequestTypesApi.*;

public class BullAndCowsProxeTcpJava extends TcpJavaClient implements BullsAndCowsOperations{
	
	public BullAndCowsProxeTcpJava(String host, int port) throws Exception {
		super(host, port);
	}

	@Override
	public UserCodes registration(long userId, String name) throws Exception {
		User user = new User(userId, name);
		return send(REGISTRATION, user);
	}

	@Override
	public long start(long userId) throws Exception  {
		return send(START, userId);
	}

	@Override
	public ArrayList<Move> move(MoveData moveData) throws Exception {
		return send(MOVE, moveData);
	}


	@Override
	public SearchGameDataResponce searchGames(SearchGameDataRequest gameData) throws Exception {
		return send(SEARCH_GAMES, gameData);
	}

	@Override
	public void save(String fileName) throws Exception {
		throw new Exception("Client can't send request for saving");
	}
	
	@Override
	public boolean currentGameIsActive(long userId) throws Exception {
		return send(GAME_ACTIVE, userId);
	}
	
	@Override
	public UserCodes checkUser(long userId) throws Exception {
		return send(CHECK_USER, userId);
	}

	@Override
	public CompetitionCode createNewCompetition(Instant startAt, Instant finishAt, String resultsPath,
			int maxGameDuration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearCurrentGames() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGame(User user) {
		// TODO Auto-generated method stub
		
	}


}
