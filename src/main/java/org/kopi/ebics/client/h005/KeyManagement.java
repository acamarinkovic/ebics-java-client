package org.kopi.ebics.client.h005;

import org.kopi.ebics.certificate.h005.BankCertificateManager;
import org.kopi.ebics.enumeration.h005.EbicsUserAction;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.http.h005.TraceableHttpClient;
import org.kopi.ebics.http.h005.factory.HttpTransferSession;
import org.kopi.ebics.http.h005.factory.IHttpTransferSession;
import org.kopi.ebics.http.h005.factory.ITraceableHttpClientFactory;
import org.kopi.ebics.interfaces.h005.IKeyManager;
import org.kopi.ebics.interfaces.h005.TraceManager;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;
import org.kopi.ebics.session.h005.BankConnectionTraceSession;
import org.kopi.ebics.session.h005.EbicsSession;
import org.kopi.ebics.utils.h005.Utils;
import org.kopi.ebics.xml.h005.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class KeyManagement implements IKeyManager {
    private final ITraceableHttpClientFactory<TraceableHttpClient> httpClient;
    private final TraceManager traceManager;

    public KeyManagement(ITraceableHttpClientFactory<TraceableHttpClient> httpClient, TraceManager traceManager) {
        this.httpClient = httpClient;
        this.traceManager = traceManager;
    }

    @Override
    public void sendINI(EbicsSession ebicsSession, BankConnectionTraceSession traceSession) throws EbicsException, IOException {
        ebicsSession.getUser().checkAction(EbicsUserAction.INI);
        IHttpTransferSession httpSession = new HttpTransferSession(ebicsSession);
        INIRequestElement request = new INIRequestElement(ebicsSession);
        request.build();
        request.validate();
        //traceManager.trace(new ByteArrayContentFactory(request.getSignaturePubKey().toByteArray()), traceSession, false);
        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(request.prettyPrint()));
        KeyManagementResponseElement response = new KeyManagementResponseElement(responseBody);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
                response.report();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/
        ebicsSession.getUser().updateStatus(EbicsUserAction.INI);
    }

    @Override
    public void sendHIA(EbicsSession ebicsSession, BankConnectionTraceSession traceSession) throws EbicsException {
        ebicsSession.getUser().checkAction(EbicsUserAction.HIA);
        IHttpTransferSession httpSession = new HttpTransferSession(ebicsSession);
        HIARequestElement request = new HIARequestElement(ebicsSession);
        request.build();
        request.validate();
        //traceManager.trace(new ByteArrayContentFactory(request.getRequestOrderData().toByteArray()), traceSession, false);
        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(request.prettyPrint()));
        KeyManagementResponseElement response = new KeyManagementResponseElement(responseBody);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
                response.report();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/
        ebicsSession.getUser().updateStatus(EbicsUserAction.HIA);
    }

    @Override
    public BankCertificateManager sendHPB(EbicsSession ebicsSession, BankConnectionTraceSession traceSession, String password) throws IOException, GeneralSecurityException, EbicsException {
        ebicsSession.getUser().checkAction(EbicsUserAction.HPB);
        IHttpTransferSession httpSession = new HttpTransferSession(ebicsSession);
        HPBRequestElement request = new HPBRequestElement(ebicsSession);
        request.build();
        request.validate();
        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(request.prettyPrint()));
        KeyManagementResponseElement response = new KeyManagementResponseElement(responseBody);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
                response.report();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/
        ByteArrayContentFactory factory = new ByteArrayContentFactory(Utils.unzip(ebicsSession.getUserCert().decrypt(response.getOrderData(), response.getTransactionKey())));
        HPBResponseOrderDataElement orderData = new HPBResponseOrderDataElement(factory);
        orderData.build();
        //traceManager.trace(new ByteArrayContentFactory(orderData.toByteArray()), traceSession, false);
        BankCertificateManager manager = BankCertificateManager.createFromCertificates(orderData.getBankE002Certificate(), orderData.getBankX002Certificate());
        ebicsSession.getUser().updateStatus(EbicsUserAction.HPB);
        return manager;
    }

    @Override
    public void lockAccess(EbicsSession ebicsSession, BankConnectionTraceSession traceSession) throws IOException, EbicsException {
        ebicsSession.getUser().checkAction(EbicsUserAction.SPR);
        IHttpTransferSession httpSession = new HttpTransferSession(ebicsSession);
        SPRRequestElement request = new SPRRequestElement(ebicsSession);
        request.build();
        request.validate();
        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(request.prettyPrint()));
        SPRResponseElement response = new SPRResponseElement(responseBody);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
                response.report();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/
        ebicsSession.getUser().updateStatus(EbicsUserAction.SPR);
    }
}
