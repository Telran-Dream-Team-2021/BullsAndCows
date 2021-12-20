package telran.bc.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RegistrationToCompetitionData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public long userId;
	public LocalDateTime competitionKey;
	
	public RegistrationToCompetitionData(long userId, LocalDateTime competitionKey) {
		this.userId = userId;
		this.competitionKey = competitionKey;
	}

}
