package org.kopi.ebics.http.h005;

import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public interface HttpClient {

    /**
     * Actual configuration of the client
     */
    HttpClientRequestConfiguration getConfiguration();

    /**
     * Send HTTP request to given URL, using header if indicated
     * Return response bytes if the code is HTTP_OK (200),
     * Otherwise throw exception
     */
    ByteArrayContentFactory send(HttpClientRequest request) throws HttpClientException, IOException, URISyntaxException;

    default ByteArrayContentFactory send(URL requestURL, ContentFactory content) throws HttpClientException, IOException, URISyntaxException {
        return send(new HttpClientRequest(requestURL, content));
    }

    /**
     * Gracefully close the client
     */
    void close();
}
