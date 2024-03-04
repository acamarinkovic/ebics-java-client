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
import org.kopi.ebics.order.h005.EbicsDownloadOrder;
import org.kopi.ebics.session.EbicsSession;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.schema.h005.*;
import org.kopi.ebics.schema.h005.StaticHeaderOrderDetailsType;

import java.util.Calendar;
import java.util.Date;


/**
 * The <code>DInitializationRequestElement</code> is the common initialization
 * for all ebics downloads.
 *
 * @author Hachani
 *
 */
public class DownloadInitializationRequestElement extends InitializationRequestElement {

  /**
   * Constructs a new <code>DInitializationRequestElement</code> for downloads initializations.
   * @param session the current ebics session
   * @throws EbicsException
   */
  public DownloadInitializationRequestElement(EbicsSession session,
                                              EbicsDownloadOrder ebicsOrder)
    throws EbicsException
  {
      super(session, ebicsOrder);
  }

  @Override
  public void buildInitialization() throws EbicsException {
      EbicsRequestDocument.EbicsRequest request;
      EbicsRequestDocument.EbicsRequest.Header header;
      EbicsRequestDocument.EbicsRequest.Body body;
      MutableHeaderType mutable;
      StaticHeaderType xstatic;
      StaticHeaderType.Product product;
      StaticHeaderType.BankPubKeyDigests bankPubKeyDigests;
      StaticHeaderType.BankPubKeyDigests.Authentication authentication;
      StaticHeaderType.BankPubKeyDigests.Encryption encryption;
      StaticHeaderOrderDetailsType.AdminOrderType adminOrderType;
      StaticHeaderOrderDetailsType orderDetails;

      mutable = EbicsXmlFactory.createMutableHeaderType("Initialisation", null);
      product = EbicsXmlFactory.createProduct(session.getProduct());
      authentication = EbicsXmlFactory.createAuthentication(session.getConfiguration().getAuthenticationVersion(),
              "http://www.w3.org/2001/04/xmlenc#sha256",
              decodeHex(session.getUser().getPartner().getBank().getX002Digest()));
      encryption = EbicsXmlFactory.createEncryption(session.getConfiguration().getEncryptionVersion(),
              "http://www.w3.org/2001/04/xmlenc#sha256",
              decodeHex(session.getUser().getPartner().getBank().getE002Digest()));
      bankPubKeyDigests = EbicsXmlFactory.createBankPubKeyDigests(authentication, encryption);
      adminOrderType = EbicsXmlFactory.createAdminOrderType(ebicsOrder.getAdminOrderType().toString());

      if (ebicsOrder.getAdminOrderType().equals(EbicsAdminOrderType.BTD)) {
          BTDParamsType btdParamsType = EbicsXmlFactory.createBTDParamsType((EbicsDownloadOrder)ebicsOrder);

          if (Boolean.getBoolean(session.getSessionParam("TEST"))) {
              ParameterDocument.Parameter parameter;
              ParameterDocument.Parameter.Value value;

              value = EbicsXmlFactory.createValue("String", "TRUE");
              parameter = EbicsXmlFactory.createParameter("TEST", value);
              btdParamsType.setParameterArray(new ParameterDocument.Parameter[]{parameter});
          }
          orderDetails = EbicsXmlFactory.createStaticHeaderOrderDetailsType(adminOrderType, btdParamsType);
      } else {
          StandardOrderParamsType standardOrderParamsType = EbicsXmlFactory.createStandardOrderParamsType();
          orderDetails = EbicsXmlFactory.createStaticHeaderOrderDetailsType(null,
                  adminOrderType,
                  standardOrderParamsType);
      }

      xstatic = EbicsXmlFactory.createStaticHeaderType(session.getBankID(),
              nonce,
              session.getUser().getPartner().getPartnerId(),
              product,
              session.getUser().getSecurityMedium(),
              session.getUser().getUserId(),
              Calendar.getInstance(),
              orderDetails,
              bankPubKeyDigests);
      header = EbicsXmlFactory.createEbicsRequestHeader(true, mutable, xstatic);
      body = EbicsXmlFactory.createEbicsRequestBody();
      request = EbicsXmlFactory.createEbicsRequest(header, body);
      document = EbicsXmlFactory.createEbicsRequestDocument(request);
  }

  // --------------------------------------------------------------------
  // DATA MEMBERS
  // --------------------------------------------------------------------

  private static final long 			serialVersionUID = 3776072549761880272L;
}
