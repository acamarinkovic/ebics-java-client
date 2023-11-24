package org.kopi.ebics.http.h005.factory;

import org.apache.http.impl.client.CloseableHttpClient;
import org.kopi.ebics.http.h005.SimpleHttpClient;
import org.kopi.ebics.http.h005.HttpClientRequestConfiguration;

public class PooledSimpleHttpClientFactory extends AbstractPooledHttpClientFactory<SimpleHttpClient> {

    public PooledSimpleHttpClientFactory(IHttpClientGlobalConfiguration config) {
        super(config);
    }

    @Override
    public SimpleHttpClient instantiateHttpClient(CloseableHttpClient httpClient, HttpClientRequestConfiguration configuration, String configurationName) {
        return new SimpleHttpClient(httpClient, configuration, configurationName);
    }
}
