package org.kopi.ebics.http.h005;

import org.apache.http.HttpHeaders;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.exception.h005.HttpServerException;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class SimpleHttpClient implements HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

    private final CloseableHttpClient httpClient;
    private final HttpClientRequestConfiguration configuration;
    private final String configurationName;

    public SimpleHttpClient(CloseableHttpClient httpClient, HttpClientRequestConfiguration configuration, String configurationName) {
        this.httpClient = httpClient;
        this.configuration = configuration;
        this.configurationName = configurationName;
    }

    @Override
    public HttpClientRequestConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public ByteArrayContentFactory send(HttpClientRequest request) throws HttpClientException, IOException, URISyntaxException {
        logger.trace("Sending HTTP POST request to URL: {} using config: {}", request.getRequestURL(), configurationName);
        HttpPost method = new HttpPost(request.getRequestURL().toURI());
        method.setEntity(EntityBuilder.create().setStream(request.getContent().getContent()).build());
        if (configuration.getHttpContentType() != null && !configuration.getHttpContentType().trim().equals("")) {
            logger.trace("Setting HTTP POST content header: {}", configuration.getHttpContentType());
            method.setHeader(HttpHeaders.CONTENT_TYPE, configuration.getHttpContentType());
        }
        try {
            try (CloseableHttpResponse response = httpClient.execute(method)) {
                logger.trace("Received HTTP POST response code {} from URL: {} using config: {}", response.getStatusLine().getStatusCode(), request.getRequestURL(), configurationName);
                // Check the HTTP return code (must be 200)
                checkHttpCode(response);
                // If ok returning content
                return new ByteArrayContentFactory(EntityUtils.toByteArray(response.getEntity()));
            }
        } catch (IOException exception) {
            throw new HttpClientException("Exception while sending HTTP request to " + request.getRequestURL(), exception);
        } catch (EbicsException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Checks for the returned http code
     *
     * @param response the http response
     * @throws EbicsException
     */
    private void checkHttpCode(CloseableHttpResponse response) throws EbicsException {
        int httpCode = response.getStatusLine().getStatusCode();
        String reasonPhrase = response.getStatusLine().getReasonPhrase();
        if (httpCode != 200) {
            // Log detail response in server log & throw new exception

            // Try to get response content if there is something
            String responseContent;
            try {
                responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (Exception ex) {
                responseContent = null;
            }

            // Get response headers
            String responseHeaders = response.getAllHeaders() != null ? String.join(";", (CharSequence) Arrays.asList(response.getAllHeaders())) : "";

            if (responseContent != null && !responseContent.trim().equals("")) {
                logger.warn("Unexpected HTTP Code: {} returned as EBICS response, reason: {}, response headers '{}', response content: '{}'",
                        httpCode, reasonPhrase, responseHeaders, responseContent);
            } else {
                logger.warn("Unexpected HTTP Code: {} returned as EBICS response, reason: {}, response headers '{}' (no response content available)",
                        httpCode, reasonPhrase, responseHeaders);
            }
            throw new HttpServerException(
                    Integer.toString(httpCode), reasonPhrase,
                    "Wrong returned HTTP code: " + httpCode + " " + reasonPhrase
            );
        }
    }


    @PreDestroy
    @Override
    public void close() {
        logger.debug("Closing gracefully the HTTP client");
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.error("Error while closing the HTTP client", e);
        }
    }
}
