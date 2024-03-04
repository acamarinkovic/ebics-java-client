package org.kopi.ebics.exception.h005;

import java.util.HashMap;
import java.util.Map;

public class EbicsReturnCode extends AbstractEbicsReturnCode {

    private static final Map<String, EbicsReturnCode> returnCodes = new HashMap<>();
    public static final EbicsReturnCode EBICS_OK = create("000000", "EBICS_OK");
    public static final EbicsReturnCode EBICS_DOWNLOAD_POSTPROCESS_DONE = create("011000", "EBICS_DOWNLOAD_POSTPROCESS_DONE");
    public static final EbicsReturnCode EBICS_DOWNLOAD_POSTPROCESS_SKIPPED = create("011001", "EBICS_DOWNLOAD_POSTPROCESS_SKIPPED");
    public static final EbicsReturnCode EBICS_TX_SEGMENT_NUMBER_UNDERRUN = create("011101", "EBICS_TX_SEGMENT_NUMBER_UNDERRUN");
    public static final EbicsReturnCode EBICS_ORDER_PARAMS_IGNORED = create("031003", "EBICS_ORDER_PARAMS_IGNORED");
    public static final EbicsReturnCode EBICS_AUTHENTICATION_FAILED = create("061001", "EBICS_AUTHENTICATION_FAILED");
    public static final EbicsReturnCode EBICS_INVALID_REQUEST = create("061002", "EBICS_INVALID_REQUEST");
    public static final EbicsReturnCode EBICS_INTERNAL_ERROR = create("061099", "EBICS_INTERNAL_ERROR");
    public static final EbicsReturnCode EBICS_TX_RECOVERY_SYNC = create("061101", "EBICS_TX_RECOVERY_SYNC");

    public static final EbicsReturnCode EBICS_NO_DOWNLOAD_DATA_AVAILABLE = create("090005", "EBICS_NO_DOWNLOAD_DATA_AVAILABLE");
    public static final EbicsReturnCode EBICS_INVALID_USER_OR_USER_STATE = create("091002", "EBICS_INVALID_USER_OR_USER_STATE");
    public static final EbicsReturnCode EBICS_USER_UNKNOWN = create("091003", "EBICS_USER_UNKNOWN");
    public static final EbicsReturnCode EBICS_INVALID_USER_STATE = create("091004", "EBICS_INVALID_USER_STATE");
    public static final EbicsReturnCode EBICS_INVALID_ORDER_IDENTIFIER = create("091005", "EBICS_INVALID_ORDER_IDENTIFIER");
    public static final EbicsReturnCode EBICS_UNSUPPORTED_ORDER_IDENTIFIER = create("091006", "EBICS_UNSUPPORTED_ORDER_IDENTIFIER");
    public static final EbicsReturnCode EBICS_DISTRIBUTED_SIGNATURE_AUTHORISATION_FAILED = create("091007", "EBICS_DISTRIBUTED_SIGNATURE_AUTHORISATION_FAILED");
    public static final EbicsReturnCode EBICS_BANK_PUBKEY_UPDATE_REQUIRED = create("091008", "EBICS_BANK_PUBKEY_UPDATE_REQUIRED");
    public static final EbicsReturnCode EBICS_SEGMENT_SIZE_EXCEEDED = create("091009", "EBICS_SEGMENT_SIZE_EXCEEDED");
    public static final EbicsReturnCode EBICS_INVALID_XML = create("091010", "EBICS_INVALID_XML");
    public static final EbicsReturnCode EBICS_INVALID_HOST_ID = create("091011", "EBICS_INVALID_HOST_ID");
    public static final EbicsReturnCode EBICS_TX_UNKNOWN_TXID = create("091101", "EBICS_TX_UNKNOWN_TXID");
    public static final EbicsReturnCode EBICS_TX_ABORT = create("091102", "EBICS_TX_ABORT");
    public static final EbicsReturnCode EBICS_TX_MESSAGE_REPLAY = create("091103", "EBICS_TX_MESSAGE_REPLAY");
    public static final EbicsReturnCode EBICS_TX_SEGMENT_NUMBER_EXCEEDED = create("091104", "EBICS_TX_SEGMENT_NUMBER_EXCEEDED");
    public static final EbicsReturnCode EBICS_INVALID_ORDER_PARAMS = create("091112", "EBICS_INVALID_ORDER_PARAMS");
    public static final EbicsReturnCode EBICS_INVALID_REQUEST_CONTENT = create("091113", "EBICS_INVALID_REQUEST_CONTENT");
    public static final EbicsReturnCode EBICS_MAX_ORDER_DATA_SIZE_EXCEEDED = create("091117", "EBICS_MAX_ORDER_DATA_SIZE_EXCEEDED");
    public static final EbicsReturnCode EBICS_MAX_SEGMENTS_EXCEEDED = create("091118", "EBICS_MAX_SEGMENTS_EXCEEDED");
    public static final EbicsReturnCode EBICS_MAX_TRANSACTIONS_EXCEEDED = create("091119", "EBICS_MAX_TRANSACTIONS_EXCEEDED");
    public static final EbicsReturnCode EBICS_PARTNER_ID_MISMATCH = create("091120", "EBICS_PARTNER_ID_MISMATCH");
    public static final EbicsReturnCode EBICS_ORDER_ALREADY_EXISTS = create("091122", "EBICS_ORDER_ALREADY_EXISTS");

    public static final EbicsReturnCode EBICS_X509_CERTIFICATE_NOT_VALID_YET = create("091209", "EBICS_X509_CERTIFICATE_NOT_VALID_YET");
    public static final EbicsReturnCode EBICS_SIGNATURE_VERIFICATION_FAILED = create("091301", "EBICS_SIGNATURE_VERIFICATION_FAILED");
    public static final EbicsReturnCode EBICS_INVALID_ORDER_DATA_FORMAT = create("090004", "EBICS_INVALID_ORDER_DATA_FORMAT");

    private EbicsReturnCode(String code, String text) {
        super(code, text);
    }

    public static EbicsReturnCode create(String code, String symbolicName) {
        EbicsReturnCode returnCode = new EbicsReturnCode(code, symbolicName);
        returnCodes.put(code, returnCode);
        return returnCode;
    }

    public static EbicsReturnCode toReturnCode(String code, String text) {
        return new EbicsReturnCode(code, text);
    }

    public static EbicsReturnCode toReturnCode(String code) {
        return returnCodes.getOrDefault(
                code, new EbicsReturnCode(
                        code, String.format("Unknown EBICS error %s", code)
                )
        );
    }

    @Override
    public boolean isOk() {
        return code.equals(EBICS_OK.code);
    }

}
