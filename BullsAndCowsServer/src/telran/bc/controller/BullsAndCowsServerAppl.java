package telran.bc.controller;

import java.io.FileInputStream;
import java.util.Properties;

import telran.bc.net.BullsAndCowsProtocol;
import telran.bc.services.BullsAndCowsOperationsImpl;
import telran.net.ApplProtocolJava;
import telran.net.NetJavaServer;
import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;

public class BullsAndCowsServerAppl {

	private static final String filePath = "BCGameData.data";
	
	public static void main(String[] args) throws Exception {
		InputOutput io = new ConsoleInputOutput();
		Properties props = new Properties();
		props.load(new FileInputStream("../NetJavaCommon/application.properties"));
		int port = Integer.parseInt(props.getProperty("port"));
		String base_package = props.getProperty("base_package");
		String protocolName = props.getProperty("server_protocol");
		
		ApplProtocolJava applProtocol = new BullsAndCowsProtocol(BullsAndCowsOperationsImpl.getBullsAndCowsGame(filePath));

		@SuppressWarnings("unchecked")
		Class<NetJavaServer> clazz = (Class<NetJavaServer>) Class.forName(base_package + protocolName);
		NetJavaServer server = clazz.getConstructor(int.class, ApplProtocolJava.class).newInstance(port, applProtocol);
		server.start();
		while(true) {
			String str = io.readString("Enter 'shutdown' to close server");
			if(str.equals("shutdown")) {
				server.shutdown();
				break;
			}
		}
	}

}
