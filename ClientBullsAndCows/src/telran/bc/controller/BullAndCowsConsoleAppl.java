package telran.bc.controller;

import telran.bc.controller.actions.BullsAndCowsActions;
import telran.bc.net.BullsAndCowsProxyNetJava;
import telran.bc.services.BullsAndCowsOperations;
import telran.net.NetJavaClient;
import telran.view.*;
import terlan.view.Item;
import terlan.view.Menu;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class BullAndCowsConsoleAppl {

    public static void main(String[] args) {
        InputOutput io = new ConsoleInputOutput();
        BullsAndCowsOperations bullsAndCowsService;
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("./TcpJavaServer/application.properties"));
            String host = props.getProperty("host");
            int port = Integer.parseInt(props.getProperty("port"));
            String basePackage = props.getProperty("base_package");
            String protocolName = props.getProperty("client_protocol");

            @SuppressWarnings("unchecked")
            Class<NetJavaClient> clazz = (Class<NetJavaClient>) Class.forName(basePackage + protocolName);
            NetJavaClient protocol = clazz.getConstructor(String.class, int.class).newInstance(host, port);
            bullsAndCowsService = new BullsAndCowsProxyNetJava(protocol);
            ArrayList<Item> items = BullsAndCowsActions.getItems(bullsAndCowsService, io);
            Menu menu = new Menu("Bulls and Cows", items);
            try {
                menu.perform(io);
            } catch (EndOfInputException e1) {
                io.writeObjectLine("Good by");
            }
        } catch (Exception e) {
            e.printStackTrace();
            io.writeObjectLine("Cannot connected to server");
        }
    }
}
