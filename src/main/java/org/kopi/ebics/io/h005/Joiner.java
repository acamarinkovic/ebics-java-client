package org.kopi.ebics.io.h005;

import org.kopi.ebics.certificate.h005.UserCertificateManager;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.utils.h005.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

public class Joiner {
    private final UserCertificateManager userCert;
    private final ByteArrayOutputStream buffer;

    public Joiner(UserCertificateManager userCert) {
        this.userCert = userCert;
        this.buffer = new ByteArrayOutputStream();
    }

    /**
     * Appends data to the internal buffer.
     *
     * @param data the data to append.
     * @throws EbicsException if an EbicsException occurs.
     */
    public void append(byte[] data) throws EbicsException {
        try {
            buffer.write(data);
            buffer.flush();
        } catch (IOException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    /**
     * Writes the joined part to an output stream.
     *
     * @param output          the output stream.
     * @param transactionKey the transaction key.
     * @throws EbicsException if an EbicsException occurs.
     */
    public void writeTo(OutputStream output, byte[] transactionKey) throws EbicsException {
        try {
            buffer.close();
            byte[] decrypted = userCert.decrypt(buffer.toByteArray(), transactionKey);
            output.write(Utils.unzip(decrypted));
        } catch (GeneralSecurityException | IOException e) {
            throw new EbicsException(e.getMessage());
        }
    }
}
