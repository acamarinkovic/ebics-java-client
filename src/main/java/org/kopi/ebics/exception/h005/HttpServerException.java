package org.kopi.ebics.exception.h005;

public class HttpServerException extends EbicsException implements IErrorCodeText{

    private final String errorCode;
    private final String errorCodeText;

    public HttpServerException(String errorCode, String errorCodeText, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorCodeText = errorCodeText;
    }

    @Override
    public String getErrorCode() {
        return null;
    }

    @Override
    public String getErrorCodeText() {
        return null;
    }
}
