package hu.laborreg.server.handlers;

import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.labEvent.LabEventContainer;

import java.util.logging.Logger;

public class ClientConnectionHandler {

	private final LabEventContainer labEvents;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * This class handles the messages sent by client and the messages which the
	 * server wants to send to the client.
	 */
	public ClientConnectionHandler(LabEventContainer labEventContainer) {
		labEvents = labEventContainer;
	}

	/**
	 * This function has to be called when a student tries to sign in for a
	 * labevent with his/her NEPTUN code.
	 * 
	 * @param neptun
	 *            The NEPTUN code of the student
	 * @param ipAddress2
	 * @param The
	 *            IP address of the computer from which the student has singed
	 *            in
	 * @return Message for the student
	 */
	public String signInForLabEvent(String neptun, String labEventName, String ipAddress) {
		logger.info("Sign in request with NEPTUN code " + neptun + " with IP address " + ipAddress + " for "
				+ labEventName + " labEvent.");
		try {
			labEvents.signInForLabEvent(labEventName, neptun);
		} catch (ElementNotFoundException e) {
			logger.info("Requested thing not found: " + e.getMessage());
			return "Jelentkezés sikertelen";
		} catch (ElementAlreadyAddedException e) {
			logger.info("Sign in already happened: " + e.getMessage());
			return "Jelentkezés már korábban megtörtént";
		}
		return "Sikeres jelentkezés";
	}
}
