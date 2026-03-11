package no.hvl.dat110.messagetransport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MessagingServer {

    private ServerSocket welcomeSocket;

    public MessagingServer(int port) {

        try {

            // create server socket and allow port reuse (important for tests)
            welcomeSocket = new ServerSocket();
            welcomeSocket.setReuseAddress(true);
            welcomeSocket.bind(new InetSocketAddress(port));

        } catch (IOException ex) {

            System.out.println("Messaging server: " + ex.getMessage());
            ex.printStackTrace();
            welcomeSocket = null;   // ensure null if creation failed
        }
    }

    // accept an incoming connection from a client
    public Connection accept() {

        // if socket is not available, return null
        if (welcomeSocket == null || welcomeSocket.isClosed()) {
            return null;
        }

        try {

            Socket connectionSocket = welcomeSocket.accept();
            return new Connection(connectionSocket);

        } catch (IOException ex) {

            System.out.println("Messaging server: " + ex.getMessage());
            ex.printStackTrace();

            stop();   // close socket safely
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
