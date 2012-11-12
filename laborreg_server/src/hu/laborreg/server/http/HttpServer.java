package hu.laborreg.server.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class HttpServer implements Runnable {

	private Socket connectedClient;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private String httpMethod;
	private String httpQuery;
	private String responseFileName;
	private String errorFileName;
	private Logger logger;

	public HttpServer(Socket client, String clientHtmlPageName, String errorHtmlPageName) {
		connectedClient = client;
		logger = Logger.getLogger(this.getClass().getName());
		responseFileName = clientHtmlPageName;
		errorFileName = errorHtmlPageName;
	}

	@Override
	public void run() {
		logger.info("Client " + connectedClient.getInetAddress() + " connected.");
		try {
			createReader();
			readRequest();
			createWriter();
			if (httpMethod.equals("GET") && httpQuery.equals("/")) {
				sendResponse();
			} else {
				sendError();
			}
		} catch (IOException e) {
			logger.warning("Failed to fulfill request, because: " + e.getMessage());
		}
	}

	private void createReader() throws IOException {
		InputStream inputStream = connectedClient.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		inFromClient = new BufferedReader(inputStreamReader);
	}

	private void readRequest() throws IOException {
		String requestString = inFromClient.readLine();
		StringTokenizer tokenizer = new StringTokenizer(requestString);
		httpMethod = tokenizer.nextToken();
		httpQuery = tokenizer.nextToken();
	}

	private void createWriter() throws IOException {
		outToClient = new DataOutputStream(connectedClient.getOutputStream());
	}

	private void sendResponse() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(responseFileName);
		String statusLine = "HTTP/1.1 200 OK" + "\r\n";
		String contentLengthLine = "Content-Length: " + Integer.toString(fileInputStream.available()) + "\r\n";
		sendResponse(statusLine, contentLengthLine, fileInputStream);
	}

	private void sendError() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(errorFileName);
		String statusLine = "HTTP/1.1 404 Not Found" + "\r\n";
		String contentLengthLine = "Content-Length: " + Integer.toString(fileInputStream.available()) + "\r\n";
		sendResponse(statusLine, contentLengthLine, fileInputStream);
	}

	private void sendResponse(String statusLine, String contentLengthLine, FileInputStream file) throws IOException {
		outToClient.writeBytes(statusLine);
		outToClient.writeBytes("Server: Java HTTPServer");
		outToClient.writeBytes("Content-Type: text/html" + "\r\n");
		outToClient.writeBytes(contentLengthLine);
		outToClient.writeBytes("Connection: close\r\n");
		outToClient.writeBytes("\r\n");

		if (file != null) {
			writeFile(file);
		}

		outToClient.close();
	}

	private void writeFile(FileInputStream file) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = file.read(buffer)) != -1) {
			outToClient.write(buffer, 0, bytesRead);
		}

		file.close();
	}

}
