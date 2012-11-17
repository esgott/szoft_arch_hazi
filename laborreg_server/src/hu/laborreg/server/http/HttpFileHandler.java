package hu.laborreg.server.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class HttpFileHandler implements HttpRequestHandler {

	private final String docRoot;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public HttpFileHandler(final String documentsRoot) {
		docRoot = documentsRoot;
	}

	@Override
	public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context)
			throws HttpException, IOException {
		checkMethod(request);

		File file = getRequestedFile(request);
		if (!file.exists()) {
			replyFileNotFound(response, file);
		} else if (!file.canRead() || file.isDirectory()) {
			replyAccessDenied(response, file);
		} else {
			replyOk(response, file);
		}
	}

	private void checkMethod(final HttpRequest request) throws MethodNotSupportedException {
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
			String message = method + "method not supported";
			logger.info(message);
			throw new MethodNotSupportedException(message);
		}
	}

	private File getRequestedFile(final HttpRequest request) throws UnsupportedEncodingException {
		String target = request.getRequestLine().getUri();
		File file = new File(docRoot, URLDecoder.decode(target, "UTF-8"));
		if (file.isDirectory()) {
			file = new File(file, "index.html");
		}
		return file;
	}

	private void replyFileNotFound(final HttpResponse response, final File file) {
		ContentType utf8ContentType = ContentType.create("text/html", "UTF-8");
		response.setStatusCode(HttpStatus.SC_NOT_FOUND);
		String answer = "<html><body><h1>File" + file.getPath() + " not found</h1></body></html>";
		StringEntity entity = new StringEntity(answer, utf8ContentType);
		response.setEntity(entity);
		logger.info("File " + file.getPath() + " not found");
	}

	private void replyAccessDenied(final HttpResponse response, final File file) {
		ContentType utf8ContentType = ContentType.create("text/html", "UTF-8");
		response.setStatusCode(HttpStatus.SC_FORBIDDEN);
		StringEntity entity = new StringEntity("<html><body><h1>Access denied</h1></body></html>", utf8ContentType);
		response.setEntity(entity);
		logger.info("Cannot read file " + file.getPath());
	}

	private void replyOk(final HttpResponse response, final File file) {
		response.setStatusCode(HttpStatus.SC_OK);
		FileEntity body = new FileEntity(file, ContentType.create("text/html"));
		response.setEntity(body);
		logger.fine("Serving file " + file.getPath());
	}
}
