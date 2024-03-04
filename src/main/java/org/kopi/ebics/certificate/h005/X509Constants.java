package org.kopi.ebics.certificate.h005;

public interface X509Constants {
    /**
     * Certificate signature algorithm
     */
    String SIGNATURE_ALGORITHM = "SHA256WithRSA";

    /**
     * Default days validity of a certificate
     */
    int DEFAULT_DURATION = 10 * 365;

    /**
     * EBICS key size
     */
    int EBICS_KEY_SIZE = 2048;
}
