package org.kopi.ebics.http.h005;

import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.http.h005.factory.IHttpClientRequest;

import java.net.URL;

public class TracedHttpClientRequest implements IHttpClientRequest {

    private URL requestURL;
    private ContentFactory content;
    private IBankConnectionTraceSession traceSession;

    public TracedHttpClientRequest(URL requestURL, ContentFactory content, IBankConnectionTraceSession traceSession) {
        this.requestURL = requestURL;
        this.content = content;
        this.traceSession = traceSession;
    }

    public void setRequestURL(URL requestURL) {
        this.requestURL = requestURL;
    }

    public void setContent(ContentFactory content) {
        this.content = content;
    }

    @Override
    public URL getRequestURL() {
        return this.requestURL;
    }

    @Override
    public ContentFactory getContent() {
        return this.content;
    }

    public IBankConnectionTraceSession getTraceSession() {
        return traceSession;
    }

    public void setTraceSession(IBankConnectionTraceSession traceSession) {
        this.traceSession = traceSession;
    }
}
