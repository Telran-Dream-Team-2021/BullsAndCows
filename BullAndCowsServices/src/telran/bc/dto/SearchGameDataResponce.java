package telran.bc.dto;

import java.io.Serializable;
import java.util.List;

public class SearchGameDataResponce implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long userId;
	public String userName;
	public List<Game> games;
	
	public SearchGameDataResponce(long userId, String userName, List<Game> games) {
		this.userId = userId;
		this.userName = userName;
		this.games = games;
	}
	
}
