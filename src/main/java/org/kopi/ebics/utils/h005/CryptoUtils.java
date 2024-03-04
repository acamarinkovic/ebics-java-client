package org.kopi.ebics.utils.h005;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kopi.ebics.exception.EbicsException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Security;

public class CryptoUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Encrypts an input with a given key spec.
     *
     * @param input   the input to encrypt
     * @param keySpec the key spec
     * @return the encrypted input
     * @throws EbicsException
     */
    public static byte[] encrypt(byte[] input, SecretKeySpec keySpec) throws EbicsException {
        return encryptOrDecrypt(Cipher.ENCRYPT_MODE, input, keySpec);
    }

    /**
     * Decrypts the given input according to key spec.
     *
     * @param input   the input to decrypt
     * @param keySpec the key spec
     * @return the decrypted input
     * @throws EbicsException
     */
    public static byte[] decrypt(byte[] input, SecretKeySpec keySpec) throws EbicsException {
        return encryptOrDecrypt(Cipher.DECRYPT_MODE, input, keySpec);
    }

    /**
     * Encrypts or decrypts the given input according to key spec.
     *
     * @param mode    the encryption-decryption mode.
     * @param input   the input to encrypt or decrypt.
     * @param keySpec the key spec.
     * @return the encrypted or decrypted data.
     */
    private static byte[] encryptOrDecrypt(int mode, byte[] input, SecretKeySpec keySpec) throws EbicsException {
        Cipher cipher;
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        try {
            cipher = Cipher.getInstance("AES/CBC/ISO10126Padding", BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(mode, keySpec, iv);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new EbicsException(e.getMessage());
        }
    }
}
