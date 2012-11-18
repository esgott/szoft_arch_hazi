package hu.laborreg.server.http;

import hu.laborreg.server.handlers.ClientConnectionHandler;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpService;

public class HttpRequestListenerThread implements Runnable {

	private final ServerSocket serverSocket;
	private final HttpParams params = new SyncBasicHttpParams();;
	private final HttpService httpService;
	private final HttpFactory factory;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public HttpRequestListenerThread(int port, final String docRoot, final HttpFactory httpFactory,
			final ClientConnectionHandler clientConnHandler) throws IOException {
		factory = httpFactory;
		serverSocket = factory.createServerSocket(port);

		setParameters();

		httpService = factory.createHttpService(docRoot, params, clientConnHandler);
	}

	private void setParameters() {
		params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);
		params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
		params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
		params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpCore/4.2.2");
	}

	@Override
	public void run() {
		logger.info("Http Server listening on port " + serverSocket.getLocalPort());
		while (!Thread.interrupted()) {
			try {
				Socket socket = serverSocket.accept();
				DefaultHttpServerConnection connection = factory.createHttpServerConnection();
				logger.info("Incomming connection from " + socket.getInetAddress());
				connection.bind(socket, params);

				Thread thread = factory.createThread(httpService, connection);
				thread.start();
			} catch (InterruptedIOException e) {
				logger.fine("InterruptedIOException happened: " + e.getMessage());
				break;
			} catch (IOException e) {
				logger.severe("Error in initializing connetion thread: " + e.getMessage());
				break;
			}
		}
	}

}
