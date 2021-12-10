package telran.net;

public abstract class NetJavaServer extends Thread{
	int port;
	ApplProtocolJava protocol;
	static volatile boolean isShutdown;
	
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
