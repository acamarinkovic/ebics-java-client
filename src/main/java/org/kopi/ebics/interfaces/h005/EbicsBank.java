package org.kopi.ebics.interfaces.h005;

import java.net.URL;

public interface EbicsBank {

    /**
     * Returns the URL needed for communication to the bank.
     * @return the URL needed for communication to the bank.
     */
    URL getURL();

    /**
     * Returns the bank's name.
     * @return the bank's name.
     */
    String getName();

    /**
     * Returns the bank's id.
     * @return the bank's id.
     */
    String getHostId();

    /**
     * The name of the HTTP configuration which is used for this bank
     */
    String getHttpClientConfigurationName();
}
