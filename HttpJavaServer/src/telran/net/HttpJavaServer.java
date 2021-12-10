package telran.net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpJavaServer extends NetJavaServer{
	HttpServer server;
	
	public HttpJavaServer(int port, ApplProtocolJava protocol) {
		super(port, protocol);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
