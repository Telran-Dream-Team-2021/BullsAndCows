package telran.bc.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SearchGameDataRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long userId;
	public LocalDateTime from;
	public LocalDateTime to;
	public SearchGameDataRequest(long userId, LocalDateTime from, LocalDateTime to) {
		this.userId = userId;
		this.from = from;
		this.to = to;
	}
	
}
