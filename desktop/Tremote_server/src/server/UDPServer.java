package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class UDPServer {
	
	private final int BUFFER_SIZE = 256;
	private final String CONNECTION_ACCEPT_CODE = "0001";
	private final String KEY_RIGHT_CODE = "0002";
	private final String KEY_LEFT_CODE = "0003";
	private final String EXIT_CODE = "0004";
	public final static int DEFAULT_PORT = 2050;

	private DatagramSocket socket;
	public int port;
	public String password;
	private byte intents = 0;
	private String clientIP;
	
	/* Initializes a server with the given port and sets a PIN code */
	public UDPServer(int port) throws SocketException, IOException {
		this.port = port;
		this.socket = new DatagramSocket(this.port);
		this.password = setPassword();
	}
	
	/* Listen for key presses in an infinite loop */
	private void listen() throws Exception{
		System.out.println("PIN code accepted, listening for data.");
		String msg;
		String sentPassword;
		String keyCode;
		
		while(true) {
			byte[] buffer = new byte[BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			msg = new String(packet.getData()).trim();
			sentPassword = msg.substring(0, msg.indexOf("#"));
			keyCode = msg.substring(msg.indexOf("#") + 1);
			if(sentPassword.equals(password)) { // check password for security
				switch(keyCode) {
				case KEY_RIGHT_CODE:
					//TODO: simulate key right
					System.out.println("KEY RIGHT PRESSED");
					break;
				case KEY_LEFT_CODE:
					//TODO: simulate key left
					System.out.println("KEY LEFT PRESSED");
					break;
				case EXIT_CODE:
					System.out.println("EXIT PRESSED");
					exit();
					// exit
					break;
				default:
					// unknown message, discard and log it
					System.out.println("MSG RECEIVED ERROR: " + keyCode);
				}
			}
		}
	
	}
	
	/* Returns a 4 digit PIN for the session */
	private String setPassword() {
		return Integer.toString(1000 + new Random().nextInt(9000));
	}
	
	/* Sets a new PIN code and relaunches the server */
	private void relaunch() throws Exception{
		System.out.println("PIN code rejected three times. Relaunching...");
		this.password = setPassword();
		intents = 0;
		establishConnection();
	}
	
	/* Accepts PIN code from phone, it has to be equal to the PIN given to the user */
	private boolean connect() throws Exception{
		System.out.println("Waiting for PIN code...");
		String msg;
		byte[] buffer = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		msg = new String(packet.getData()).trim();
		String[] msgDecoded = msg.split("#");
		clientIP = msgDecoded[2];
		
		if(msgDecoded[0].equals(password) && msgDecoded[1].equals(CONNECTION_ACCEPT_CODE)) return true;
		else return false;
	}
	
	/* Tries to establish the connection between the server and the phone */
	public void establishConnection() throws Exception{
		System.out.println("Created UDP Server with port: " + port + " and PIN: " + password);
		System.out.println("Address: " + InetAddress.getLocalHost().toString());
		System.out.println("Client address: " + clientIP);
		if(connect()) {
			byte[] sendData = new byte[BUFFER_SIZE];
			sendData = "ACK".getBytes();
			InetAddress phoneAddress = InetAddress.getByName(clientIP);
			socket.send(new DatagramPacket(sendData, sendData.length, phoneAddress, DEFAULT_PORT));
			listen();
		}
		else {
			intents++;
			if(intents >= 3) relaunch();
			else establishConnection();
		}
	}
	
	/* Closes the UDP socket and exits */
	private void exit() {
		socket.close();
		System.exit(0);
	}
}
