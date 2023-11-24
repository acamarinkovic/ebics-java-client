package org.kopi.ebics.client.h005.user;

import org.kopi.ebics.enumeration.h005.EbicsUserAction;
import org.kopi.ebics.enumeration.h005.EbicsUserStatusEnum;

public class EbicsUserStatus {

    private EbicsUserStatusEnum status;

    public EbicsUserStatus(EbicsUserStatusEnum status) {
        this.status = status;
    }

    public EbicsUserStatus() {
        this(EbicsUserStatusEnum.CREATED);
    }

    public EbicsUserStatusEnum getStatus() {
        return status;
    }

    private void setStatus(EbicsUserStatusEnum status) {
        this.status = status;
    }

    public void update(EbicsUserAction action) {
        setStatus(getStatus().updateStatus(action));
    }

    public void check(EbicsUserAction action) {
        getStatus().checkAction(action);
    }
}
