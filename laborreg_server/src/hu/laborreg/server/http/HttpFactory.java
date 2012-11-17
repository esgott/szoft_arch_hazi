package hu.laborreg.server.http;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class HttpFactory {

	public ServerSocket createServerSocket(int port) throws IOException {
		return new ServerSocket(port);
	}

	public HttpService createHttpService(final String docRoot, final HttpParams params) {
		HttpResponseInterceptor[] interceptors = { new ResponseDate(), new ResponseServer(), new ResponseContent(),
				new ResponseConnControl() };
		HttpProcessor httpProcessor = new ImmutableHttpProcessor(interceptors);
		HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
		registry.register("*", new HttpFileHandler(docRoot));
		ConnectionReuseStrategy requestStrategy = new DefaultConnectionReuseStrategy();
		HttpResponseFactory responseFactory = new DefaultHttpResponseFactory();
		return new HttpService(httpProcessor, requestStrategy, responseFactory, registry, params);
	}

	public DefaultHttpServerConnection createHttpServerConnection() {
		return new DefaultHttpServerConnection();
	}

	public HttpWorkerThread createHttpWorkerThread(final HttpService service,
			final HttpServerConnection httpServerConnection) {
		return new HttpWorkerThread(service, httpServerConnection);
	}

	public Thread createThread(final HttpService service, final HttpServerConnection httpServerConnection) {
		HttpWorkerThread workerThread = createHttpWorkerThread(service, httpServerConnection);
		Thread thread = new Thread(workerThread);
		thread.setDaemon(true);
		return thread;
	}

}
