package telran.bc.net;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import telran.bc.dto.Move;
import telran.bc.dto.MoveData;
import telran.bc.dto.RegistrationToCompetitionData;
import telran.bc.dto.SearchGameDataRequest;
import telran.bc.dto.User;
import telran.bc.services.BullsAndCowsOperations;
import telran.net.ApplProtocolJava;
import telran.net.dto.RequestJava;
import telran.net.dto.ResponseCode;
import telran.net.dto.ResponseJava;

public class BullsAndCowsProtocol implements ApplProtocolJava{
	BullsAndCowsOperations bullsAndCowsOperations;
	
	public BullsAndCowsProtocol(BullsAndCowsOperations bullsAndCowsOperations) {
		this.bullsAndCowsOperations = bullsAndCowsOperations;
	}
	@Override
	public ResponseJava getResponse(RequestJava request) {
		try {
			Method method = BullsAndCowsProtocol.class
					.getDeclaredMethod(request.requestType.replaceAll("/", "_"), Serializable.class);
			return (ResponseJava) method.invoke(this, request.data);
		} catch (NoSuchMethodException e) {
			return new ResponseJava(ResponseCode.WRONG_REQUEST_TYPE, new Exception("Wrong request " + request.requestType));
		} catch (Exception e) {
			return new ResponseJava(ResponseCode.WRONG_REQUEST_DATA, e);
		}
	}
	private ResponseJava bc_check_user(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, bullsAndCowsOperations.checkUser((long)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	private ResponseJava bc_game_active(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, 
					bullsAndCowsOperations.currentGameIsActive((long)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	private ResponseJava bc_search_games(Serializable data) {
		SearchGameDataRequest gameData = (SearchGameDataRequest)data;
		try {
			return new ResponseJava(ResponseCode.OK, 
					bullsAndCowsOperations.searchGames(gameData));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	private ResponseJava bc_move(Serializable data) {
		MoveData moveData = (MoveData)data;
		try {
			return new ResponseJava(ResponseCode.OK, new ArrayList<Move>(bullsAndCowsOperations.move(moveData)));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	private ResponseJava bc_start(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, bullsAndCowsOperations.start((long)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	private ResponseJava bc_registration(Serializable data) {
		try {
			User user = (User)data;
			return new ResponseJava(ResponseCode.OK, bullsAndCowsOperations.registration(user.getId(), user.getName()));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	@SuppressWarnings("unused")
	private ResponseJava bc_get_competitions(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, bullsAndCowsOperations.getAllCompetitions());
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	@SuppressWarnings("unused")
	private ResponseJava bc_registration_competition(Serializable data) {
		try {
//			long userId = ((RegistrationToCompetitionData)data).userId;
//			LocalDateTime competitionKey = ((RegistrationToCompetitionData)data).competitionKey;
			return new ResponseJava(ResponseCode.OK, bullsAndCowsOperations.registerToCompetition((RegistrationToCompetitionData)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	private ResponseJava getWrongDataResponse(Exception e) {
		return new ResponseJava(ResponseCode.WRONG_REQUEST_DATA, e);
	}



}
