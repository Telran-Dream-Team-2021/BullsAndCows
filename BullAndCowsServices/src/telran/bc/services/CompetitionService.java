package telran.bc.services;

import telran.bc.dto.Competition;
import telran.bc.dto.Game;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CompetitionService implements Serializable {

    private transient final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(10); // TODO change 2 to nThreads + 2

    public void addGame(Game game, Long endTime, Thread threadUser) {
        GameFinisher finisher = createFinisher(game, threadUser);
        scheduler.schedule(finisher, endTime, TimeUnit.SECONDS);
    }

    public void createCompetitionSwitchers(Competition comp, BullsAndCowsOperationsImpl bc) {
        ModeSwitcher switcher = new ModeSwitcher(bc);
        long timeNow = System.currentTimeMillis() / 1000;
        long timeStart = comp.getStartAt() - timeNow;
        long timeEnd = comp.getFinishAt() - timeNow;
        scheduler.schedule(switcher, timeStart, TimeUnit.SECONDS);
        scheduler.schedule(switcher, timeEnd, TimeUnit.SECONDS);
    }

    public GameFinisher createFinisher(Game game, Thread threadUser) {
        return new GameFinisher(game, threadUser);
    }
}
