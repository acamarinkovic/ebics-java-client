package org.kopi.ebics.session.h005;

import org.kopi.ebics.client.h005.user.EbicsUser;
import org.kopi.ebics.enumeration.h005.EbicsVersion;
import org.kopi.ebics.interfaces.h005.EbicsBank;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.interfaces.h005.ITraceOrderTypeDefinition;

import java.util.UUID;

public class BankConnectionTraceSession implements IBankConnectionTraceSession {

    private EbicsUser bankConnection;
    private ITraceOrderTypeDefinition orderType;
    private boolean upload;
    private boolean request;
    private String sessionId;
    private EbicsVersion ebicsVersion;
    private String orderNumber;
    private EbicsBank bank;
    private Long lastTraceId;

    public BankConnectionTraceSession(
            EbicsUser bankConnection,
            ITraceOrderTypeDefinition orderType,
            boolean upload,
            boolean request,
            String sessionId,
            EbicsVersion ebicsVersion,
            String orderNumber,
            EbicsBank bank,
            Long lastTraceId
    ) {
        this.bankConnection = bankConnection;
        this.orderType = orderType;
        this.upload = upload;
        this.request = request;
        this.sessionId = sessionId;
        this.ebicsVersion = ebicsVersion;
        this.orderNumber = orderNumber;
        this.bank = bank;
        this.lastTraceId = lastTraceId;
    }

    public BankConnectionTraceSession(EbicsSession ebicsSession, ITraceOrderTypeDefinition orderType,
                                      boolean upload, boolean request) {
        this(ebicsSession.getUser(),
                orderType,
                upload,
                request,
                ebicsSession.getSessionId(),
                EbicsVersion.H005,
                UUID.randomUUID().toString(),
                ebicsSession.getUser().getPartner().getBank(),
                null);
    }


    @Override
    public EbicsUser getBankConnection() {
        return this.bankConnection;
    }

    @Override
    public EbicsBank getBank() {
        return this.bank;
    }

    @Override
    public ITraceOrderTypeDefinition getOrderType() {
        return this.orderType;
    }

    @Override
    public boolean isUpload() {
        return this.upload;
    }

    @Override
    public boolean isRequest() {
        return this.request;
    }

    @Override
    public EbicsVersion getEbicsVersion() {
        return this.ebicsVersion;
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public String getOrderNumber() {
        return this.orderNumber;
    }

    @Override
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public Long getLastTraceId() {
        return this.lastTraceId;
    }

    @Override
    public void setLastTraceId(Long lastTraceId) {
        this.lastTraceId = lastTraceId;
    }

    public void setBankConnection(EbicsUser bankConnection) {
        this.bankConnection = bankConnection;
    }

    public void setOrderType(ITraceOrderTypeDefinition orderType) {
        this.orderType = orderType;
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

    public void setBank(EbicsBank bank) {
        this.bank = bank;
    }
}
