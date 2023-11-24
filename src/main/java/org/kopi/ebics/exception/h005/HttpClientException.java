package org.kopi.ebics.exception.h005;

public class HttpClientException extends EbicsException {
    public HttpClientException(String message, Exception exception) {
        super(message, exception);
    }
}
