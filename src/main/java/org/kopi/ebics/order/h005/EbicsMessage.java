package org.kopi.ebics.order.h005;

public class EbicsMessage implements IEbicsMessage {
    private String messageName;
    private String messageNameVariant;
    private String messageNameVersion;
    private String messageNameFormat;

    public EbicsMessage(
            String messageName,
            String messageNameVariant,
            String messageNameVersion,
            String messageNameFormat
    ) {
        this.messageName = messageName;
        this.messageNameVariant = messageNameVariant;
        this.messageNameVersion = messageNameVersion;
        this.messageNameFormat = messageNameFormat;
    }

    @Override
    public String getMessageName() {
        return messageName;
    }

    @Override
    public String getMessageNameVariant() {
        return messageNameVariant;
    }

    @Override
    public String getMessageNameVersion() {
        return messageNameVersion;
    }

    @Override
    public String getMessageNameFormat() {
        return messageNameFormat;
    }

    @Override
    public String toString() {
        return String.join(".",
                messageName,
                messageNameVariant != null ? messageNameVariant : "_",
                messageNameVersion != null ? messageNameVersion : "_"
        );
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public void setMessageNameVariant(String messageNameVariant) {
        this.messageNameVariant = messageNameVariant;
    }

    public void setMessageNameVersion(String messageNameVersion) {
        this.messageNameVersion = messageNameVersion;
    }

    public void setMessageNameFormat(String messageNameFormat) {
        this.messageNameFormat = messageNameFormat;
    }
}

