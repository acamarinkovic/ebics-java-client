/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */

package org.kopi.ebics.xml.h005;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kopi.ebics.order.h005.EbicsOrder;
import org.kopi.ebics.session.EbicsSession;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.utils.h005.Utils;
import org.kopi.ebics.schema.h005.EbicsRequestDocument;

import javax.crypto.Cipher;


/**
 * The <code>InitializationRequestElement</code> is the root element for
 * ebics uploads and downloads requests. The response of this element is
 * then used either to upload or download files from the ebics server.
 *
 * @author Hachani
 */
public abstract class InitializationRequestElement extends DefaultEbicsRootElement {

    /**
     * Construct a new <code>InitializationRequestElement</code> root element.
     *
     * @param session    the current ebics session.
     * @param ebicsOrder the initialization order details.
     * @throws EbicsException
     */
    public InitializationRequestElement(EbicsSession session,
                                        EbicsOrder ebicsOrder)
            throws EbicsException {
        super(session);
        this.ebicsOrder = ebicsOrder;
        nonce = Utils.generateNonce();
    }

    @Override
    public void build() throws EbicsException {
        SignedInfo signedInfo;

        buildInitialization();
        signedInfo = new SignedInfo(session.getUser(), getDigest());
        signedInfo.build();
        ((EbicsRequestDocument) document).getEbicsRequest().setAuthSignature(signedInfo.getSignatureType());
        ((EbicsRequestDocument) document).getEbicsRequest().getAuthSignature().setSignatureValue(EbicsXmlFactory.createSignatureValueType(signedInfo.sign(toByteArray())));
    }

    @Override
    public byte[] toByteArray() {
        setSaveSuggestedPrefixes("http://www.ebics.org/h005", "");

        return super.toByteArray();
    }

    /**
     * Decodes an hexadecimal input.
     *
     * @param hex the hexadecimal input
     * @return the decoded hexadecimal value
     * @throws EbicsException
     */
    protected byte[] decodeHex(byte[] hex) throws EbicsException {
        if (hex == null) {
            throw new EbicsException("Bank digest is empty, HPB request must be performed before");
        }

        try {
            return Hex.decodeHex((new String(hex)).toCharArray());
        } catch (DecoderException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    /**
     * Generates the upload transaction key
     *
     * @return the transaction key
     */
    protected byte[] generateTransactionKey() throws EbicsException {
        try {
            Cipher cipher;

            cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, session.getBankE002Key());

            return cipher.doFinal(nonce);
        } catch (Exception e) {
            throw new EbicsException(e.getMessage());
        }
    }

    /**
     * Builds the initialization request according to the
     * element type.
     *
     * @throws EbicsException build fails
     */
    public abstract void buildInitialization() throws EbicsException;

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    protected EbicsOrder ebicsOrder;
    protected byte[] nonce;
    private static final long serialVersionUID = 8983807819242699280L;
}
