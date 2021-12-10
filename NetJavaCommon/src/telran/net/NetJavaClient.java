package telran.net;

import java.io.Closeable;
import java.io.Serializable;


public abstract class NetJavaClient implements Closeable{

	String host;
	int port;
	public NetJavaClient(String host, int port) throws Exception{
		this.host = host;
		this.port = port;
	}

	public abstract <T> T send(String requestType, Serializable data) throws Exception;
}
