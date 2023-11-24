package org.kopi.ebics.interfaces.h005;


public interface EbicsPartner {

    /**
     * Returns the bank we are customer of.
     * @return the bank we are customer of
     */
    EbicsBank getBank();

    /**
     * Returns the customers id at the bank.
     * @return the customers id at the bank.
     */
    String getPartnerId();

    /**
     * Creates the next order number.
     * @return the next order number.
     */
    String nextOrderId();

}
