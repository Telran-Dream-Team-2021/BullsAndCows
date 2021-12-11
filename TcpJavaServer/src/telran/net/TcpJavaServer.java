package telran.net;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import telran.net.NetJavaServer;

public class TcpJavaServer extends NetJavaServer {
	private ServerSocket serverSocket;
	ExecutorService executor;

	public TcpJavaServer(int port, ApplProtocolJava protocol) {

		super(port, protocol);
		try {
			serverSocket = new ServerSocket(port);
			executor = Executors.newCachedThreadPool();
			serverSocket.setSoTimeout(1000);
		} catch (IOException e) {
			System.out.println("Port in use " + port);
		}

	}

	@Override
	public void run() {
		System.out.println("TCP Server is listening on port " + port);

		while (true) {
			try {
				Socket socket = serverSocket.accept();
				sockets.add(socket);
				socket.setSoTimeout(1000);
				executor.execute(new Thread(new TcpClientServer(socket, protocol)));
				if (isShutdown)
					throw new SocketTimeoutException();

			} catch (SocketTimeoutException e) {
				if (isShutdown) {
					shutdowning();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void shutdowning() {
		executor.shutdownNow();
		try {
			serverSocket.close();
			System.out.println("Server is closed");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
