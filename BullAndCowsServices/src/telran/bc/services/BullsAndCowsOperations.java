package telran.bc.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import telran.bc.dto.CompetitionCode;
import telran.bc.dto.Move;
import telran.bc.dto.MoveData;
import telran.bc.dto.SearchGameDataRequest;
import telran.bc.dto.SearchGameDataResponce;
import telran.bc.dto.User;
import telran.bc.dto.UserCodes;

public interface BullsAndCowsOperations{
	public UserCodes registration(long userId, String name) throws Exception;
	public long start(long userId) throws Exception ;
	public ArrayList<Move> move(MoveData moveData) throws Exception;
	SearchGameDataResponce searchGames(SearchGameDataRequest gameData) throws Exception;
	void save(String filePath) throws Exception;
	public boolean currentGameIsActive(long userId) throws Exception;
	public UserCodes checkUser(long userId) throws Exception;
	public CompetitionCode createNewCompetition(Instant startAt, Instant finishAt, 
			String resultsPath, int maxGameDuration);
	public void clearCurrentGames();
	public void deleteGame(User user);
}
