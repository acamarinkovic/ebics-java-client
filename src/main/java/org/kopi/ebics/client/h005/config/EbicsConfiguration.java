package org.kopi.ebics.client.h005.config;

import java.util.Locale;

public class EbicsConfiguration {

    private Locale locale;
    private String signatureVersion;
    private String authenticationVersion;
    private String encryptionVersion;
    private boolean isTraceEnabled;
    private boolean isCompressionEnabled;

    public EbicsConfiguration(
            Locale locale,
            String signatureVersion,
            String authenticationVersion,
            String encryptionVersion,
            boolean isTraceEnabled,
            boolean isCompressionEnabled
    ) {
        this.locale = locale;
        this.signatureVersion = signatureVersion;
        this.authenticationVersion = authenticationVersion;
        this.encryptionVersion = encryptionVersion;
        this.isTraceEnabled = isTraceEnabled;
        this.isCompressionEnabled = isCompressionEnabled;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getSignatureVersion() {
        return signatureVersion;
    }

    public String getAuthenticationVersion() {
        return authenticationVersion;
    }

    public String getEncryptionVersion() {
        return encryptionVersion;
    }

    public boolean isTraceEnabled() {
        return isTraceEnabled;
    }

    public boolean isCompressionEnabled() {
        return isCompressionEnabled;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    public void setAuthenticationVersion(String authenticationVersion) {
        this.authenticationVersion = authenticationVersion;
    }

    public void setEncryptionVersion(String encryptionVersion) {
        this.encryptionVersion = encryptionVersion;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        isTraceEnabled = traceEnabled;
    }

    public void setCompressionEnabled(boolean compressionEnabled) {
        isCompressionEnabled = compressionEnabled;
    }
}
