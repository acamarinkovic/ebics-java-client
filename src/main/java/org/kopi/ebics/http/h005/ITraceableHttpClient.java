package org.kopi.ebics.http.h005;

import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.http.h005.HttpClient;
import org.kopi.ebics.http.h005.HttpClientRequest;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.interfaces.h005.IBaseTraceSession;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;

import java.net.URL;

public interface ITraceableHttpClient extends HttpClient {

    ByteArrayContentFactory sendAndTrace(HttpClientRequest request, IBaseTraceSession traceSession) throws HttpClientException;

    default ByteArrayContentFactory sendAndTrace(URL requestURL, ContentFactory content, IBaseTraceSession traceSession) throws HttpClientException {
        return sendAndTrace(new HttpClientRequest(requestURL, content), traceSession);
    }
}
