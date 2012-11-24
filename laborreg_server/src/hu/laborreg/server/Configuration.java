package hu.laborreg.server;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Configuration extends Properties {

	private static final long serialVersionUID = 7870462862521472713L;
	
	public static final String dbFile = "db_file";
	public static final String logFile = "log_file";
	public static final String httpServerPort = "http_server_port";
	public static final String htmlRoot = "client_html_page";
	public static final String smallestIpAddress = "smallest_ip_address";
	public static final String biggestIpAddress = "biggest_ip_address";
	
	private Options options = new Options();
	
	public Configuration(String[] args) {
		setDefaultParameters();
		addOptions();
		parseForOptions(args);
	}

	private void setDefaultParameters() {
		defaults = new Properties();
		defaults.setProperty(dbFile, "data.db");
		defaults.setProperty(logFile, "log.txt");
		defaults.setProperty(httpServerPort, "8000");
		defaults.setProperty(htmlRoot, "../laborreg_client/");
		defaults.setProperty(smallestIpAddress, "100.101.102.103");
		defaults.setProperty(biggestIpAddress, "200.201.202.203");
	}
	
	private void addOptions() {
		options.addOption("h", "help", false, "Displays this message");
		options.addOption("d", "db-file", true, "Specifies the file that stores the DB. Default: data.db");
		options.addOption("l", "log-file", true, "Specifies the file that stores the logs. Default: log.txt");
		options.addOption("p", "http-server-port", true, "Specifies the port, that the HTTP server will listen on.");
		options.addOption("r", "html-root", true, "Specifies the directory, which contains the HTML client.");
		options.addOption("s", "smallest-ip-address", true, "Tha lower end of the accepted IP range");
		options.addOption("b", "biggest-ip-address", true, "Tha higher end of the accepted IP range");
	}
	
	private void parseForOptions(String[] args) {
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("laborreg_server", options);
			} else if (line.hasOption("db-file")) {
				setProperty(dbFile, line.getOptionValue("db-file"));
			} else if (line.hasOption("log-file")) {
				setProperty(logFile, line.getOptionValue("log-file"));
			} else if (line.hasOption("http-server-port")) {
				setProperty(httpServerPort, line.getOptionValue("http-server-port"));
			} else if (line.hasOption("html-root")) {
				setProperty(htmlRoot, line.getOptionValue("html-root"));
			} else if (line.hasOption("smallest-ip-address")) {
				setProperty(smallestIpAddress, line.getOptionValue("smallest-ip-address"));
			} else if (line.hasOption("biggest-ip-address")) {
				setProperty(biggestIpAddress, line.getOptionValue("biggest-ip-address"));
			}
		} catch (ParseException e) {
			System.out.println("Unexpected exception during argument parsing");
			e.printStackTrace();
		}
	}

}
