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

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;
import org.kopi.ebics.order.h005.EbicsOrder;
import org.kopi.ebics.session.EbicsSession;
import org.kopi.ebics.utils.h005.CryptoUtils;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.utils.h005.Utils;
import org.kopi.ebics.schema.h005.*;

import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Collections;


/**
 * The <code>SPRRequestElement</code> is the request element
 * for revoking a subscriber
 *
 * @author Hachani
 *
 */
public class SPRRequestElement extends InitializationRequestElement {

    /**
     * Constructs a new SPR request element.
     * @param session the current ebics session.
     */
    public SPRRequestElement(EbicsSession session) throws EbicsException {
        super(session, new EbicsOrder(EbicsAdminOrderType.SPR, Collections.emptyMap()));
        keySpec = new SecretKeySpec(nonce, "EAS");
    }

    @Override
    public void buildInitialization() throws EbicsException {
        EbicsRequestDocument.EbicsRequest request;
        EbicsRequestDocument.EbicsRequest.Header header;
        EbicsRequestDocument.EbicsRequest.Body				body;
        MutableHeaderType 			mutable;
        StaticHeaderType 			xstatic;
        StaticHeaderType.Product product;
        StaticHeaderType.BankPubKeyDigests bankPubKeyDigests;
        StaticHeaderType.BankPubKeyDigests.Authentication authentication;
        StaticHeaderType.BankPubKeyDigests.Encryption encryption;
        DataTransferRequestType 		dataTransfer;
        DataTransferRequestType.DataEncryptionInfo dataEncryptionInfo;
        DataTransferRequestType.SignatureData signatureData;
        DataEncryptionInfoType.EncryptionPubKeyDigest encryptionPubKeyDigest;
        StaticHeaderOrderDetailsType 	orderDetails;
        StaticHeaderOrderDetailsType.AdminOrderType orderType;
        StandardOrderParamsType		standardOrderParamsType;
        UserSignature userSignature;
        DataDigestType dataDigest;

        userSignature = new UserSignature(session.getUser(), generateName("SIG"),
                session.getConfiguration().getSignatureVersion(),
                " ".getBytes());
        userSignature.build();
        userSignature.validate();

        mutable = EbicsXmlFactory.createMutableHeaderType("Initialisation", null);
        product = EbicsXmlFactory.createProduct(session.getProduct());
        authentication = EbicsXmlFactory.createAuthentication(session.getConfiguration().getAuthenticationVersion(),
                "http://www.w3.org/2001/04/xmlenc#sha256",
                decodeHex(session.getUser().getPartner().getBank().getX002Digest()));
        encryption = EbicsXmlFactory.createEncryption(session.getConfiguration().getEncryptionVersion(),
                "http://www.w3.org/2001/04/xmlenc#sha256",
                decodeHex(session.getUser().getPartner().getBank().getE002Digest()));
        bankPubKeyDigests = EbicsXmlFactory.createBankPubKeyDigests(authentication, encryption);
        orderType = EbicsXmlFactory.createAdminOrderType(ebicsOrder.getAdminOrderType().toString());
        standardOrderParamsType = EbicsXmlFactory.createStandardOrderParamsType();
        orderDetails = EbicsXmlFactory.createStaticHeaderOrderDetailsType(null,
                orderType,
                standardOrderParamsType);
        xstatic = EbicsXmlFactory.createStaticHeaderType(session.getBankID(),
                nonce,
                0,
                session.getUser().getPartner().getPartnerId(),
                product,
                session.getUser().getSecurityMedium(),
                session.getUser().getUserId(),
                Calendar.getInstance(),
                orderDetails,
                bankPubKeyDigests);
        header = EbicsXmlFactory.createEbicsRequestHeader(true, mutable, xstatic);
        encryptionPubKeyDigest = EbicsXmlFactory.createEncryptionPubKeyDigest(session.getConfiguration().getEncryptionVersion(),
                "http://www.w3.org/2001/04/xmlenc#sha256",
                decodeHex(session.getUser().getPartner().getBank().getE002Digest()));
        signatureData = EbicsXmlFactory.createSignatureData(true, CryptoUtils.encrypt(Utils.zip(userSignature.prettyPrint()), keySpec));
        dataEncryptionInfo = EbicsXmlFactory.createDataEncryptionInfo(true,
                encryptionPubKeyDigest,
                generateTransactionKey());
        dataDigest = EbicsXmlFactory.createDataDigestType(session.getConfiguration().getSignatureVersion(), null);
        dataTransfer = EbicsXmlFactory.createDataTransferRequestType(dataEncryptionInfo, signatureData, dataDigest);
        body = EbicsXmlFactory.createEbicsRequestBody(dataTransfer);
        request = EbicsXmlFactory.createEbicsRequest(header, body);
        document = EbicsXmlFactory.createEbicsRequestDocument(request);
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private SecretKeySpec			keySpec;
    private static final long 		serialVersionUID = -6742241777786111337L;
}
