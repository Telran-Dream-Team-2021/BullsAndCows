package telran.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import telran.net.dto.*;
import telran.net.util.UdpUtils;

public class UdpServerJava extends NetJavaServer{
	private static final int MAX_PACKEGE_LENGTH = 10000;
	DatagramSocket socket;
	public UdpServerJava(int port, ApplProtocolJava protocol) throws Exception {
		super(port, protocol);
		socket = new DatagramSocket(port);
	}
	@Override
	public void run() {
		System.out.println("UDP server is listening datagrams on port " + port);
		while(true) {
			try {
				
				byte[] bufferReceive = new byte[MAX_PACKEGE_LENGTH];
				DatagramPacket packageReceive = new DatagramPacket(bufferReceive, MAX_PACKEGE_LENGTH);
				socket.receive(packageReceive);
				RequestJava request = 
						(RequestJava) UdpUtils.getSerializableFromByteArray(packageReceive.getData(),
						packageReceive.getLength());
				ResponseJava response = protocol.getResponse(request);
				byte[] bufferSend = UdpUtils.getByteArray(response);
				DatagramPacket packageSend = new DatagramPacket(bufferSend, bufferSend.length, 
						packageReceive.getAddress(), packageReceive.getPort());
				socket.send(packageSend);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
