package hu.laborreg.server.exception;

public class WrongTimeFormatException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2473630614234257473L;

	public WrongTimeFormatException(String msg)
	{
		super(msg);
	}

}
