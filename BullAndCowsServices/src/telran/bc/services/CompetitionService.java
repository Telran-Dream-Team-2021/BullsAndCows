package telran.bc.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import telran.bc.dto.Competition;
import telran.bc.dto.Game;

public class CompetitionService {
	private final ScheduledExecutorService scheduler =
		     Executors.newScheduledThreadPool(2); // TODO change 2 to nThreads + 2
	
	public void addGame(Game game, Long endTime, Thread threadUser) {
		//TODO ALEX
	}
	
	public void createCompetitionSwitchers(Competition comp, BullsAndCowsOperationsImpl BC) {
		ModeSwitcher switcher = new ModeSwitcher(BC);
		long timeNow = System.currentTimeMillis()/1000;
		long timeStart = comp.getStartAt() - timeNow;
		long timeEnd = comp.getFinishAt() - timeNow;
		scheduler.schedule(switcher, timeStart, TimeUnit.SECONDS);
		scheduler.schedule(switcher, timeEnd, TimeUnit.SECONDS);
	}
	
	// TODO ALEX createFinisher(Game game, Thread threadUser): GameFinisher
}
