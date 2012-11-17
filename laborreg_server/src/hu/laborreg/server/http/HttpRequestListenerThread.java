package hu.laborreg.server.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class HttpRequestListenerThread implements Runnable {

	private final ServerSocket serverSocket;
	private final HttpParams params = new SyncBasicHttpParams();;
	private final HttpService httpService;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public HttpRequestListenerThread(int port, final String docRoot) throws IOException {
		serverSocket = new ServerSocket(port);

		setParameters();

		HttpResponseInterceptor[] interceptors = { new ResponseDate(), new ResponseServer(), new ResponseContent(),
				new ResponseConnControl() };
		HttpProcessor httpProcessor = new ImmutableHttpProcessor(interceptors);
		HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
		registry.register("*", new HttpFileHandler(docRoot));
		ConnectionReuseStrategy requestStrategy = new DefaultConnectionReuseStrategy();
		HttpResponseFactory responseFactory = new DefaultHttpResponseFactory();
		httpService = new HttpService(httpProcessor, requestStrategy, responseFactory, registry, params);
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
				DefaultHttpServerConnection connection = new DefaultHttpServerConnection();
				logger.info("Incomming connection from " + socket.getInetAddress());
				connection.bind(socket, params);

				HttpWorkerThread workerThread = new HttpWorkerThread(httpService, connection);
				Thread thread = new Thread(workerThread);
				thread.setDaemon(true);
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
