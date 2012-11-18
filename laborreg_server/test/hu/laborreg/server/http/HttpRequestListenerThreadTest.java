package hu.laborreg.server.http;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hu.laborreg.server.handlers.ClientConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HttpRequestListenerThreadTest {

	@Mock
	private HttpFactory mockHttpFactory;
	@Mock
	ClientConnectionHandler mockClientConnHandler;
	@Mock
	private ServerSocket mockServerSocket;
	@Mock
	private HttpService mockHttpService;
	@Mock
	private DefaultHttpServerConnection mockHttpServerConnection;
	@Mock
	private Socket mockSocket;
	@Mock
	private Thread mockThread;

	private final static int port = 1000;
	private final static String docRoot = "/foo/";
	private HttpRequestListenerThread listenerThread;

	@Before
	public void init() throws IOException {
		MockitoAnnotations.initMocks(this);

		when(mockHttpFactory.createServerSocket(port)).thenReturn(mockServerSocket);
		when(mockHttpFactory.createHttpService(eq(docRoot), any(HttpParams.class), eq(mockClientConnHandler)))
				.thenReturn(mockHttpService);

		listenerThread = new HttpRequestListenerThread(port, docRoot, mockHttpFactory, mockClientConnHandler);
	}

	@Test
	public void connectionEstablishedThenStopOnIOException() throws IOException {
		when(mockServerSocket.accept()).thenReturn(mockSocket).thenThrow(new IOException());
		when(mockHttpFactory.createHttpServerConnection()).thenReturn(mockHttpServerConnection);
		when(mockHttpFactory.createThread(mockHttpService, mockHttpServerConnection)).thenReturn(mockThread);

		listenerThread.run();

		verify(mockHttpServerConnection).bind(eq(mockSocket), any(HttpParams.class));
		verify(mockThread).start();
	}

}
