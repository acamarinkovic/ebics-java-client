package org.kopi.ebics.client.h005;

public class TransferState {

    private final int numSegments;
    private final byte[] transactionId;

    // Actual segment number
    private int segmentNumber = 0;

    private boolean isLastSegment = false;

    public TransferState(int numSegments, byte[] transactionId) {
        this.numSegments = numSegments;
        this.transactionId = transactionId;
    }

    /**
     * Returns the next segment number to be transferred.
     *
     * @return the next segment number to be transferred.
     */
    public int next() {
        segmentNumber++;
        if (segmentNumber == numSegments) {
            isLastSegment = true;
        }
        return segmentNumber;
    }

    /**
     * Checks if there is a next segment.
     *
     * @return True if there is a next segment, false otherwise.
     */
    public boolean hasNext() {
        return segmentNumber < numSegments;
    }

    /**
     * Sets the segment number.
     *
     * @param segmentNumber the segment number
     */
    public void setSegmentNumber(int segmentNumber) {
        this.segmentNumber = segmentNumber;
    }

    /**
     * Checks if the current segment is the last one.
     *
     * @return True if it is the last segment
     */
    public boolean isLastSegment() {
        return isLastSegment;
    }

    public int getNumSegments() {
        return numSegments;
    }

    public byte[] getTransactionId() {
        return transactionId;
    }

    public int getSegmentNumber() {
        return segmentNumber;
    }

    public void setLastSegment(boolean lastSegment) {
        isLastSegment = lastSegment;
    }
}
