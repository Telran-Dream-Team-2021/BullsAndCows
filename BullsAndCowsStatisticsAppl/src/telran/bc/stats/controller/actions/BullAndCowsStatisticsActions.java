package telran.bc.stats.controller.actions;

import java.util.*;

import telran.bc.stats.dto.CompetitionResult;
import telran.bc.stats.service.BullsAndCowsStatisticsServices;
import telran.view.*;
import terlan.view.Item;

public class BullAndCowsStatisticsActions {
	private static BullsAndCowsStatisticsServices service;
	
	public static ArrayList<Item> getItems(InputOutput io, BullsAndCowsStatisticsServices service) {
		BullAndCowsStatisticsActions.service = service;
		return getMenuItems(io);
	}

	private static ArrayList<Item> getMenuItems(InputOutput io) {
		ArrayList<Item> menuItems = new ArrayList<Item>();
		menuItems.add(Item.of("Show competitions results", BullAndCowsStatisticsActions :: showAllCompetitions));
		menuItems.add(Item.exit());
		return menuItems;
	}
	
	private static void showAllCompetitions(InputOutput io) {
		List<String> competitionNames = service.getAllCompetitionNames();
		int competitionIndex =  io.readInt(getCompetitionLine(competitionNames), 1, competitionNames.size()) - 1;
		CompetitionResult competitionResult = service.getCompetitionResult(competitionIndex);
		displayCompetitionResult(competitionResult, io);
	}

	private static void displayCompetitionResult(CompetitionResult competitionResult, InputOutput io) {
		io.writeObjectLine(String.format("Competition: %s Number of participants: %d Games finnished: %d",
				competitionResult.getCompetitionName(),
				competitionResult.getUsersAmount(),
				competitionResult.getTotalGamesAmount()));
		competitionResult.getCompStatistics().forEach(userResult -> {
			io.writeObjectLine(String.format("Pos: %d UserId: %d Name: %s Games: %d Moves: %d",
					userResult.getPosition(),
					userResult.getUserId(),
					userResult.getUserName(),
					userResult.getGamesAmount(),
					userResult.getMovesAmount()));
		});	
	}

	private static String getCompetitionLine(List<String> competitionNames) {
		int lineNum = 1;
		StringBuilder sb = new StringBuilder("");
		int length = competitionNames.size();
		for (int i = 0; i < length - 1; i++) {
			sb.append(String.format("%d. %s\n", lineNum, competitionNames.get(i)));
			lineNum++;
		}
		sb.append(String.format("%d. %s", lineNum, competitionNames.get(length - 1)));
		return sb.toString();
	}
}
