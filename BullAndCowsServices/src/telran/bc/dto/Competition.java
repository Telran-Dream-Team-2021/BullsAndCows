package telran.bc.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Competition implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<Long> users = new TreeSet<>();
    private long startAt;
    private long finishAt;
    private String resultsPath;
    private int maxGameDuration;

    public Competition(long startAt, long finishAt, String resultsPath, int maxGameDuration) {
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.resultsPath = resultsPath;
        this.maxGameDuration = maxGameDuration;
        users = new HashSet<Long>();
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

    public long getMaxGameDuration() {
        return maxGameDuration;
    }
}
