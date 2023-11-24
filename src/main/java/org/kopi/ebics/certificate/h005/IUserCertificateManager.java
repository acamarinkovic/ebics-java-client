package org.kopi.ebics.certificate.h005;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.utils.h005.CryptoUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

public interface IUserCertificateManager {

    X509Certificate getA005Certificate();
    X509Certificate getX002Certificate();
    X509Certificate getE002Certificate();
    PrivateKey getA005PrivateKey();
    PrivateKey getX002PrivateKey();
    PrivateKey getE002PrivateKey();

    default byte[] getA005CertificateBytes() throws EbicsException {
        try {
            return getA005Certificate().getEncoded();
        } catch (CertificateEncodingException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    default byte[] getE002CertificateBytes() throws EbicsException {
        try {
            return getE002Certificate().getEncoded();
        } catch (CertificateEncodingException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    default byte[] getX002CertificateBytes() throws EbicsException {
        try {
            return getX002Certificate().getEncoded();
        } catch (CertificateEncodingException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    default RSAPublicKey getA005PublicKey() {
        return (RSAPublicKey) getA005Certificate().getPublicKey();
    }

    default RSAPublicKey getE002PublicKey() {
        return (RSAPublicKey) getE002Certificate().getPublicKey();
    }

    default RSAPublicKey getX002PublicKey() {
        return (RSAPublicKey) getX002Certificate().getPublicKey();
    }

    default byte[] authenticate(byte[] digest) throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        Signature signature = Signature.getInstance("SHA256WithRSA", "BC");
        signature.initSign(getX002PrivateKey());
        signature.update(digest);
        return signature.sign();
    }

    default byte[] sign(byte[] digest) throws IOException, GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        Signature signature = Signature.getInstance("SHA256WithRSA", "BC");
        signature.initSign(getA005PrivateKey());
        signature.update(removeOSSpecificChars(digest));
        return signature.sign();
    }

    default byte[] decrypt(byte[] encryptedData, byte[] transactionKey)
            throws EbicsException, GeneralSecurityException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, getE002PrivateKey());
        int blockSize = cipher.getBlockSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int j = 0; j * blockSize < transactionKey.length; j++) {
            outputStream.write(cipher.doFinal(transactionKey, j * blockSize, blockSize));
        }
        return decryptData(encryptedData, outputStream.toByteArray());
    }

    default byte[] decryptData(byte[] input, byte[] key) throws EbicsException {
        return CryptoUtils.decrypt(input, new SecretKeySpec(key, "EAS"));
    }

    default byte[] removeOSSpecificChars(byte[] buf) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (byte b : buf) {
            if (b != '\r' && b != '\n' && b != 0x1A) {
                output.write(b);
            }
        }
        return output.toByteArray();
    }
}
