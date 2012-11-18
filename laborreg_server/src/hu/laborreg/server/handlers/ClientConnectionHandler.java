package hu.laborreg.server.handlers;

public class ClientConnectionHandler {

	/**
	 * This class handles the messages sent by client and the messages which the
	 * server wants to send to the client.
	 */
	public ClientConnectionHandler() {
		// TODO constructor. Do we need this?
	}

	/**
	 * This function has to be called when a student tries to sign in for a
	 * labevent with his/her NEPTUN code.
	 * 
	 * @param neptun
	 *            The NEPTUN code of the student
	 * @return Message for the student
	 */
	public String signInForLabEvent(String neptun) {
		return "Sikeres jelentkezés";
	}
}
