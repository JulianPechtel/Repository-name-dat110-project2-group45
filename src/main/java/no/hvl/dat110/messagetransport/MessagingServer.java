package no.hvl.dat110.messagetransport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MessagingServer {

	private ServerSocket welcomeSocket;

	public MessagingServer(int port) {

		try {
			// Create server socket with reuse enabled (important for repeated tests)
			this.welcomeSocket = new ServerSocket();
			this.welcomeSocket.setReuseAddress(true);
			this.welcomeSocket.bind(new InetSocketAddress(port));

		} catch (IOException ex) {
			System.out.println("Messaging server: " + ex.getMessage());
			ex.printStackTrace();
			this.welcomeSocket = null; // make sure it's null if setup failed
		}
	}

	// accept an incoming connection from a client
	public Connection accept() {

		// if server socket was not created (e.g., port already in use)
		if (welcomeSocket == null || welcomeSocket.isClosed()) {
			return null;
		}
	
		try {
			Socket connectionSocket = welcomeSocket.accept();
			return new Connection(connectionSocket);
		} catch (IOException ex) {
			System.out.println("Messaging server: " + ex.getMessage());
			ex.printStackTrace();
			stop();               // close welcomeSocket safely
			return null;
		}
	}
	

	public void stop() {

		if (welcomeSocket != null) {
			try {
				welcomeSocket.close();
			} catch (IOException ex) {
				System.out.println("Messaging server: " + ex.getMessage());
				ex.printStackTrace();
			} finally {
				welcomeSocket = null;
			}
		}
	}
}
