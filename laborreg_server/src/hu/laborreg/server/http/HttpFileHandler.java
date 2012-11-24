package hu.laborreg.server.http;

import hu.laborreg.server.file.FileProvider;
import hu.laborreg.server.handlers.ClientConnectionHandler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class HttpFileHandler implements HttpRequestHandler {

	private final String docRoot;
	private final FileProvider fileProvider;
	private final ClientConnectionHandler clientConnHandler;
	private final ResponseProcessor responseProcessor = new ResponseProcessor();
	private HttpResponse response;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public HttpFileHandler(final String documentsRoot, final FileProvider provider,
			final ClientConnectionHandler connHandler) {
		docRoot = documentsRoot;
		fileProvider = provider;
		clientConnHandler = connHandler;
	}

	@Override
	public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context)
			throws MethodNotSupportedException, IOException {
		this.response = response;
		checkMethod(request);

		String target = request.getRequestLine().getUri();
		String[] parts = target.split("\\?");
		String targetFile = parts[0];
		if (parts.length > 1) {
			Charset charset = Charset.forName("UTF-8");
			List<NameValuePair> parameters = URLEncodedUtils.parse(parts[1], charset);
			respondToParameters(targetFile, parameters, context);
		} else {
			File file = getRequestedFile(targetFile);
			if (!file.exists()) {
				replyFileNotFound(file);
			} else if (!file.canRead() || file.isDirectory()) {
				replyAccessDenied(file);
			} else {
				replyOk(file);
			}
		}
	}

	private void checkMethod(final HttpRequest request) throws MethodNotSupportedException {
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
			String message = method + " method not supported";
			logger.info(message);
			throw new MethodNotSupportedException(message);
		}
	}

	private void respondToParameters(final String fileName, final List<NameValuePair> parameters,
			final HttpContext httpContext) {
		String properFileName = "index.html";
		File file = fileProvider.requestFile(docRoot, fileName);
		if ((!fileName.contentEquals(properFileName) && !fileName.contentEquals("/")) || parameters.size() != 2) {
			replyFileNotFound(file);
			return;
		}
		NameValuePair neptunParameter = parameters.get(0);
		NameValuePair labEventParameter = parameters.get(1);
		if (!neptunParameter.getName().equals("neptun") || !labEventParameter.getName().equals("labevent")) {
			replyAccessDenied(file);
			return;
		}
		String ipAddress = getIpAddress(httpContext);
		String neptun = neptunParameter.getValue();
		String labEventName = labEventParameter.getValue();
		String message = clientConnHandler.signInForLabEvent(neptun, labEventName, ipAddress);
		replyWithMessage(message, HttpStatus.SC_OK);
		logger.info("Message for sign in sent back: " + message);
	}

	private String getIpAddress(HttpContext httpContext) {
		Object attribute = httpContext.getAttribute("http.connection");
		if (attribute instanceof DefaultHttpServerConnection) {
			@SuppressWarnings("resource")
			DefaultHttpServerConnection serverConnetion = (DefaultHttpServerConnection) attribute;
			InetAddress ipAddress = serverConnetion.getRemoteAddress();
			return ipAddress.getHostAddress();
		} else {
			logger.warning("Failed to detect IP address of client");
			return "";
		}
	}

	private File getRequestedFile(final String target) throws UnsupportedEncodingException {
		File file = fileProvider.requestFile(docRoot, URLDecoder.decode(target, "UTF-8"));
		if (file.isDirectory()) {
			file = fileProvider.requestFile(file, "index.html");
		}
		return file;
	}

	private void replyFileNotFound(final File file) {
		String message = "File" + file.getPath() + " not found";
		replyWithMessage(message, HttpStatus.SC_NOT_FOUND);
		logger.info("File " + file.getPath() + " not found");
	}

	private void replyAccessDenied(final File file) {
		String message = "Access denied";
		replyWithMessage(message, HttpStatus.SC_FORBIDDEN);
		logger.info("Cannot read file " + file.getPath());
	}

	private void replyWithMessage(String message, int statusCode) {
		response.setStatusCode(statusCode);
		ContentType utf8ContentType = ContentType.create("text/html", "UTF-8");
		String page = responseProcessor.processHtmlResponse(message);
		StringEntity entity = new StringEntity(page, utf8ContentType);
		response.setEntity(entity);
	}

	private void replyOk(final File file) {
		response.setStatusCode(HttpStatus.SC_OK);
		FileEntity body = fileProvider.createFileEntity(file);
		response.setEntity(body);
		logger.fine("Serving file " + file.getPath());
	}
}
