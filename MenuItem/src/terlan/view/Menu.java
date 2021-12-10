package terlan.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import telran.view.EndOfInputException;
import telran.view.InputOutput;

public class Menu implements Item {
	
	private String name;
	private ArrayList<Item> items;
	
	public Menu(String name, ArrayList<Item> items) {
		this.name = name;
		this.items = items;
	}
	
	public Menu(String name, Item ...items) {
		this(name, new ArrayList<Item>(Arrays.asList(items)));
	}

	@Override
	public String displayName() {
		return name;
	}
	private static void clearConsole() throws InterruptedException, IOException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
	@Override
	public void perform(InputOutput io) {
		displayTitle(io);
		displayItems(io);
		while(true) {
			try {
				int itemNumber = io.readInt("Select action item", 1, items.size());
				Item item = items.get(itemNumber-1);
				try {
					clearConsole();
				} catch(Exception e) {}
				
				item.perform(io);
				displayTitle(io);
				displayItems(io);
				if(item.isExit()) {
					break;
				}
			} catch(EndOfInputException e) {
				if(!io.readString("Do you wonna exit? [y/n]").equals("n")) {
					throw e;
				}
			}
		}
	}

	private void displayItems(InputOutput io) {
		IntStream.range(0, items.size()).forEach(i->
		io.writeObjectLine(String.format("%d. %s", i+1, items.get(i).displayName())));
	}

	private void displayTitle(InputOutput io) {
		io.writeObjectLine("_".repeat(20));
		io.writeObjectLine(name);
		io.writeObjectLine("_".repeat(20));
	}

	@Override
	public boolean isExit() {
		return false;
	}

}
