package telran.bc.stats.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import telran.bc.stats.controller.actions.BullAndCowsStatisticsActions;
import telran.bc.stats.service.BullsAndCowsStatisticsServices;
import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;
import terlan.view.Menu;

public class BullsAndCowsStatisticsAppl {
	private static final String CONFIG_PATH = "./config.properties";
	private static final String DEFAULT_COMPETITION_PATH = "./config.properties";

	public static void main(String[] args) {
		Properties props = new Properties();
		String path;
		try {
			props.load(new FileReader(CONFIG_PATH));
			path = props.getProperty("competitionsPath");
		} catch (Exception e) {
			path = "./games/competitions";
			e.printStackTrace();
		}
		InputOutput io = new ConsoleInputOutput();
		BullsAndCowsStatisticsServices service =
				new BullsAndCowsStatisticsServices("../BullsAndCowsServer/games/competitions");
		Menu menu = new Menu("Bulls and cows competitions statistics",
				BullAndCowsStatisticsActions.getItems(io, service));
		
		menu.perform(io);
	}

}
