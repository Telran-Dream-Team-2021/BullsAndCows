package telran.bc.stats.service;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import telran.bc.stats.dto.CompetitionResult;
import telran.bc.stats.dto.UserResult;

public class BullsAndCowsStatisticsServices {
	private List<CompetitionResult> competitionResults = new ArrayList<CompetitionResult>();
	private int usersAmount = 0;
	private int totalGamesAmount = 0;
	private int position = 1;
	
	public BullsAndCowsStatisticsServices(String path) {
		downloadCompetitionResults(path);
	}

	private void downloadCompetitionResults(String path) {
		File[] compFolders = new File(path).listFiles(file -> file.isDirectory());
		Arrays.stream(compFolders)
			.forEach(compFolder -> {
				CompetitionResult compResult;
				compResult = getCompetitionResult(compFolder);
				competitionResults.add(compResult);
			});		
	}
	
	private CompetitionResult getCompetitionResult(File compFolder) {
		String compName = compFolder.getName();
		Map<Long, UserResult> userResultsByUserId = new HashMap<>();
		Map<Long, List<Integer>> gamesHistoryByUser = new HashMap<>();
		Arrays.stream(compFolder.listFiles((dir, name) -> name.matches
		("\\d*_.+_[\\d]{4}_[\\d]{1,2}_[\\d]{1,2}_[\\d]{1,2}_[\\d]{1,2}\\.txt")))
			.forEach(file -> {
				try(BufferedReader reader = new BufferedReader(new FileReader((File)file))) {
					long userId = getUserId(file);
					int movesAmount = (int) reader.lines().count();
					UserResult userResult = new UserResult(userId, getUserName(file));
					userResultsByUserId.put(userId, userResult);
					gamesHistoryByUser.compute(userId, (id, gamesHistory) -> {
						if(gamesHistoryByUser.get(id) == null) {
							gamesHistory = new LinkedList<Integer>();
							usersAmount++;
						}
						gamesHistory.add(movesAmount);
						totalGamesAmount++;
						return gamesHistory;
						}
					);
				} catch(Exception e) {
					e.printStackTrace();
//					System.out.println("Wrong path to games directory or games data files are not consistent");
				}
			});	
		List<UserResult> userResults = getUserResults(gamesHistoryByUser, userResultsByUserId);
		CompetitionResult competitionResult = 
				new CompetitionResult(compName, usersAmount, totalGamesAmount, userResults);
		cleanGlobalVariables();
		return competitionResult;
	}
	
	private void cleanGlobalVariables() {
		usersAmount = 0;
		totalGamesAmount = 0;
		position = 1;	
	}

	private List<UserResult> getUserResults(Map<Long, List<Integer>> gamesHistoryByUser,
			Map<Long, UserResult> userResultsByUserId) {
		gamesHistoryByUser.entrySet().stream()
			.sorted((e1, e2) -> {
				if(e1.getValue().size() < e2.getValue().size()) {
					return 1;
				} else if(e1.getValue().size() > e2.getValue().size()) {
					return -1;
				} else {
					if(e1.getValue().stream().mapToInt(i -> i).sum() 
							> e2.getValue().stream().mapToInt(i -> i).sum()) {
						return 1;
					} else
						return 0;
				}
			})
			.forEach(e -> {
				UserResult userResult = userResultsByUserId.get(e.getKey());
				userResult.setGamesAmount(e.getValue().size());
				userResult.setMovesAmount(e.getValue().stream().mapToInt(i -> i).sum());
				userResult.setPosition(position++);
			});
		List<UserResult> list = new LinkedList<UserResult>();
		userResultsByUserId.values().stream().forEach(v -> list.add(v));
		return list;
	}

	private long getUserId(File file) {
		String[] parsedFileName = file.getName().split("_");	
		return Long.parseLong(parsedFileName[0]);
	}
	
	private String getUserName(File file) {	
		String[] parsedFileName = file.getName().split("_");	
		return parsedFileName[1];
	}

	public List<String> getAllCompetitionNames() {		
		List<String> res = new ArrayList<String>();
		competitionResults.stream()
				.map(compRes -> compRes.getCompetitionName())
				.forEach(compName -> res.add(compName));
		return res;				
	}
	
	public CompetitionResult getCompetitionResult(int competitionIndex) {
		return competitionResults.get(competitionIndex);
	}

}
