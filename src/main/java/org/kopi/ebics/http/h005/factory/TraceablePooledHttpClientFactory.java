package org.kopi.ebics.http.h005.factory;

import org.apache.http.impl.client.CloseableHttpClient;
import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.http.h005.HttpClientRequest;
import org.kopi.ebics.http.h005.TraceableHttpClient;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.http.h005.HttpClientRequestConfiguration;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.interfaces.h005.TraceManager;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;

public class TraceablePooledHttpClientFactory extends AbstractPooledHttpClientFactory<TraceableHttpClient>
        implements ITraceableHttpClientFactory<TraceableHttpClient> {

    private final TraceManager traceManager;

    public TraceablePooledHttpClientFactory(IHttpClientGlobalConfiguration config, TraceManager traceManager) {
        super(config);
        this.traceManager = traceManager;
    }

    @Override
    public ByteArrayContentFactory sendAndTraceRequest(IHttpTransferSession httpTransferSession,
                                                       IBankConnectionTraceSession traceSession,
                                                       ContentFactory contentFactory) throws HttpClientException {
        return getHttpClient(httpTransferSession.getHttpConfigurationName()).sendAndTrace(
                new HttpClientRequest(httpTransferSession.getEbicsUrl(), contentFactory), traceSession);
    }

    @Override
    public TraceableHttpClient instantiateHttpClient(CloseableHttpClient httpClient,
                                                     HttpClientRequestConfiguration configuration,
                                                     String configurationName) {
        return new TraceableHttpClient(httpClient, configuration, configurationName, traceManager);
    }
}
