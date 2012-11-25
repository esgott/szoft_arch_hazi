package hu.laborreg.server.http;

import hu.laborreg.server.file.FileProvider;
import hu.laborreg.server.handlers.ClientConnectionHandler;
import hu.laborreg.server.handlers.NotRegisteredException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
		if ((!fileName.contentEquals(properFileName) && !fileName.contentEquals("/"))
				|| (parameters.size() != 2 && parameters.size() != 3)) {
			replyFileNotFound(file);
			return;
		}
		NameValuePair neptunParameter = parameters.get(0);
		NameValuePair labEventParameter = parameters.get(1);
		boolean force = false;
		try {
			NameValuePair forceParameter = parameters.get(2);
			if (forceParameter.getName().equals("force") && forceParameter.getValue().equals("true")) {
				force = true;
			}
		} catch (IndexOutOfBoundsException e) {
			force = false;
		}
		if (!neptunParameter.getName().equals("neptun") || !labEventParameter.getName().equals("labevent")) {
			replyAccessDenied(file);
			return;
		}
		String ipAddress = getIpAddress(httpContext);
		String neptun = neptunParameter.getValue();
		String labEventName = labEventParameter.getValue();
		try {
			String message = clientConnHandler.signInForLabEvent(neptun, labEventName, ipAddress, force);
			replyWithMessage(message, HttpStatus.SC_OK);
			logger.info("Message for sign in sent back: " + message);
		} catch (NotRegisteredException e) {
			try {
				replyWithForce(neptun, labEventName);
				logger.info("Please acknowledge message sent back");
			} catch (ParserConfigurationException | SAXException | IOException | TransformerFactoryConfigurationError
					| TransformerException | XPathExpressionException e1) {
				replyFileNotFound(file);
				logger.warning("Failed to make answer: " + e.getMessage());
			}
		}
	}

	private void replyWithForce(String neptun, String labEventName) throws ParserConfigurationException, SAXException,
			IOException, TransformerFactoryConfigurationError, TransformerException, XPathExpressionException {
		File htmlFile = new File(docRoot, "force.html");
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(htmlFile);
		XPath xpath = XPathFactory.newInstance().newXPath();
		Element neptunInput = (Element) xpath.evaluate("//*[@id = 'neptuninput']", document, XPathConstants.NODE);
		neptunInput.setAttribute("value", neptun);
		Element labEventInput = (Element) xpath.evaluate("//*[@id = 'labeventinput']", document, XPathConstants.NODE);
		labEventInput.setAttribute("value", labEventName);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		Source source = new DOMSource(document);
		Result result = new StreamResult(stringWriter);
		transformer.transform(source, result);
		String transformedXml = stringWriter.getBuffer().toString();
		response.setStatusCode(HttpStatus.SC_OK);
		ContentType utf8ContentType = ContentType.create("text/html", "UTF-8");
		StringEntity entity = new StringEntity(transformedXml, utf8ContentType);
		response.setEntity(entity);
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
