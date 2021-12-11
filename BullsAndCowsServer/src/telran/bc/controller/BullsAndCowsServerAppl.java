package telran.bc.controller;

import java.io.FileInputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Properties;

import telran.bc.dto.CompetitionCode;
import telran.bc.net.BullsAndCowsProtocol;
import telran.bc.services.BullsAndCowsOperations;
import telran.bc.services.BullsAndCowsOperationsImpl;
import telran.net.ApplProtocolJava;
import telran.net.NetJavaServer;
import telran.view.ConsoleInputOutput;
import telran.view.EndOfInputException;
import telran.view.InputOutput;
import terlan.view.Item;
import terlan.view.Menu;

public class BullsAndCowsServerAppl {

	private static final String filePath = "BCGameData.data";
	private static BullsAndCowsOperations bullsAndCowsService;
	static InputOutput io;
	public static void main(String[] args) throws Exception {
		bullsAndCowsService = BullsAndCowsOperationsImpl.getBullsAndCowsGame(filePath);
		io = new ConsoleInputOutput();
		
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
		ArrayList<Item> items = getItems(server);
		Menu menu = new Menu("Server menu", items);
		try {
			menu.perform(io);
		} catch (EndOfInputException e) {
			io.writeObjectLine("Good by server");
		}
	}

	private static ArrayList<Item> getItems(NetJavaServer server) {
		 ArrayList<Item> menuItems = new ArrayList<>();
			menuItems.add(Item.of("Create new competition", t->{
				createCompetition(io);
			}));
			menuItems.add(Item.of("Shutdown server", t->{
				server.shutdown();
			}));
			return menuItems;
	}

	private static void createCompetition(InputOutput io) {
		try {
			CompetitionCode res = bullsAndCowsService.createNewCompetition(getStartAt(io), getFinishAt(io), getPath(io),
					getMaxGameDuration(io));
			io.writeObjectLine(res);
			bullsAndCowsService.save(filePath);
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	private static int getMaxGameDuration(InputOutput io) {
		return io.readInt("Enter max game duration in minutes")*60;
	}

	private static String getPath(InputOutput io) {
		return io.readString("Enter path of competition's results");
	}

	private static Instant getStartAt(InputOutput io) {
		LocalDateTime localDateTime = io.readDate("Enter start of competition - [yyyy-MM-dd HH:mm]", "yyyy-MM-dd HH:mm");
		Instant res = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return res;
	}
	
	private static Instant getFinishAt(InputOutput io) {
		LocalDateTime localDateTime = io.readDate("Enter finish of competition - [yyyy-MM-dd HH:mm]", "yyyy-MM-dd HH:mm");
		Instant res = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return res;
	}
	
	

}
