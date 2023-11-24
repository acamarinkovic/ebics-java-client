package org.kopi.ebics.http.h005;

import org.apache.http.impl.client.CloseableHttpClient;
import org.kopi.ebics.enumeration.h005.TraceCategory;
import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.interfaces.h005.*;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class TraceableHttpClient implements HttpClient, ITraceableHttpClient {

    private final SimpleHttpClient simpleHttpClient;
    private final TraceManager traceManager;

    public TraceableHttpClient(SimpleHttpClient simpleHttpClient, TraceManager traceManager) {
        this.simpleHttpClient = simpleHttpClient;
        this.traceManager = traceManager;
    }

    public TraceableHttpClient(CloseableHttpClient httpClient,
                               HttpClientRequestConfiguration configuration,
                               String configurationName,
                               TraceManager traceManager) {
        this(new SimpleHttpClient(httpClient, configuration, configurationName), traceManager);
    }

    @Override
    public HttpClientRequestConfiguration getConfiguration() {
        return null;
    }

    @Override
    public ByteArrayContentFactory send(HttpClientRequest request) throws HttpClientException, IOException, URISyntaxException {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public ByteArrayContentFactory sendAndTrace(HttpClientRequest request, IBaseTraceSession traceSession) throws HttpClientException {
        try {
            //traceManager.trace(request.getContent(), traceSession, true);
            ByteArrayContentFactory response = simpleHttpClient.send(request);
            //traceManager.trace(response, traceSession, false);
            //traceManager.updateLastTrace(traceSession, TraceCategory.RequestOk);
            return response;
        } catch (HttpClientException e) {
            //traceManager.updateLastTrace(traceSession, TraceCategory.RequestError, e);
            throw e;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
