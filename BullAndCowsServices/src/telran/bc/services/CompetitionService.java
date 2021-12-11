package telran.bc.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CompetitionService {
	private final ScheduledExecutorService scheduler =
		     Executors.newScheduledThreadPool(1);
}
