package telran.bc.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import telran.bc.controller.actions.BullsAndCowsActions;
import telran.bc.net.BullAndCowsProxeTcpJava;
import telran.bc.net.BullAndCowsProxeUdpJava;
import telran.bc.net.BullsAndCowsProxeNetJava;
import telran.bc.services.BullsAndCowsOperations;
import telran.net.NetJavaClient;
import telran.net.TcpJavaClient;
import telran.net.UdpJavaClient;
import telran.view.ConsoleInputOutput;
import telran.view.EndOfInputException;
import telran.view.InputOutput;
import terlan.view.Item;
import terlan.view.Menu;

public class BullAndCowsConsoleAppl {
	
	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		BullsAndCowsOperations bullsAndCowsService;
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream("../NetJavaCommon/application.properties"));
			String host = props.getProperty("host");
			int port = Integer.parseInt(props.getProperty("port"));
			String base_package = props.getProperty("base_package");
			String protocolName = props.getProperty("client_protocol");
			
			@SuppressWarnings("unchecked")
			Class<NetJavaClient> clazz = (Class<NetJavaClient>) Class.forName(base_package + protocolName);
			NetJavaClient protocol = clazz.getConstructor(String.class, int.class).newInstance(host, port);
			bullsAndCowsService = new BullsAndCowsProxeNetJava(protocol);
			ArrayList<Item> items = BullsAndCowsActions.getItems(bullsAndCowsService, io);
			Menu menu = new Menu("Bulls and Cows", items);
			try {
				menu.perform(io);
			} catch (EndOfInputException e1) {
				io.writeObjectLine("Good bye");

			}
		} catch (Exception e) {
			e.printStackTrace();
			io.writeObjectLine("Cannot connected to server");
		}
		
	}
	
}
