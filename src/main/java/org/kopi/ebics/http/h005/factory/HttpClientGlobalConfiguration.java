package org.kopi.ebics.http.h005.factory;

import org.kopi.ebics.http.h005.HttpClientConfiguration;

import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientGlobalConfiguration implements IHttpClientGlobalConfiguration{

    private int connectionPoolMaxTotal = 25;
    private int connectionPoolDefaultMaxPerRoute = 5;
    private Map<String, HttpClientConfiguration> configurations;

    @Override
    public int getConnectionPoolMaxTotal() {
        return connectionPoolMaxTotal;
    }

    @Override
    public int getConnectionPoolDefaultMaxPerRoute() {
        return connectionPoolDefaultMaxPerRoute;
    }

    @Override
    public Map<String, IHttpClientConfiguration> getConfigurations() {
        // Assuming IHttpClientConfiguration is an interface, so we need to convert the values
        // to the interface type before returning the map.
        configurations.put("default", new HttpClientConfiguration());
        return configurations.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public void setConnectionPoolMaxTotal(int connectionPoolMaxTotal) {
        this.connectionPoolMaxTotal = connectionPoolMaxTotal;
    }

    public void setConnectionPoolDefaultMaxPerRoute(int connectionPoolDefaultMaxPerRoute) {
        this.connectionPoolDefaultMaxPerRoute = connectionPoolDefaultMaxPerRoute;
    }

    public void setConfigurations(Map<String, HttpClientConfiguration> configurations) {
        this.configurations = configurations;
    }
}
