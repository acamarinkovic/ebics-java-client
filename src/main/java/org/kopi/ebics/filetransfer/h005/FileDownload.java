package org.kopi.ebics.filetransfer.h005;

import org.bouncycastle.util.encoders.Hex;
import org.kopi.ebics.client.h005.TransferState;
import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.http.h005.TraceableHttpClient;
import org.kopi.ebics.http.h005.factory.HttpTransferSession;
import org.kopi.ebics.http.h005.factory.ITraceableHttpClientFactory;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.interfaces.h005.TraceManager;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;
import org.kopi.ebics.io.h005.Joiner;
import org.kopi.ebics.order.h005.EbicsDownloadOrder;
import org.kopi.ebics.session.h005.EbicsSession;
import org.kopi.ebics.utils.h005.Utils;
import org.kopi.ebics.xml.h005.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileDownload {

    private static final Logger logger = LoggerFactory.getLogger(FileDownload.class);
    private final ITraceableHttpClientFactory<TraceableHttpClient> httpClient;
    private final TraceManager traceManager;

    public FileDownload(
            ITraceableHttpClientFactory<TraceableHttpClient> httpClient,
            TraceManager traceManager
    ) {
        this.httpClient = httpClient;
        this.traceManager = traceManager;
    }

    public ByteArrayOutputStream fetchFile(
            EbicsSession ebicsSession,
            IBankConnectionTraceSession traceSession,
            EbicsDownloadOrder downloadOrder
    ) throws EbicsException {
        logger.info("Start downloading file via EBICS sessionId={}, userId={}, partnerId={}, bankURL={}, order={}",
                ebicsSession.getSessionId(),
                ebicsSession.getUser().getUserId(),
                ebicsSession.getUser().getPartner().getPartnerId(),
                ebicsSession.getUser().getPartner().getBank().getURL(),
                downloadOrder.toString()
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EbicsAdminOrderType orderType = downloadOrder.getAdminOrderType();
        HttpTransferSession httpSession = new HttpTransferSession(ebicsSession);
        DownloadInitializationRequestElement initializer = new DownloadInitializationRequestElement(ebicsSession, downloadOrder);
        initializer.build();
        initializer.validate();

        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(initializer.prettyPrint()));

        DownloadInitializationResponseElement response = new DownloadInitializationResponseElement(responseBody, orderType);

        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
                response.report();
            } catch (EbicsException e) {
                throw new EbicsServerException(e.getReturnCode());
            }
            return null;
        });*/

        TransferState state = new TransferState(response.getSegmentsNumber(), response.getTransactionId());
        state.setSegmentNumber(response.getSegmentNumber());
        Joiner joiner = new Joiner(ebicsSession.getUserCert());
        joiner.append(response.getOrderData());

        while (state.hasNext()) {
            int segmentNumber = state.next();
            fetchFileSegment(
                    ebicsSession,
                    orderType,
                    segmentNumber,
                    state.isLastSegment(),
                    state.getTransactionId(),
                    joiner,
                    traceSession,
                    httpSession
            );
        }

        try (OutputStream dest = outputStream) {
            joiner.writeTo(dest, response.getTransactionKey());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ReceiptRequestElement receipt = new ReceiptRequestElement(ebicsSession, state.getTransactionId());
        receipt.build();
        receipt.validate();

        ByteArrayContentFactory receiptResponseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(receipt.prettyPrint()));

        ReceiptResponseElement receiptResponse = new ReceiptResponseElement(receiptResponseBody);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                receiptResponse.build();
                receiptResponse.report();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/

        logger.info("Finished downloading file via EBICS sessionId={}, userId={}, partnerId={}, bankURL={}, order={}, transactionId={}, fileLength={}",
                ebicsSession.getSessionId(),
                ebicsSession.getUser().getUserId(),
                ebicsSession.getUser().getPartner().getPartnerId(),
                ebicsSession.getUser().getPartner().getBank().getURL(),
                downloadOrder,
                Utils.toHexString(state.getTransactionId()),
                outputStream.size()
        );

        return outputStream;
    }

    private void fetchFileSegment(
            EbicsSession session,
            EbicsAdminOrderType orderType,
            int segmentNumber,
            boolean lastSegment,
            byte[] transactionId,
            Joiner joiner,
            IBankConnectionTraceSession traceSession,
            HttpTransferSession httpSession
    ) throws EbicsException {
        DownloadTransferRequestElement downloader = new DownloadTransferRequestElement(
                session,
                orderType,
                segmentNumber,
                lastSegment,
                transactionId
        );
        downloader.build();
        downloader.validate();

        ByteArrayContentFactory responseBody = httpClient.sendAndTraceRequest(httpSession, traceSession, new ByteArrayContentFactory(downloader.prettyPrint()));

        DownloadTransferResponseElement response = new DownloadTransferResponseElement(responseBody, orderType);
        /*traceManager.callAndUpdateLastTrace(traceSession, () -> {
            try {
                response.build();
                response.report();
            } catch (EbicsException e) {
                throw new RuntimeException(e);
            }
            return null;
        });*/

        joiner.append(response.getOrderData());
    }
}
