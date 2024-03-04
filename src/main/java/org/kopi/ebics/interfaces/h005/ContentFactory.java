package org.kopi.ebics.interfaces.h005;


import org.kopi.ebics.exception.EbicsException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public interface ContentFactory extends Serializable {

    InputStream getContent() throws IOException;
    
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
