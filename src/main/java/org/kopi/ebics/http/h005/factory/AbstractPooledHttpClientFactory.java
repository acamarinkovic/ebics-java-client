package org.kopi.ebics.http.h005.factory;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.kopi.ebics.exception.h005.HttpClientException;
import org.kopi.ebics.http.h005.HttpClientConfiguration;
import org.kopi.ebics.http.h005.HttpClientRequest;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.http.h005.HttpClient;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public abstract class AbstractPooledHttpClientFactory<T extends HttpClient> implements IHttpClientFactory<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPooledHttpClientFactory.class);

    private final IHttpClientGlobalConfiguration config;
    private final PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();

    private Map<String, T> httpClients;

    public AbstractPooledHttpClientFactory(IHttpClientGlobalConfiguration config) {
        this.config = config;

        poolingConnManager.setMaxTotal(config.getConnectionPoolMaxTotal());
        poolingConnManager.setDefaultMaxPerRoute(config.getConnectionPoolDefaultMaxPerRoute());
    }

    @PostConstruct
    public void initConfigurations() {
        Map<String, IHttpClientConfiguration> configurations = config.getConfigurations();
        if (configurations.isEmpty()) {
            HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();
            httpClientConfiguration.setDisplayName("default");
            httpClientConfiguration.setHttpContentType("text/xml; charset=ISO-8859-1");
            httpClientConfiguration.setSocketTimeoutMilliseconds(300 * 1000);
            httpClientConfiguration.setConnectionTimeoutMilliseconds(300 * 1000);

            configurations.put("default", httpClientConfiguration);
        }
        httpClients = createHttpClients(configurations);
    }

    @PreDestroy
    public void preDestroy() {
        logger.info("Closing gracefully the HTTP clients and connection manager");
        httpClients.forEach((key, client) -> HttpClientUtils.closeQuietly((HttpResponse) client));
        poolingConnManager.close();
    }

    @Override
    public T getHttpClient(String configurationName) {
        return httpClients.get(configurationName);
    }

    @Override
    public ByteArrayContentFactory sendRequest(IHttpTransferSession httpTransferSession, ContentFactory contentFactory) throws HttpClientException, IOException, URISyntaxException {
        return getHttpClient(httpTransferSession.getHttpConfigurationName()).send(
                new HttpClientRequest(httpTransferSession.getEbicsUrl(), contentFactory)
        );
    }

    private Map<String, T> createHttpClients(Map<String, IHttpClientConfiguration> namedClientConfigurations) {
        return namedClientConfigurations.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> instantiateHttpClient(
                                createHttpClient(entry.getKey(), entry.getValue()),
                                entry.getValue(),
                                entry.getKey())
                ));
    }

    private CloseableHttpClient createHttpClient(String configurationName, IHttpClientConfiguration configuration) {
        try {
            final String logPrefix = "HttpClient '" + configurationName + ":" + configuration.getDisplayName() + "'";
            logger.info("Creating " + logPrefix);

            HttpClientBuilder builder = HttpClientBuilder.create();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(configuration.getSocketTimeoutMilliseconds())
                    .setConnectTimeout(configuration.getConnectionTimeoutMilliseconds())
                    .build();
            builder.setDefaultRequestConfig(requestConfig);

            if (configuration.getHttpProxyHost() != null && configuration.getHttpProxyPort() != null) {
                logger.debug(logPrefix + " : setting proxy host:port=" + configuration.getHttpProxyHost() + ":" + configuration.getHttpProxyPort());
                builder.setProxy(new HttpHost(configuration.getHttpProxyHost(), configuration.getHttpProxyPort()));
            } else {
                logger.debug(logPrefix + " : no proxy used");
            }

            if (configuration.getHttpProxyUser() != null && configuration.getHttpProxyPassword() != null) {
                logger.debug(logPrefix + " : setting proxy credentials: " + configuration.getHttpProxyUser() + ":" + configuration.getHttpProxyPassword());
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(
                        new AuthScope(configuration.getHttpProxyHost(), configuration.getHttpProxyPort()),
                        new UsernamePasswordCredentials(configuration.getHttpProxyUser(), configuration.getHttpProxyPassword()));
                builder.setDefaultCredentialsProvider(credentialsProvider);
                builder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
            } else {
                logger.debug(logPrefix + " : no proxy credentials used");
            }

            if (configuration.getSslTrustedStoreFile() != null) {
                String sslTrustStoreFile = configuration.getSslTrustedStoreFile();
                logger.debug(logPrefix + " : setting SSL trust store: " + sslTrustStoreFile + ", pass: " + configuration.getSslTrustedStoreFilePassword());
                File trustStoreFile = new File(sslTrustStoreFile);
                if (trustStoreFile.exists()) {
                    SSLContextBuilder sslContextBuilder = SSLContexts.custom();
                    SSLContext sslContext = null;
                    if (configuration.getSslTrustedStoreFilePassword() != null) {
                        sslContextBuilder = sslContextBuilder.loadTrustMaterial(trustStoreFile, configuration.getSslTrustedStoreFilePassword().toCharArray());
                    } else {
                        sslContextBuilder = sslContextBuilder.loadTrustMaterial(trustStoreFile);
                    }
                    builder.setSSLContext(sslContextBuilder.build());
                } else {
                    logger.error("Provided truststore file name doesn't exist: '" + sslTrustStoreFile + "'");
                }
            } else {
                logger.debug(logPrefix + " : no truststore file used");
            }

            builder.setConnectionManager(poolingConnManager);
            builder.setConnectionManagerShared(true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating HttpClient", e);
        }
    }
}