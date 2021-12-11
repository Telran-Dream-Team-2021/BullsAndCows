package telran.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class NetJavaServer extends Thread{
	int port;
	ApplProtocolJava protocol;
	static volatile boolean isShutdown;
	static List<Socket> sockets;
	public NetJavaServer(int port, ApplProtocolJava protocol) {
		this.port = port;
		this.protocol = protocol;
		isShutdown = false;
		sockets = new ArrayList<>();
	}
	
	public abstract void run();
	
	public void shutdown() {
		isShutdown = true;
	}
	
	public boolean addSocket(Socket socket) {
		return sockets.add(socket);
	}
	
	
	public static void kickAllSokets() {
		sockets.forEach(s->{
			try {
				s.close();
			} catch (IOException e) {
				//TODO make exception more informative
				System.out.printf("Socket not closed");
				System.out.println(e.getMessage());
			}
		});
		sockets = new ArrayList<>();
	}
	
	
}
