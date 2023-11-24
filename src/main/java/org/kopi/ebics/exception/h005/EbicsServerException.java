package org.kopi.ebics.exception.h005;

public class EbicsServerException extends EbicsException implements IErrorCodeText{
    private final EbicsReturnCode ebicsReturnCode;

    public EbicsServerException(EbicsReturnCode ebicsReturnCode) {
        super(ebicsReturnCode.toString());
        this.ebicsReturnCode = ebicsReturnCode;
    }

    public EbicsReturnCode getEbicsReturnCode() {
        return ebicsReturnCode;
    }

    @Override
    public String getErrorCode() {
        return ebicsReturnCode.code;
    }

    @Override
    public String getErrorCodeText() {
        return ebicsReturnCode.text;
    }
}
