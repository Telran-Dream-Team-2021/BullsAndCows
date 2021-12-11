package telran.net;

import java.net.Socket;
import java.util.List;

public abstract class NetJavaServer extends Thread{
	int port;
	ApplProtocolJava protocol;
	static volatile boolean isShutdown;
	static List<Socket> sockets; //todo
	public NetJavaServer(int port, ApplProtocolJava protocol) {
		this.port = port;
		this.protocol = protocol;
		isShutdown = false;
	}
	
	public abstract void run();
	
	public void shutdown() {
		isShutdown = true;
	}
	
	
}
