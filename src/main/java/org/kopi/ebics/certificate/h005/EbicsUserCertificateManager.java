package org.kopi.ebics.certificate.h005;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.util.*;
import java.util.stream.Collectors;

public class EbicsUserCertificateManager  implements Map<String, EbicsUserCertificates> {
    private final Map<String, EbicsUserCertificates> certs;

    public EbicsUserCertificateManager(Map<String, EbicsUserCertificates> certs) {
        this.certs = certs;
    }

    public static EbicsUserCertificateManager createEmpty() {
        return new EbicsUserCertificateManager(new HashMap<>());
    }

    public static EbicsUserCertificateManager load(InputStream ins, String password) throws GeneralSecurityException, IOException {
        KeyStoreManager manager = KeyStoreManager.load(ins, password);

        Set<String> aliasPrefixes = new HashSet<>();
        while(manager.aliases().hasMoreElements()){
            String alias = manager.aliases().nextElement();
            aliasPrefixes.add(alias.replaceAll("-((A005)|(E002)|(X002))$", ""));
        }

        Map<String, EbicsUserCertificates> singleCertEntries = aliasPrefixes.stream()
                .collect(Collectors.toMap(aliasPrefix -> aliasPrefix, aliasPrefix -> {
                    try {
                        return EbicsUserCertificates.load(manager, aliasPrefix);
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    }
                }));
        return new EbicsUserCertificateManager(new HashMap<>(singleCertEntries));
    }

    public void add(String userDn, String aliasPrefix) throws GeneralSecurityException, IOException {
        if (certs.containsKey(aliasPrefix)) {
            throw new IllegalArgumentException("The alias " + aliasPrefix + " exists already");
        }
        certs.put(aliasPrefix, EbicsUserCertificates.create(userDn));
    }

    public void remove(String aliasPrefix) {
        if (!certs.containsKey(aliasPrefix)) {
            throw new IllegalArgumentException("The alias " + aliasPrefix + " doesn't exist");
        }
        certs.remove(aliasPrefix);
    }

    public void save(OutputStream os, String password) throws GeneralSecurityException, IOException {
        KeyStoreManager manager = KeyStoreManager.create(password);
        certs.forEach((aliasPrefix, userCerts) -> {
            try {
                userCerts.save(manager, aliasPrefix);
            } catch (KeyStoreException e) {
                throw new RuntimeException(e);
            }
        });
        manager.save(os);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public EbicsUserCertificates get(Object key) {
        return null;
    }

    @Override
    public EbicsUserCertificates put(String key, EbicsUserCertificates value) {
        return null;
    }

    @Override
    public EbicsUserCertificates remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends EbicsUserCertificates> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<EbicsUserCertificates> values() {
        return null;
    }

    @Override
    public Set<Entry<String, EbicsUserCertificates>> entrySet() {
        return null;
    }
}
