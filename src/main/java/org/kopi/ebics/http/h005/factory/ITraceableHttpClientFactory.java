package org.kopi.ebics.http.h005.factory;

import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.http.h005.HttpClient;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;

public interface ITraceableHttpClientFactory<T extends HttpClient> extends IHttpClientFactory<T> {

    /**
     * Trace the request before its send, and then trace the response,
     * In case of any exception is the exception traced
     */
    ByteArrayContentFactory sendAndTraceRequest(IHttpTransferSession httpTransferSession, IBankConnectionTraceSession traceSession, ContentFactory contentFactory) throws HttpClientException;
}
