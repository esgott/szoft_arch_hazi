package hu.laborreg.server.exception;

public class WrongIpAddressException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8044624115020602748L;
	
	public WrongIpAddressException(String msg)
	{
		super(msg);
	}

}
