package telran.bc.stats.service;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import telran.bc.stats.dto.CompetitionResult;
import telran.bc.stats.dto.UserResult;

public class BullsAndCowsStatisticsServices {
	private List<CompetitionResult> competitionResults = new ArrayList<CompetitionResult>();
	private Map<Long, UserResult> userResultsByUserId = new HashMap<Long, UserResult>();
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
		Map<Long, List<Integer>> gamesHistoryByUser = new HashMap<>();
		Arrays.stream(compFolder.listFiles((dir, name) -> name.matches
				("\\d*_?*_[\\d]{4}_[\\d]{2}_[\\d]{1,2}_[\\d]{1,2}_[\\d]{1,2}")))
			.forEach(file -> {
				try(BufferedReader reader = new BufferedReader(new FileReader((File)file))) {
					UserResult userResult = new UserResult(getUserId(file), getUserName(file));
					userResultsByUserId.put(userResult.getUserId(), userResult);
					int movesAmount = (int) reader.lines().count();
					gamesHistoryByUser.compute(userResult.getUserId(), (userId, gamesHistory) -> {
						if(gamesHistoryByUser.get(userId) == null) {
							gamesHistory = new LinkedList<Integer>();
							gamesHistoryByUser.put(userId, gamesHistory);
							usersAmount++;
						}
						gamesHistory.add(movesAmount);
						totalGamesAmount++;
						return gamesHistory;
						}
					);
				} catch(Exception e) {
					System.out.println("Wrong path to games directory or games data files are not consistent");
				}
			});	
		List<UserResult> userResults = getUserResults(gamesHistoryByUser);
		return new CompetitionResult(compName, usersAmount, totalGamesAmount, userResults);
	}
	
	private List<UserResult> getUserResults(Map<Long, List<Integer>> gamesHistoryByUser) {
		gamesHistoryByUser.entrySet().stream()
			.sorted((e1, e2) -> {
				if(e1.getValue().size() > e2.getValue().size()) {
					return 1;
				} else if(e1.getValue().size() < e2.getValue().size()) {
					return -1;
				} else {
					if(e1.getValue().stream().mapToInt(i -> i).sum() 
							> e1.getValue().stream().mapToInt(i -> i).sum()) {
						return 1;
					} else
						return -1;
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
