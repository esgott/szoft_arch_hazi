package hu.laborreg.server.http;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

public class HttpWorkerThread implements Runnable {

	private final HttpService httpService;
	private final HttpServerConnection connection;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public HttpWorkerThread(final HttpService service, final HttpServerConnection httpServerConnection) {
		httpService = service;
		connection = httpServerConnection;
	}

	@Override
	public void run() {
		logger.fine("New Connection Thread");
		HttpContext context = new BasicHttpContext();
		try {
			while (!Thread.interrupted() && connection.isOpen()) {
				httpService.handleRequest(connection, context);
			}
		} catch (ConnectionClosedException e) {
			logger.warning("Connection closed: " + e.getMessage());
		} catch (IOException e) {
			logger.warning("I/O error: " + e.getMessage());
		} catch (HttpException e) {
			logger.warning("Unrecoverable HTTP protocol violation: " + e.getMessage());
		} finally {
			try {
				connection.shutdown();
			} catch (IOException e) {
				logger.warning("Failed to shut down connection: " + e.getMessage());
			}
		}
	}

}
