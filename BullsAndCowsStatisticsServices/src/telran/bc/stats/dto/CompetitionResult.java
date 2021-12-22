package telran.bc.stats.dto;

import java.util.List;

public class CompetitionResult {
	private String competitionName;
	private int usersAmount;
	private int totalGamesAmount;
	private List<UserResult> compStatistics;
	
	public CompetitionResult(String competitionMame, int usersAmount, int totalGamesAmount,
			List<UserResult> compStatistics) {
		this.competitionName = competitionMame;
		this.usersAmount = usersAmount;
		this.totalGamesAmount = totalGamesAmount;
		this.compStatistics = compStatistics;
	}

	public String getCompetitionName() {
		return competitionName;
	}

	public int getUsersAmount() {
		return usersAmount;
	}

	public int getTotalGamesAmount() {
		return totalGamesAmount;
	}

	public List<UserResult> getCompStatistics() {
		return compStatistics;
	}

}
