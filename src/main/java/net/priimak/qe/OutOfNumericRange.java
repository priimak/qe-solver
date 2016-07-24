package net.priimak.qe;

/**
 * This exception is thrown if computing solution resulted in catastrophic numeric overflow.
 */
public final class OutOfNumericRange extends Exception {
    private static final long serialVersionUID = 6866488171317329644L;

    public OutOfNumericRange() { }

    public OutOfNumericRange(String msg) {
        super(msg);
    }
}
