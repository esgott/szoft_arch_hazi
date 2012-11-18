package hu.laborreg.server.http;

public class ResponseProcessor {
	
	private final static String HEAD = "<head><title>Jelenlét regisztráló rendszer</title><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head>\n";
	private final static String BEGIN = "<body><p>";
	private final static String END = "</p></body>";
	
	public String processHtmlResponse(String message) {
		return HEAD + BEGIN + message + END;
	}

}
