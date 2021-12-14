package telran.bc.dto;

import java.time.Instant;
import java.util.Set;

public class Competition {
	private Set<Long> users;
	private long startAt;
	private long finishAt;
	private String resultsPath;
	private int maxGameDuration;

	public Competition(long startAt, long finishAt, String resultsPath, int maxGameDuration) {
		this.startAt = startAt;
		this.finishAt = finishAt;
		this.resultsPath = resultsPath;
		this.maxGameDuration = maxGameDuration;
	}

	public boolean isOnGoing() {
		long time = Instant.now().getEpochSecond();
		return time >= startAt && time < finishAt;
	}

	public CompetitionCode registerUser(long id) {
		return users.add(id) ? CompetitionCode.OK : CompetitionCode.ALREADY_REGISTERED;
	}
	
	public boolean isOnCompetition(long id) {
		return users.contains(id);
	}
	
	public long getStartAt() {
		return startAt;
	}

	public long getFinishAt() {
		return finishAt;
	}

	public String getResultsPath() {
		return resultsPath;
	}

	public int getMaxGameDuration() {
		return maxGameDuration;
	}
}
