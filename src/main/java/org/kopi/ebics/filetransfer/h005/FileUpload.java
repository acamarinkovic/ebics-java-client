package org.kopi.ebics.filetransfer.h005;

import org.bouncycastle.util.encoders.Hex;
import org.kopi.ebics.client.h005.TransferState;
import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.http.h005.TraceableHttpClient;
import org.kopi.ebics.http.h005.factory.HttpTransferSession;
import org.kopi.ebics.http.h005.factory.ITraceableHttpClientFactory;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.interfaces.h005.TraceManager;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;
import org.kopi.ebics.order.h005.EbicsUploadOrder;
import org.kopi.ebics.order.h005.EbicsUploadOrderResponse;
import org.kopi.ebics.session.h005.EbicsSession;
import org.kopi.ebics.utils.h005.Utils;
import org.kopi.ebics.xml.h005.InitializationResponseElement;
import org.kopi.ebics.xml.h005.TransferResponseElement;
import org.kopi.ebics.xml.h005.UploadInitializationRequestElement;
import org.kopi.ebics.xml.h005.UploadTransferRequestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class FileUpload {
    private final ITraceableHttpClientFactory<TraceableHttpClient> httpClient;
    private final TraceManager traceManager;

    private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);

    public FileUpload(ITraceableHttpClientFactory<TraceableHttpClient> httpClient, TraceManager traceManager) {
        this.httpClient = httpClient;
        this.traceManager = traceManager;
    }

    public EbicsUploadOrderResponse sendFile(
            EbicsSession session,
            IBankConnectionTraceSession traceSession,
            byte[] content,
            EbicsUploadOrder ebicsUploadOrder
    ) throws IOException, EbicsException {
        logger.info("Start uploading file via EBICS sessionId={}, userId={}, partnerId={}, bankURL={}, order={}, file length={}",
                session.getSessionId(), session.getUser().getUserId(), session.getUser().getPartner().getPartnerId(),
                session.getUser().getPartner().getBank().getURL(), ebicsUploadOrder, content.length);

        EbicsAdminOrderType orderType = ebicsUploadOrder.getAdminOrderType();
        HttpTransferSession httpSession = new HttpTransferSession(session);
        UploadInitializationRequestElement initializer = new UploadInitializationRequestElement(
                session,
                ebicsUploadOrder,
                content
        );
        initializer.build();
        initializer.validate();

        /*traceManager.trace(new ByteArrayContentFactory(initializer.getUserSignature().toByteArray()), traceSession);*/

        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(
                httpSession,
                traceSession,
                new ByteArrayContentFactory(initializer.prettyPrint())
        );

        InitializationResponseElement response = new InitializationResponseElement(
                responseBody,
                orderType
        );
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        traceManager.updateSessionOrderNumber(traceSession, response.getOrderNumber());*/

        TransferState state = new TransferState(initializer.getSegmentNumber(), response.getTransactionId());
        while (state.hasNext()) {
            int segmentNumber = state.next();
            sendFileSegment(
                    session,
                    initializer.getContent(segmentNumber),
                    segmentNumber,
                    state.isLastSegment(),
                    state.getTransactionId(),
                    orderType,
                    traceSession,
                    httpSession
            );
        }

        logger.info("Finished uploading file via EBICS sessionId={}, userId={}, partnerId={}, bankURL={}, order={}, file length={}, orderNumber={}, transactionId={}",
                session.getSessionId(), session.getUser().getUserId(), session.getUser().getPartner().getPartnerId(),
                session.getUser().getPartner().getBank().getURL(), ebicsUploadOrder, content.length,
                response.getOrderNumber(), Utils.toHexString(response.getTransactionId()));

        return new EbicsUploadOrderResponse(response.getOrderNumber(), Utils.toHexString(response.getTransactionId()));
    }

    private void sendFileSegment(
            EbicsSession session,
            ContentFactory contentFactory,
            int segmentNumber,
            boolean lastSegment,
            byte[] transactionId,
            EbicsAdminOrderType orderType,
            IBankConnectionTraceSession traceSession,
            HttpTransferSession httpSession
    ) throws IOException, EbicsException {
        String segmentStr = lastSegment ? "last segment (" + segmentNumber + ")" : "segment (" + segmentNumber + ")";
        logger.info(
                "Uploading {} of file via EBICS sessionId={}, userId={}, partnerId={}, bankURL={}, segmentLength={} Bytes",
                segmentStr, session.getSessionId(), session.getUser().getUserId(),
                session.getUser().getPartner().getPartnerId(), session.getUser().getPartner().getBank().getURL(),
                contentFactory.getContent().available());

        UploadTransferRequestElement uploader = new UploadTransferRequestElement(
                session,
                orderType,
                segmentNumber,
                lastSegment,
                transactionId,
                contentFactory
        );
        uploader.build();
        uploader.validate();

        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(uploader.prettyPrint()));

        TransferResponseElement response = new TransferResponseElement(responseBody);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/
    }
}
