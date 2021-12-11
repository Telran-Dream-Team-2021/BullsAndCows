package telran.bc.services;

import telran.net.NetJavaServer;

public class ModeSwitcher implements Runnable{
	BullsAndCowsOperationsImpl BC;
	public ModeSwitcher(BullsAndCowsOperationsImpl BC) {
		this.BC = BC;
	}
	@Override
	public void run() {
		NetJavaServer.kickAllSokets();
		BC.clearCurrentGames();
	}

}
