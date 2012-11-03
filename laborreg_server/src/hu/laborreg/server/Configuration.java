package hu.laborreg.server;

import java.util.Properties;

public class Configuration extends Properties {

	private static final long serialVersionUID = 7870462862521472713L;
	
	public static final String dbFile = "db_file";
	
	public Configuration() {
		defaults = new Properties();
		defaults.setProperty(dbFile, "data.db");
	}

}
