package telran.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import telran.net.NetJavaClient;
import telran.net.dto.RequestJava;
import telran.net.dto.ResponseCode;
import telran.net.dto.ResponseJava;
import telran.net.util.UdpUtils;

public class UdpJavaClient extends NetJavaClient{
	private static final int MAX_PACKEGE_LENGTH = 10000;
	DatagramSocket socket;
	InetAddress serverHost;
	public UdpJavaClient(String host, int port) throws Exception{
		super(host, port);
		serverHost = InetAddress.getByName(host);
		socket = new DatagramSocket();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T send(String requestType, Serializable data) throws Exception {
		
		RequestJava request = new RequestJava(requestType, data);
		byte [] bufferToSend = UdpUtils.getByteArray((Serializable)request);
		DatagramPacket packageSend = new DatagramPacket(bufferToSend, bufferToSend.length, serverHost, port);

		socket.send(packageSend);
		
		byte[] bufferReceive = new byte[MAX_PACKEGE_LENGTH];
		DatagramPacket packageReceive = new DatagramPacket(bufferReceive, MAX_PACKEGE_LENGTH);
		socket.receive(packageReceive);
		ResponseJava response = (ResponseJava)UdpUtils.getSerializableFromByteArray(packageReceive.getData(),
				packageReceive.getLength());

		if(response.code != ResponseCode.OK) {
			throw (Exception)response.data;
		}
		return (T) response.data;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
