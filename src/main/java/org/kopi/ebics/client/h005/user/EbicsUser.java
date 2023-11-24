package org.kopi.ebics.client.h005.user;


import org.kopi.ebics.interfaces.h005.EbicsPartner;

public class EbicsUser extends EbicsUserInfo {
    EbicsPartner partner;

    public EbicsPartner getPartner() {
        return partner;
    }

    public void setPartner(EbicsPartner partner) {
        this.partner = partner;
    }
}
