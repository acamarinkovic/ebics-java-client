package org.kopi.ebics.interfaces.h005;

import org.kopi.ebics.certificate.h005.UserCertificateManager;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.interfaces.EbicsUser;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface LetterManager {
    /**
     * Creates the initialization letter for the INI request.
     * This letter contains information about the signature certificate
     * of the given user.
     *
     * @param user     the ebics user.
     * @param userCert the user certificate manager.
     * @return the INI letter.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws EbicsException
     */
    InitLetter createA005Letter(EbicsUser user, UserCertificateManager userCert) throws GeneralSecurityException, IOException, EbicsException;

    /**
     * Creates the initialization letter for the HIA request.
     * This letter contains information about the encryption
     * certificates of the given user.
     *
     * @param user     the ebics user.
     * @param userCert the user certificate manager.
     * @return the HIA letter.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws EbicsException
     */
    InitLetter createE002Letter(EbicsUser user, UserCertificateManager userCert) throws GeneralSecurityException, IOException, EbicsException;

    /**
     * Creates the initialization letter for the HIA request.
     * This letter contains information about the authentication
     * certificates of the given user.
     *
     * @param user     the ebics user.
     * @param userCert the user certificate manager.
     * @return the HIA letter.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws EbicsException
     */
    InitLetter createX002Letter(EbicsUser user, UserCertificateManager userCert) throws GeneralSecurityException, IOException, EbicsException;
}
