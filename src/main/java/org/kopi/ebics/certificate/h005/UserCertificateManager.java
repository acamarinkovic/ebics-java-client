package org.kopi.ebics.certificate.h005;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kopi.ebics.utils.h005.CryptoUtils;
import org.kopi.ebics.exception.h005.EbicsException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

public class UserCertificateManager implements IUserCertificateManager{

    private X509Certificate a005Certificate;
    private X509Certificate x002Certificate;
    private X509Certificate e002Certificate;
    private PrivateKey a005PrivateKey;
    private PrivateKey x002PrivateKey;
    private PrivateKey e002PrivateKey;

    public UserCertificateManager(
            X509Certificate a005Certificate,
            X509Certificate x002Certificate,
            X509Certificate e002Certificate,
            PrivateKey a005PrivateKey,
            PrivateKey x002PrivateKey,
            PrivateKey e002PrivateKey
    ) {
        this.a005Certificate = a005Certificate;
        this.x002Certificate = x002Certificate;
        this.e002Certificate = e002Certificate;
        this.a005PrivateKey = a005PrivateKey;
        this.x002PrivateKey = x002PrivateKey;
        this.e002PrivateKey = e002PrivateKey;
    }

    
    public byte[] getA005CertificateBytes() throws EbicsException {
        return getCertificateBytes(a005Certificate);
    }

    
    public byte[] getE002CertificateBytes() throws EbicsException {
        return getCertificateBytes(e002Certificate);
    }

    
    public byte[] getX002CertificateBytes() throws EbicsException {
        return getCertificateBytes(x002Certificate);
    }

    
    public RSAPublicKey getA005PublicKey() {
        return (RSAPublicKey) a005Certificate.getPublicKey();
    }

    
    public RSAPublicKey getE002PublicKey() {
        return (RSAPublicKey) e002Certificate.getPublicKey();
    }

    
    public RSAPublicKey getX002PublicKey() {
        return (RSAPublicKey) x002Certificate.getPublicKey();
    }

    
    public byte[] authenticate(byte[] digest) throws GeneralSecurityException {
        return sign(x002PrivateKey, digest);
    }

    
    public byte[] sign(byte[] digest) throws GeneralSecurityException, IOException {
        return sign(a005PrivateKey, removeOSSpecificChars(digest));
    }

    
    public byte[] decrypt(byte[] encryptedData, byte[] transactionKey)
            throws EbicsException, GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, e002PrivateKey);
        int blockSize = cipher.getBlockSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int j = 0; j * blockSize < transactionKey.length; j++) {
            outputStream.write(cipher.doFinal(transactionKey, j * blockSize, blockSize));
        }
        return decryptData(encryptedData, outputStream.toByteArray());
    }

    public byte[] decryptData(byte[] input, byte[] key) throws EbicsException {
        return CryptoUtils.decrypt(input, new SecretKeySpec(key, "EAS"));
    }

    private byte[] getCertificateBytes(X509Certificate certificate) throws EbicsException {
        try {
            return certificate.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    private byte[] sign(PrivateKey privateKey, byte[] data) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SHA256WithRSA", BouncyCastleProvider.PROVIDER_NAME);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public byte[] removeOSSpecificChars(byte[] buf) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (byte b : buf) {
            if (b != '\r' && b != '\n' && b != 0x1A) {
                output.write(b);
            }
        }
        return output.toByteArray();
    }

    public X509Certificate getA005Certificate() {
        return a005Certificate;
    }

    public void setA005Certificate(X509Certificate a005Certificate) {
        this.a005Certificate = a005Certificate;
    }

    public X509Certificate getX002Certificate() {
        return x002Certificate;
    }

    public void setX002Certificate(X509Certificate x002Certificate) {
        this.x002Certificate = x002Certificate;
    }

    public X509Certificate getE002Certificate() {
        return e002Certificate;
    }

    public void setE002Certificate(X509Certificate e002Certificate) {
        this.e002Certificate = e002Certificate;
    }

    public PrivateKey getA005PrivateKey() {
        return a005PrivateKey;
    }

    public void setA005PrivateKey(PrivateKey a005PrivateKey) {
        this.a005PrivateKey = a005PrivateKey;
    }

    public PrivateKey getX002PrivateKey() {
        return x002PrivateKey;
    }

    public void setX002PrivateKey(PrivateKey x002PrivateKey) {
        this.x002PrivateKey = x002PrivateKey;
    }

    public PrivateKey getE002PrivateKey() {
        return e002PrivateKey;
    }

    public void setE002PrivateKey(PrivateKey e002PrivateKey) {
        this.e002PrivateKey = e002PrivateKey;
    }
}
