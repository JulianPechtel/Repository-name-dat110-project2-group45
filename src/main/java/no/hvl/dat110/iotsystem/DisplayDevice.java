package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;

public class DisplayDevice {
	
	private static final int COUNT = 10;
		
	public static void main (String[] args) {
		
		System.out.println("Display starting ...");
		
		Client client = new Client("display", "localhost", 8080);

		client.connect();

		client.createTopic("temperature");
		client.subscribe("temperature");

		for (int i = 0; i < COUNT; i++) {
			Message msg = client.receive();

			if (msg instanceof PublishMsg) {
				PublishMsg pmsg = (PublishMsg) msg;
				System.out.println("DISPLAY:" + pmsg.getMessage());
			}
		}

		client.unsubscribe("temperature");
		client.disconnect();
		
		System.out.println("Display stopping ... ");
	}
}
