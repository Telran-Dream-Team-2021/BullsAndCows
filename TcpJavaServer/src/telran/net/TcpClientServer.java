package telran.net;

import java.io.*;
import java.net.*;

import telran.net.dto.*;

public class TcpClientServer implements Runnable {
	private final Socket socket;
	private final ApplProtocolJava protocol;
	private final ObjectInputStream reader;
	private final ObjectOutputStream writer;

	public TcpClientServer(Socket socket, ApplProtocolJava protocol) throws IOException {
		this.socket = socket;
		this.protocol = protocol;
		reader = new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		try (socket) {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					RequestJava request = (RequestJava) reader.readObject();
					ResponseJava response = protocol.getResponse(request);
					writer.writeObject(response);
				} catch (SocketTimeoutException e) {
					if (NetJavaServer.isShutdown) {
						break;
					}
				}
			}
		} catch (EOFException e) {
			System.out.println("client closed connection");
		} catch (Exception e) {
			System.out.println("abnormal closing connection: " + e.getMessage());
		}
	}
}
