package hu.laborreg.server;

public abstract class Constants
{
	public static final String IP_ADDRESS_DELIMITER = "\\."; //regexp
	public static final String VALID_IP_ADDRESS_PATTERN = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	public static String SMALLEST_VALID_IP_ADDRESS = "100.101.102.103";
	public static String BIGGEST_VALID_IP_ADDRESS = "200.201.202.203";
}
