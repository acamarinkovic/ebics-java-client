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

package org.kopi.ebics.interfaces;

import org.kopi.ebics.exception.EbicsException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public interface ContentFactory extends Serializable {

  /**
   * Returns a new data source of the data to be sent.
   * The instance must ensure that the returned stream will
   * deliver the identical data during the lifetime of this instance.
   * Nevertheless how often the method will be called.
   * @return a new data source of the data to be sent.
   * @throws IOException
   */
  public InputStream getContent() throws IOException;

  default byte[] getFactoryContent() throws EbicsException {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = getContent().read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead);
      }
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new EbicsException(e.getMessage());
    }
  }
}
