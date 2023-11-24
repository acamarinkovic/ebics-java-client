package org.kopi.ebics.session.h005;

import org.kopi.ebics.enumeration.h005.EbicsVersion;
import org.kopi.ebics.interfaces.h005.EbicsBank;
import org.kopi.ebics.interfaces.h005.ITraceOrderTypeDefinition;
import org.kopi.ebics.interfaces.h005.IBankTraceSession;

import java.util.UUID;

public class BankTraceSession implements IBankTraceSession {

    private EbicsBank bank;
    private boolean upload;
    private boolean request;
    private String sessionId;
    private EbicsVersion ebicsVersion;
    private ITraceOrderTypeDefinition orderType;
    private String orderNumber;
    private Long lastTraceId;

    public BankTraceSession(
            EbicsBank bank,
            boolean upload,
            boolean request,
            String sessionId,
            EbicsVersion ebicsVersion,
            ITraceOrderTypeDefinition orderType
    ) {
        this.bank = bank;
        this.upload = upload;
        this.request = request;
        this.sessionId = sessionId;
        this.ebicsVersion = ebicsVersion;
        this.orderType = orderType;
        this.orderNumber = UUID.randomUUID().toString();
        this.lastTraceId = null;
    }

    @Override
    public EbicsBank getBank() {
        return bank;
    }

    @Override
    public boolean isUpload() {
        return upload;
    }

    @Override
    public boolean isRequest() {
        return request;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public EbicsVersion getEbicsVersion() {
        return ebicsVersion;
    }

    @Override
    public ITraceOrderTypeDefinition getOrderType() {
        return orderType;
    }

    @Override
    public String getOrderNumber() {
        return orderNumber;
    }

    @Override
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public Long getLastTraceId() {
        return lastTraceId;
    }

    @Override
    public void setLastTraceId(Long lastTraceId) {
        this.lastTraceId = lastTraceId;
    }

    public void setBank(EbicsBank bank) {
        this.bank = bank;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setEbicsVersion(EbicsVersion ebicsVersion) {
        this.ebicsVersion = ebicsVersion;
    }

    public void setOrderType(ITraceOrderTypeDefinition orderType) {
        this.orderType = orderType;
    }
}
