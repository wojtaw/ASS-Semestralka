package ass.server.errors;

public enum HTTPStatusCodes {
	BAD_REQUEST("HTTP/1.1 400 Bad Request\r\n"),
	NOT_FOUND("HTTP/1.1 404 Not Found\r\n"),
	ACCESS_DENIED("HTTP/1.1 401 Authorization Required\r\n" +
			"WWW-Authenticate: Basic realm=\"Secure Area\"\r\n" +
			"Content-Type: text/html\r\n" +
			"Content-Length: 311\r\n" +
			"\r\n" +
			"<HTML>\r\n" +
			"<HEAD>\r\n" +
			"<TITLE>Error</TITLE>\r\n" +
			"<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=ISO-8859-1\">\r\n" +
			"</HEAD>\r\n" +
			"<BODY><H1>401 Unauthorised.</H1></BODY>\r\n" +
			"</HTML>\r\n"),
	OK("HTTP/1.1 200 Ok\r\n");
	
	

    /**
     * @param text
     */
    private HTTPStatusCodes(final String text) {
        this.text = text;
    }

    private final String text;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return text;
    }	

}
