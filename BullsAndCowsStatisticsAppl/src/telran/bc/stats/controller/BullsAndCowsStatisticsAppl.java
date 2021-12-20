package telran.bc.stats.controller;

import telran.bc.stats.controller.actions.BullAndCowsStatisticsActions;
import telran.bc.stats.service.BullsAndCowsStatisticsServices;
import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;
import terlan.view.Menu;

public class BullsAndCowsStatisticsAppl {
	private static final String CONFIG_PATH = "";

	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		BullsAndCowsStatisticsServices service =
				new BullsAndCowsStatisticsServices("../BullsAndCowsServer/games/competitions");
		Menu menu = new Menu("Bulls and cows competitions statistics",
				BullAndCowsStatisticsActions.getItems(io, service));
		
		menu.perform(io);
	}

}
