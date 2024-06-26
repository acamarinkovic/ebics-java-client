package org.kopi.ebics.client.h005;

import org.kopi.ebics.interfaces.Savable;
import org.kopi.ebics.interfaces.EbicsBank;
import org.kopi.ebics.interfaces.h005.EbicsPartner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Partner implements EbicsPartner, Savable {
    /**
     * Reconstructs a persisted EBICS customer.
     *
     * @param bank the bank
     * @param ois  the stream object
     * @throws IOException
     */
    public Partner(EbicsBank bank, ObjectInputStream ois) throws IOException {
        this.bank = bank;
        this.partnerId = ois.readUTF();
        this.orderId = ois.readInt();
    }

    /**
     * First time constructor.
     *
     * @param bank      the bank
     * @param partnerId the partner ID
     */
    public Partner(EbicsBank bank, String partnerId) {
        this.bank = bank;
        this.partnerId = partnerId;
        needSave = true;
    }

    /**
     * Returns the next order available ID
     *
     * @return the next order ID
     */
    public Integer getNextOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID
     *
     * @param orderId the order ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
        needSave = true;
    }

    @Override
    public void save(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(partnerId);
        oos.writeInt(orderId);
        oos.flush();
        oos.close();
        needSave = false;
    }

    /**
     * Did any persistable attribute change since last load/save operation.
     *
     * @return True if the object needs to be saved.
     */
    public boolean needsSave() {
        return needSave;
    }

    @Override
    public EbicsBank getBank() {
        return bank;
    }

    @Override
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * In EBICS XSD schema - ebics_types.xsd, The order ID pattern
     * is defined as following: <b>pattern value="[A-Z][A-Z0-9]{3}"</b>.
     * <p>This means that the order ID should start with a letter
     * followed by three alphanumeric characters.
     *
     * <p> The <code>nextOrderId()</code> aims to generate orders from
     * <b>A000</b> to <b>ZZZZ</b>. The sequence cycle is performed infinitely.
     *
     * <p> The order index {@link org.kopi.ebics.client.Partner#} is saved whenever it
     * changes.
     */
    @Override
    public String nextOrderId() {
        char[] chars = new char[4];

        orderId += 1;
        if (orderId > 36 * 36 * 36 * 36 - 1 || orderId < 10 * 36 * 36 * 36) {
            // ensure that orderId starts with a letter
            orderId = 10 * 36 * 36 * 36;
        }
        chars[3] = ALPHA_NUM_CHARS.charAt(orderId % 36);
        chars[2] = ALPHA_NUM_CHARS.charAt((orderId / 36) % 36);
        chars[1] = ALPHA_NUM_CHARS.charAt((orderId / 36 / 36) % 36);
        chars[0] = ALPHA_NUM_CHARS.charAt(orderId / 36 / 36 / 36);
        needSave = true;

        return new String(chars);
    }

    @Override
    public String getSaveName() {
        return "partner-" + partnerId + ".cer";
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private EbicsBank bank;
    private int orderId = 10 * 36 * 36 * 36;
    private String partnerId;
    private transient boolean needSave;

    private static final String ALPHA_NUM_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
}
