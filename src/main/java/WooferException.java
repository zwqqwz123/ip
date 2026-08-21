/**
 * Represents an expected input error reported by Woofer.
 */
public class WooferException extends Exception {
    /**
     * Creates an exception with a user-friendly error message.
     *
     * @param message explanation of the input error
     */
    public WooferException(String message) {
        super(message);
    }
}
