package hu.laborreg.server;

public class ClientConnectionHandler {
	
	/**
	 * This class handles the messages sent by client and the messages which the server wants to send to the client.
	 */
	public ClientConnectionHandler()
	{
		//TODO constructor. Do we need this?
	}
	
	/**
	 * Sends answer message to the student (after he/she sent his/her Neptun-code, and the server processed it).
	 * @param clientIpAddress IP address of the computer where the student sent the request from.
	 * @param clientPortNo Port number of the computer where the student sent the request from.
	 * @param message The message that will be displayed at the client side (the student will se this message).  
	 */
	public void sendMessageToClient(String clientIpAddress, int clientPortNo, String message)
	{
		//TODO send the message via http server
	}
}
