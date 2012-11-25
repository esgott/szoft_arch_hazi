package hu.laborreg.server.handlers;

import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
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
	 * @throws NotRegisteredException 
	 */
	public String signInForLabEvent(String neptun, String labEventName, String ipAddress, boolean forced) throws NotRegisteredException {
		logger.info("Sign in request with NEPTUN code " + neptun + " with IP address " + ipAddress + " for "
				+ labEventName + " labEvent.");
		boolean registered;
		try {
			registered = labEvents.signInForLabEvent(labEventName, neptun, forced);
		} catch (ElementNotFoundException e) {
			logger.info("Labevent not found: " + e.getMessage());
			return "Jelentkezés sikertelen: " + labEventName + " névvel nem létezik laboresemény.";
		} catch (ElementAlreadyAddedException e) {
			logger.info("Sign in already happened: " + e.getMessage());
			return "Jelentkezés már korábban megtörtént erre a laboreseményre: " + labEventName;
		} catch (TimeSetException e) {
			logger.info("Sign in rejected: " + e.getMessage());
			return "Nem lehetséges regisztrálni a " + labEventName + " laboreseményre, mert jelenleg nem aktív.";
		}

		if (registered) {
			return "Sikeres jelentkezés";
		} else {
			throw new NotRegisteredException();
		}
	}
}
