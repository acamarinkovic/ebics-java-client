package org.kopi.ebics.session.h005;

import org.kopi.ebics.interfaces.*;
import org.kopi.ebics.interfaces.h005.IEbicsConfiguration;
import org.kopi.ebics.io.IOUtils;
import org.kopi.ebics.letter.DefaultLetterManager;
import org.kopi.ebics.session.DefaultEbicsLogger;
import org.kopi.ebics.session.DefaultSerializationManager;
import org.kopi.ebics.session.DefaultTraceManager;

import java.io.File;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class DefaultEbicsConfiguration implements IEbicsConfiguration {

    /**
     * Creates a new application configuration.
     * @param rootDir the root directory
     */
    public DefaultEbicsConfiguration(String rootDir, Properties properties) {
        this.rootDir = rootDir;
        bundle = ResourceBundle.getBundle(RESOURCE_DIR);
        this.properties = properties;
        logger = new DefaultEbicsLogger();
        serializationManager = new DefaultSerializationManager();
        traceManager = new DefaultTraceManager();
    }

    /**
     * Returns the corresponding property of the given key
     * @param key the property key
     * @return the property value.
     */
    private String getString(String key) {
        try {
            return bundle.getString(key);
        } catch(MissingResourceException e) {
            return "!!" + key + "!!";
        }
    }

    @Override
    public String getRootDirectory() {
        return rootDir;
    }

    @Override
    public void init() {
        //Create the root directory
        IOUtils.createDirectories(getRootDirectory());
        //Create the logs directory
        IOUtils.createDirectories(getLogDirectory());
        //Create the serialization directory
        IOUtils.createDirectories(getSerializationDirectory());
        //create the SSL trusted stores directories
        IOUtils.createDirectories(getSSLTrustedStoreDirectory());
        //create the SSL key stores directories
        IOUtils.createDirectories(getSSLKeyStoreDirectory());
        //Create the SSL bank certificates directories
        IOUtils.createDirectories(getSSLBankCertificates());
        //Create users directory
        IOUtils.createDirectories(getUsersDirectory());

        logger.setLogFile(getLogDirectory() + File.separator + getLogFileName());
        ((DefaultEbicsLogger)logger).setFileLoggingEnabled(true);
        ((DefaultEbicsLogger)logger).setLevel(DefaultEbicsLogger.ALL_LEVEL);
        serializationManager.setSerializationDirectory(getSerializationDirectory());
        traceManager.setTraceEnabled(isTraceEnabled());
        letterManager = new DefaultLetterManager(getLocale());
    }

    @Override
    public Locale getLocale() {
        return Locale.FRANCE;
    }

    @Override
    public String getLogDirectory() {
        return rootDir + File.separator + getString("log.dir.name");
    }

    @Override
    public String getLogFileName() {
        return getString("log.file.name");
    }

    @Override
    public String getConfigurationFile() {
        return rootDir + File.separator + getString("conf.file.name");
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String getKeystoreDirectory(EbicsUser user) {
        return getUserDirectory(user) + File.separator + getString("keystore.dir.name");
    }

    @Override
    public String getTransferTraceDirectory(EbicsUser user) {
        return getUserDirectory(user) + File.separator + getString("traces.dir.name");
    }

    @Override
    public String getSerializationDirectory() {
        return rootDir + File.separator + getString("serialization.dir.name");
    }

    @Override
    public String getSSLTrustedStoreDirectory() {
        return rootDir + File.separator + getString("ssltruststore.dir.name");
    }

    @Override
    public String getSSLKeyStoreDirectory() {
        return rootDir + File.separator + getString("sslkeystore.dir.name");
    }

    @Override
    public String getSSLBankCertificates() {
        return rootDir + File.separator + getString("sslbankcert.dir.name");
    }

    @Override
    public String getUsersDirectory() {
        return rootDir + File.separator + getString("users.dir.name");
    }

    @Override
    public SerializationManager getSerializationManager() {
        return serializationManager;
    }

    @Override
    public TraceManager getTraceManager() {
        return traceManager;
    }

    @Override
    public LetterManager getLetterManager() {
        return letterManager;
    }

    @Override
    public String getLettersDirectory(EbicsUser user) {
        return getUserDirectory(user) + File.separator + getString("letters.dir.name");
    }

    @Override
    public String getUserDirectory(EbicsUser user) {
        return getUsersDirectory() + File.separator + user.getUserId();
    }

    @Override
    public EbicsLogger getLogger() {
        return logger;
    }

    @Override
    public String getSignatureVersion() {
        return getString("signature.version");
    }

    @Override
    public String getAuthenticationVersion() {
        return getString("authentication.version");
    }

    @Override
    public String getEncryptionVersion() {
        return getString("encryption.version");
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isCompressionEnabled() {
        return true;
    }

    @Override
    public int getRevision() {
        return 1;
    }

    @Override
    public String getVersion() {
        return getString("ebics.version");
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private final String rootDir;
    private final ResourceBundle bundle;
    private final Properties properties;
    private final EbicsLogger logger;
    private final SerializationManager serializationManager;
    private final TraceManager traceManager;
    private LetterManager letterManager;

    private static final String RESOURCE_DIR = "org.kopi.ebics.client.config";
}
