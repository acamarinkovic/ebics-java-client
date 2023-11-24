package org.kopi.ebics.io.h005;

import org.kopi.ebics.interfaces.h005.ContentFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayContentFactory implements ContentFactory {

    private byte[] byteContent;

    public ByteArrayContentFactory(byte[] content){
        this.byteContent = content;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(byteContent);
    }
}
