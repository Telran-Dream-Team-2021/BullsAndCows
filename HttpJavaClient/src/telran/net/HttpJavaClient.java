package telran.net;

import java.io.IOException;
import java.io.Serializable;

public class HttpJavaClient extends NetJavaClient{

	public HttpJavaClient(String host, int port) throws Exception {
		super(host, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T send(String requestType, Serializable data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
