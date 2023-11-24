package org.kopi.ebics.http.h005.factory;

import java.util.Map;

public interface IHttpClientGlobalConfiguration {
    int getConnectionPoolMaxTotal();
    int getConnectionPoolDefaultMaxPerRoute();
    Map<String, IHttpClientConfiguration> getConfigurations();
}
