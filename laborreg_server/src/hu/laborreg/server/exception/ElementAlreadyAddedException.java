package hu.laborreg.server.exception;

public class ElementAlreadyAddedException extends Exception{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8892628884282008165L;

	public ElementAlreadyAddedException(String msg)
	{
		super(msg);
	}

}