package org.kopi.ebics.enumeration.h005;

import org.kopi.ebics.exception.h005.EbicsServerException;
import org.kopi.ebics.exception.h005.HttpServerException;

public enum TraceCategory {
    Request,
    RequestError,
    RequestOk,
    HttpResponseOk,
    HttpResponseError,
    EbicsResponseOk,
    EbicsResponseError,
    GeneralError;

    public static TraceCategory fromException(Exception exception) {
        if (exception instanceof EbicsServerException) {
            return EbicsResponseError;
        } else if (exception instanceof HttpServerException) {
            return HttpResponseError;
        } else {
            return GeneralError;
        }
    }
}
