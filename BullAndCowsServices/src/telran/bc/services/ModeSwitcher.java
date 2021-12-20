package telran.bc.services;

import telran.net.NetJavaServer;

public class ModeSwitcher implements Runnable {
    BullsAndCowsOperationsImpl bcService;

    public ModeSwitcher(BullsAndCowsOperationsImpl bcService) {
        this.bcService = bcService;
    }

    @Override
    public void run() {
        NetJavaServer.kickAllSokets();
        bcService.clearCurrentGames();
    }

}
