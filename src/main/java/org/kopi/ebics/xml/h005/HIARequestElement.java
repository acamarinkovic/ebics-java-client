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
import org.kopi.ebics.session.EbicsSession;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.utils.Utils;

/**
 * The <code>HIARequestElement</code> is the root element used
 * to send the authentication and encryption keys to the ebics
 * bank server
 *
 * @author hachani
 *
 */
public class HIARequestElement extends DefaultEbicsRootElement {

  /**
   * Constructs a new HIA Request root element
   * @param session the current ebics session
   */
  public HIARequestElement(EbicsSession session, String orderId) {
    super(session);
    this.orderId = orderId;
  }

  @Override
  public void build() throws EbicsException {
    requestOrderData = new HIARequestOrderDataElement(session);
    requestOrderData.build();
    unsecuredRequest = new UnsecuredRequestElement(session, EbicsAdminOrderType.HIA, Utils.zip(requestOrderData.prettyPrint()));
    unsecuredRequest.build();
  }

  @Override
  public byte[] toByteArray() {
    setSaveSuggestedPrefixes("http://www.ebics.org/h005", "");

    return unsecuredRequest.toByteArray();
  }

  @Override
  public void validate() throws EbicsException {
    unsecuredRequest.validate();
  }

  public HIARequestOrderDataElement getRequestOrderData() {
    return requestOrderData;
  }

  // --------------------------------------------------------------------
  // DATA MEMBERS
  // --------------------------------------------------------------------
  private String			orderId;
  private HIARequestOrderDataElement requestOrderData;
  private UnsecuredRequestElement unsecuredRequest;
  private static final long 		serialVersionUID = 1130436605993828777L;
}
