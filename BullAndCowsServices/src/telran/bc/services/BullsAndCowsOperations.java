package telran.bc.services;

import telran.bc.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface BullsAndCowsOperations {
    UserCodes registration(long userId, String name) throws Exception;

    long start(long userId) throws Exception;

    ArrayList<Move> move(MoveData moveData) throws Exception;

    SearchGameDataResponce searchGames(SearchGameDataRequest gameData) throws Exception;

    void save(String filePath) throws Exception;

    boolean currentGameIsActive(long userId) throws Exception;

    UserCodes checkUser(long userId) throws Exception;

    CompetitionCode createNewCompetition(LocalDateTime startAt, LocalDateTime finishAt,
                                                String resultsPath, int maxGameDuration);

    void clearCurrentGames();

    void deleteGame(User user);

    ArrayList<LocalDateTime> getAllCompetitions() throws Exception;

    CompetitionCode registerToCompetition(RegistrationToCompetitionData data) throws Exception;
}
