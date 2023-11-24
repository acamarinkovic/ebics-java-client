package org.kopi.ebics.client.h005;

import org.kopi.ebics.interfaces.h005.EbicsBank;

import java.net.URL;

public class Bank implements EbicsBank {

    private URL url;

    private String hostId;

    private String name;

    private String httpClientConfigurationName;


    public Bank(URL url, String name, String hostId, String httpClientConfigurationName) {
        this.url = url;
        this.name = name;
        this.hostId = hostId;
        this.httpClientConfigurationName = httpClientConfigurationName;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHostId() {
        return hostId;
    }

    @Override
    public String getHttpClientConfigurationName() {
        return httpClientConfigurationName;
    }
}
