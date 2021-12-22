package telran.bc.stats.dto;

public class UserResult {
	private long userId;
	private String userName;
	private int position;
	private int gamesAmount;
	private int movesAmount;
	
	public UserResult(long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public int getPosition() {
		return position;
	}

	public int getGamesAmount() {
		return gamesAmount;
	}

	public int getMovesAmount() {
		return movesAmount;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setGamesAmount(int gamesAmount) {
		this.gamesAmount = gamesAmount;
	}

	public void setMovesAmount(int movesAmount) {
		this.movesAmount = movesAmount;
	}
	
	
	
}
