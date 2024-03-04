package org.kopi.ebics.certificate.h005;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class KeyStoreManager {
    private final KeyStore keyStore;
    private final String password;

    private KeyStoreManager(KeyStore keyStore, String password) {
        this.keyStore = keyStore;
        this.password = password;
    }

    public X509Certificate getCertificate(String alias) throws KeyStoreException {
        Certificate cert = keyStore.getCertificate(alias);
        if (cert == null) {
            throw new KeyStoreException("Alias " + alias + " not found in the KeyStore");
        }
        return (X509Certificate) cert;
    }

    public PrivateKey getPrivateKey(String alias) throws GeneralSecurityException {
        Key key = keyStore.getKey(alias, password.toCharArray());
        if (key == null) {
            throw new GeneralSecurityException("Private key not found for alias " + alias);
        }
        return (PrivateKey) key;
    }

    public static KeyStoreManager load(String path, String password) throws GeneralSecurityException, IOException {
        return load(Files.newInputStream(Paths.get(path)), password);
    }

    public static KeyStoreManager load(InputStream ins, String password) throws GeneralSecurityException, IOException {
        KeyStoreManager keyStoreManager = createKeyStoreManager(password);
        keyStoreManager.load(ins);
        return keyStoreManager;
    }

    public static KeyStoreManager create(String password) throws KeyStoreException, NoSuchProviderException {
        KeyStoreManager keyStoreManager = createKeyStoreManager(password);
        keyStoreManager.create();
        return keyStoreManager;
    }

    private static KeyStoreManager createKeyStoreManager(String password) throws KeyStoreException, NoSuchProviderException {
        return new KeyStoreManager(KeyStore.getInstance("PKCS12", BouncyCastleProvider.PROVIDER_NAME), password);
    }

    private void load(InputStream ins) throws GeneralSecurityException, IOException {
        keyStore.load(ins, password.toCharArray());
    }

    private void create() {
        try {
            keyStore.load(null, null);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to create KeyStore", e);
        }
    }

    public void setKeyEntry(String alias, Key key, X509Certificate certificate) throws KeyStoreException {
        keyStore.setKeyEntry(alias, key, password.toCharArray(), new Certificate[]{certificate});
    }

    public void save(OutputStream output) throws GeneralSecurityException, IOException {
        keyStore.store(output, password.toCharArray());
    }

    public Enumeration<String> aliases() throws KeyStoreException {
        return keyStore.aliases();
    }
}
