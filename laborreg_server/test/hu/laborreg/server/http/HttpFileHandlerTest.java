package hu.laborreg.server.http;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.laborreg.server.file.FileProvider;
import hu.laborreg.server.handlers.ClientConnectionHandler;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HttpFileHandlerTest {

	private HttpFileHandler httpFileHandler;
	private final static String documentsRoot = "/foo/bar/";

	@Mock
	FileProvider mockFileProvider;
	@Mock
	ClientConnectionHandler mockClientConnHandler;
	@Mock
	HttpRequest mockHttpRequest;
	@Mock
	HttpResponse mockHttpResponse;
	@Mock
	HttpContext mockHttpContext;
	@Mock
	RequestLine mockRequestLine;
	@Mock
	File mockFile;
	@Mock
	FileEntity mockFileEntity;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

		httpFileHandler = new HttpFileHandler(documentsRoot, mockFileProvider, mockClientConnHandler);
	}

	@Test
	public void properResponseGivenForExistingFile() throws HttpException, IOException {
		String requestLine = "index.htm";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, requestLine)).thenReturn(mockFile);
		when(mockFile.isDirectory()).thenReturn(false);
		when(mockFile.exists()).thenReturn(true);
		when(mockFile.canRead()).thenReturn(true);
		when(mockFile.isDirectory()).thenReturn(false);
		when(mockFileProvider.createFileEntity(mockFile)).thenReturn(mockFileEntity);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_OK);
		verify(mockHttpResponse).setEntity(mockFileEntity);
	}
	
	@Test
	public void properResponseGivenForNeptunParameter() throws HttpException, IOException {
		String fileName = "index.html";
		String neptun = "jqumgw";
		String requestLine = fileName + "?neptun=" + neptun;
		String message = "abcde";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, fileName)).thenReturn(mockFile);
		when(mockClientConnHandler.signInForLabEvent(neptun, "")).thenReturn(message);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_OK);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}
	
	@Test
	public void fileNotFoundRepliedForWrongFilenameAndOneParameter() throws HttpException, IOException {
		String fileName = "wrong.html";
		String requestLine = fileName + "?neptun=jqumgw";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, fileName)).thenReturn(mockFile);
		when(mockFile.getPath()).thenReturn(fileName);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_NOT_FOUND);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}
	
	@Test
	public void fileNotFoundRepliedForWrongFilenameAndMultipleParameters() throws HttpException, IOException {
		String fileName = "wrong.html";
		String requestLine = fileName + "?neptun=jqumgw&abc=def&jkl=hjk";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, fileName)).thenReturn(mockFile);
		when(mockFile.getPath()).thenReturn(fileName);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_NOT_FOUND);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}
	
	@Test
	public void fileNotFoundRepliedForGoodFilenameAndMultipleParameters() throws HttpException, IOException {
		String fileName = "index.html";
		String requestLine = fileName + "?neptun=jqumgw&abc=def&jkl=hjk";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, fileName)).thenReturn(mockFile);
		when(mockFile.getPath()).thenReturn(fileName);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_NOT_FOUND);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}
	
	@Test
	public void accessDeniedRepliedForGoodFilenameAndWrongParameter() throws HttpException, IOException {
		String fileName = "index.html";
		String requestLine = fileName + "?abcd=jqumgw";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, fileName)).thenReturn(mockFile);
		when(mockFile.getPath()).thenReturn(fileName);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_FORBIDDEN);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}

	@Test
	public void properResponseGivenForExistingDirectory() throws HttpException, IOException {
		File mockDirectory = mock(File.class);
		String requestLine = "/a/";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, requestLine)).thenReturn(mockDirectory);
		when(mockDirectory.isDirectory()).thenReturn(true);
		when(mockFileProvider.requestFile(mockDirectory, "index.html")).thenReturn(mockFile);
		when(mockFile.exists()).thenReturn(true);
		when(mockFile.canRead()).thenReturn(true);
		when(mockFile.isDirectory()).thenReturn(false);
		when(mockFileProvider.createFileEntity(mockFile)).thenReturn(mockFileEntity);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_OK);
		verify(mockHttpResponse).setEntity(mockFileEntity);
	}

	@Test
	public void errorGivenToNonExistentFile() throws HttpException, IOException {
		String requestLine = "index.htm";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, requestLine)).thenReturn(mockFile);
		when(mockFile.isDirectory()).thenReturn(false);
		when(mockFile.exists()).thenReturn(false);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_NOT_FOUND);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}

	@Test
	public void errorGivenToNonAccessibleFile() throws HttpException, IOException {
		String requestLine = "index.htm";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("GET");
		when(mockFileProvider.requestFile(documentsRoot, requestLine)).thenReturn(mockFile);
		when(mockFile.isDirectory()).thenReturn(false);
		when(mockFile.exists()).thenReturn(true);

		httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);

		verify(mockHttpResponse).setStatusCode(HttpStatus.SC_FORBIDDEN);
		verify(mockHttpResponse).setEntity(any(StringEntity.class));
	}

	@Test
	public void unsupprtedMethodCausesException() throws IOException {
		String requestLine = "index.htm";

		when(mockHttpRequest.getRequestLine()).thenReturn(mockRequestLine);
		when(mockRequestLine.getUri()).thenReturn(requestLine);
		when(mockRequestLine.getMethod()).thenReturn("XYZ");

		try {
			httpFileHandler.handle(mockHttpRequest, mockHttpResponse, mockHttpContext);
		} catch (MethodNotSupportedException e) {
			return;
		}

		fail("Exception not thrown");
	}
}
