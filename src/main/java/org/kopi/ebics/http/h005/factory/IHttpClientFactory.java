package org.kopi.ebics.http.h005.factory;

import org.apache.http.impl.client.CloseableHttpClient;
import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.http.h005.HttpClient;
import org.kopi.ebics.http.h005.HttpClientRequestConfiguration;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IHttpClientFactory<T extends HttpClient> {
    T getHttpClient(String configurationName);

    T instantiateHttpClient(CloseableHttpClient httpClient, HttpClientRequestConfiguration configuration, String configurationName);

    ByteArrayContentFactory sendRequest(IHttpTransferSession httpTransferSession, ContentFactory contentFactory) throws HttpClientException, IOException, URISyntaxException;
}
