package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.AuthorisationLevel;
import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;

import java.math.BigInteger;

public class OrderType {

    private final EbicsAdminOrderType adminOrderType;
    private final EbicsService service;
    private final String description;
    private final AuthorisationLevel authorizationLevel;
    private final BigInteger numSigRequired;

    public OrderType(EbicsAdminOrderType adminOrderType, EbicsService service,
                     String description, AuthorisationLevel authorizationLevel,
                     BigInteger numSigRequired) {
        this.adminOrderType = adminOrderType;
        this.service = service;
        this.description = description;
        this.authorizationLevel = authorizationLevel;
        this.numSigRequired = numSigRequired;
    }

    public EbicsAdminOrderType getAdminOrderType() {
        return adminOrderType;
    }

    public EbicsService getService() {
        return service;
    }

    public String getDescription() {
        return description;
    }

    public AuthorisationLevel getAuthorizationLevel() {
        return authorizationLevel;
    }

    public BigInteger getNumSigRequired() {
        return numSigRequired;
    }
}
