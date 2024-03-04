package org.kopi.ebics.http.h005;

import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.http.h005.factory.IHttpClientRequest;

import java.net.URL;

public class HttpClientRequest implements IHttpClientRequest {

    private URL requestURL;
    private ContentFactory content;

    public HttpClientRequest(URL requestURL, ContentFactory content) {
        this.requestURL = requestURL;
        this.content = content;
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
}
