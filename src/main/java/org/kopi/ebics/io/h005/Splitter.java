package org.kopi.ebics.io.h005;

import org.kopi.ebics.utils.h005.CryptoUtils;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.utils.Utils;

import javax.crypto.spec.SecretKeySpec;

public class Splitter {
    /**
     * The maximum size of a segment to be put in a request. base64 encoding adds
     * 33% to that, and we need to stay below 1 MB. This is where 700kB comes from,
     * to make sure we stay below that.
     */
    private static final int SEGMENT_SIZE = 700000;
    /**
     * Constructs a new <code>FileSplitter</code> with a given file.
     * @param input the input byte array
     */
    public Splitter(byte[] input, boolean isCompressionEnabled, SecretKeySpec keySpec) throws EbicsException {
        this.input = input;
        try {
            byte[] compressedInput = isCompressionEnabled ? Utils.zip(input) : input;
            content = CryptoUtils.encrypt(compressedInput, keySpec);
            boolean lastSegmentNotFull = content.length % SEGMENT_SIZE != 0;
            segmentNumber = content.length / SEGMENT_SIZE + (lastSegmentNotFull ? 1 : 0);
            segmentSize = content.length / segmentNumber;
        } catch (Exception e) {
            throw new EbicsException(e.getMessage());
        }
    }

    /**
     * Reads the input stream and splits it to segments of 1MB size.
     *
     * <p>EBICS Specification 2.4.2 - 7 Segmentation of the order data:
     *
     * <p>The following procedure is to be followed with segmentation:
     * <ol>
     *   <li> The order data is ZIP compressed
     *   <li> The compressed order data is encrypted in accordance with Chapter 6.2
     *   <li> The compressed, encrypted order data is base64-coded.
     *    <li> The result is to be verified with regard to the data volume:
     *      <ol>
     *        <li> If the resulting data volume is below the threshold of 1 MB = 1,048,576 bytes,
     *             the order data can be sent complete as a data segment within one transmission step
     *        <li> If the resulting data volume exceeds 1,048,576 bytes the data is to be
     *             separated sequentially and in a base64-conformant manner into segments
     *             that each have a maximum of 1,048,576 bytes.
     *     </ol>
     *
     * @param isCompressionEnabled enable compression?
     * @param keySpec the secret key spec
     * @throws EbicsException
     */
    public final void readInput(boolean isCompressionEnabled, SecretKeySpec keySpec)
            throws EbicsException
    {
        try {
            if (isCompressionEnabled) {
                input = Utils.zip(input);
            }
            content = Utils.encrypt(input, keySpec);
            segmentation();
        } catch (Exception e) {
            throw new EbicsException(e.getMessage());
        }
    }

    /**
     * Slits the input into 1MB portions.
     *
     * <p> EBICS Specification 2.4.2 - 7 Segmentation of the order data:
     *
     * <p>In Version H003 of the EBICS standard, order data that requires more than 1 MB of storage
     * space in compressed, encrypted and base64-coded form MUST be segmented before
     * transmission, irrespective of the transfer direction (upload/download).
     *
     */
    private void segmentation() {

        segmentNumber = content.length / SEGMENT_SIZE;

        if (content.length % SEGMENT_SIZE != 0) {
            segmentNumber++;
        }

        segmentSize = content.length / segmentNumber;
    }

    /**
     * Returns the content of a data segment according to
     * a given segment number.
     *
     * @param segmentNumber the segment number
     * @return
     */
    public ContentFactory getContent(int segmentNumber) {
        byte[]		segment;
        int			offset;

        offset = segmentSize * (segmentNumber - 1);
        if (content.length < segmentSize + offset) {
            segment = new byte[content.length - offset];
        } else {
            segment = new byte[segmentSize];
        }

        System.arraycopy(content, offset, segment, 0, segment.length);
        return new ByteArrayContentFactory(segment);
    }

    /**
     * Returns the hole content.
     * @return the input content.
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Returns the total segment number.
     * @return the total segment number.
     */
    public int getSegmentNumber() {
        return segmentNumber;
    }

    /**
     * Returns the size of each segment.
     * @return the size of each segment.
     */
    int getSegmentSize() { return segmentSize; }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private byte[]				input;
    private byte[]				content;
    private int					segmentSize;
    private int segmentNumber;
    private boolean isCompressionEnabled;
    private SecretKeySpec keySpec;

}
