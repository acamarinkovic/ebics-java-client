package org.kopi.ebics.exception.h005;

import org.kopi.ebics.exception.EbicsException;

public class HttpClientException extends EbicsException {
    public HttpClientException(String message, Exception exception) {
        super(message, exception);
    }
}
