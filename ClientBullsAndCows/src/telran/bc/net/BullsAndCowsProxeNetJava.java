package telran.bc.net;

import static telran.bc.api.RequestTypesApi.CHECK_USER;
import static telran.bc.api.RequestTypesApi.GAME_ACTIVE;
import static telran.bc.api.RequestTypesApi.MOVE;
import static telran.bc.api.RequestTypesApi.REGISTRATION;
import static telran.bc.api.RequestTypesApi.SEARCH_GAMES;
import static telran.bc.api.RequestTypesApi.START;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import telran.bc.dto.Move;
import telran.bc.dto.MoveData;
import telran.bc.dto.SearchGameDataRequest;
import telran.bc.dto.SearchGameDataResponce;
import telran.bc.dto.User;
import telran.bc.dto.UserCodes;
import telran.bc.services.BullsAndCowsOperations;
import telran.net.NetJavaClient;
import telran.net.UdpJavaClient;

public class BullsAndCowsProxeNetJava implements BullsAndCowsOperations{

	NetJavaClient protocol;

	public BullsAndCowsProxeNetJava(NetJavaClient protocol) throws Exception {
//		super(host, port);
		this.protocol = protocol;
	}

	@Override
	public UserCodes registration(long userId, String name) throws Exception {
		User user = new User(userId, name);
		return protocol.send(REGISTRATION, user);
	}

	@Override
	public long start(long userId) throws Exception  {
		return protocol.send(START, userId);
	}

	@Override
	public ArrayList<Move> move(MoveData moveData) throws Exception {
		return protocol.send(MOVE, moveData);
	}


	@Override
	public SearchGameDataResponce searchGames(SearchGameDataRequest gameData) throws Exception {
		return protocol.send(SEARCH_GAMES, gameData);
	}

	@Override
	public void save(String fileName) throws Exception {
		throw new Exception("Client can't send request for saving");
	}
	

	@Override
	public boolean currentGameIsActive(long userId) throws Exception {
		return protocol.send(GAME_ACTIVE, userId);
	}

	@Override
	public UserCodes checkUser(long userId) throws Exception {
		return protocol.send(CHECK_USER, userId);
	}

}
