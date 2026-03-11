package no.hvl.dat110.broker;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.Logger;
import no.hvl.dat110.messagetransport.Connection;

public class Storage {

	// maps from a topic to set of subscribed users
	protected ConcurrentHashMap<String, Set<String>> subscriptions;

	// maps from user to corresponding client session object
	protected ConcurrentHashMap<String, ClientSession> clients;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
	}

	public Collection<ClientSession> getSessions() {
		return clients.values();
	}

	public Set<String> getTopics() {
		return subscriptions.keySet();
	}

	public ClientSession getSession(String user) {
		return clients.get(user);
	}

	public Set<String> getSubscribers(String topic) {
		return subscriptions.get(topic);
	}

	public void addClientSession(String user, Connection connection) {

		// create and register a new session for the user
		ClientSession session = new ClientSession(user, connection);
		clients.put(user, session);

		Logger.log("Storage: added client session for " + user);
	}

	public void removeClientSession(String user) {

		// remove session from storage
		ClientSession session = clients.remove(user);

		// disconnect if it existed
		if (session != null) {
			session.disconnect();
		}

		// also remove user from all topic subscription sets (cleanup)
		for (String topic : subscriptions.keySet()) {
			Set<String> subs = subscriptions.get(topic);
			if (subs != null) {
				subs.remove(user);
			}
		}

		Logger.log("Storage: removed client session for " + user);
	}

	public void createTopic(String topic) {

		// create topic if it does not exist
		subscriptions.putIfAbsent(topic, ConcurrentHashMap.newKeySet());

		Logger.log("Storage: created topic " + topic);
	}

	public void deleteTopic(String topic) {

		subscriptions.remove(topic);

		Logger.log("Storage: deleted topic " + topic);
	}

	public void addSubscriber(String user, String topic) {

		// ensure topic exists, then add user to subscriber set
		subscriptions.computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet()).add(user);

		Logger.log("Storage: " + user + " subscribed to " + topic);
	}

	public void removeSubscriber(String user, String topic) {

		Set<String> subs = subscriptions.get(topic);
		if (subs != null) {
			subs.remove(user);
		}

		Logger.log("Storage: " + user + " unsubscribed from " + topic);
	}
}
