package org.kopi.ebics.exception.h005;

import java.util.Objects;

public abstract class AbstractEbicsReturnCode {

    protected final String code;
    protected final String text;

    public AbstractEbicsReturnCode(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public abstract boolean isOk();

    public void throwException() throws EbicsServerException {
        throw new EbicsServerException((EbicsReturnCode) this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AbstractEbicsReturnCode that = (AbstractEbicsReturnCode) obj;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " " + text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
