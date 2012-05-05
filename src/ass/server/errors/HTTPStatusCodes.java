package ass.server.errors;

public enum HTTPStatusCodes {
	BAD_REQUEST("HTTP/1.1 400 Bad Request\r\n"),
	NOT_FOUND("HTTP/1.1 404 Not Found\r\n"),
	ACCESS_DENIED("HTTP/1.1 401 Unauthorized\r\n"),
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
