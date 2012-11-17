package hu.laborreg.server;

import java.util.Properties;

public class Configuration extends Properties {

	private static final long serialVersionUID = 7870462862521472713L;
	
	public static final String dbFile = "db_file";
	public static final String logFile = "log_file";
	public static final String httpServerPort = "http_server_port";
	public static final String htmlRoot = "client_html_page";
	
	public Configuration() {
		defaults = new Properties();
		defaults.setProperty(dbFile, "data.db");
		defaults.setProperty(logFile, "log.txt");
		defaults.setProperty(httpServerPort, "8000");
		defaults.setProperty(htmlRoot, "../laborreg_client/");
	}

}
