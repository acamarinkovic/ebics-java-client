package org.kopi.ebics.client.h005.user;

import org.kopi.ebics.enumeration.h005.EbicsUserAction;
import org.kopi.ebics.enumeration.h005.EbicsUserStatusEnum;
import org.kopi.ebics.enumeration.h005.EbicsVersion;

public class EbicsUserInfo {
    private EbicsVersion ebicsVersion;
    private String userId;
    private String name;
    private String dn;
    private EbicsUserStatusEnum userStatus;
    private boolean useCertificate;
    private String securityMedium;


    public EbicsVersion getEbicsVersion() {
        return ebicsVersion;
    }


    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }


    public String getDn() {
        return dn;
    }


    public EbicsUserStatusEnum getUserStatus() {
        return userStatus;
    }

    public String getSecurityMedium() {
        return "0000";
    }

    public void setUserStatus(EbicsUserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }


    public boolean isUseCertificate() {
        return useCertificate;
    }


    public void setUseCertificate(boolean useCertificate) {
        this.useCertificate = useCertificate;
    }


    public void checkAction(EbicsUserAction action) {
        userStatus.checkAction(action);
    }


    public void updateStatus(EbicsUserAction action) {
        userStatus = userStatus.updateStatus(action);
    }
}
