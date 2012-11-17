package hu.laborreg.server.http;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HttpWorkerThreadTest {

	@Mock
	HttpService mockHttpService;
	@Mock
	HttpServerConnection mockhttpServerConnection;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void connectionShutdownHappenesAfterConnectionClosed() throws IOException {
		HttpWorkerThread workerThread = new HttpWorkerThread(mockHttpService, mockhttpServerConnection);

		when(mockhttpServerConnection.isOpen()).thenReturn(true, true, true, true, false);

		workerThread.run();

		verify(mockhttpServerConnection).shutdown();
	}

	@Test
	public void connectionShutdownHappenesAfterException() throws IOException, HttpException {
		HttpWorkerThread workerThread = new HttpWorkerThread(mockHttpService, mockhttpServerConnection);

		when(mockhttpServerConnection.isOpen()).thenReturn(true);
		doThrow(new ConnectionClosedException("")).when(mockHttpService).handleRequest(eq(mockhttpServerConnection),
				any(HttpContext.class));

		workerThread.run();

		verify(mockhttpServerConnection).shutdown();
	}
}
