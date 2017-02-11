/**
 *
 */
package com.lafaspot.sled.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/**
 * @author kraman
 *
 */
public class SledException extends Exception {

	/** Error message. */
    private String message = null;

    /** Type of error. */
    private Type type;

    /**
     * Constructor for exception.
     * @param type of failure
     */
    public SledException(@Nonnull final Type type) {
        super(type.toString());
        this.type = type;
    }

    /**
     * Constructor.
     * @param failureType type of failure
     * @param cause error cause
     */
    public SledException(@Nonnull final Type failureType, @Nullable final Throwable cause) {
        super(failureType.toString(), cause);
    }

    /** 
     * Types of errors.
     * @author kraman
     *
     */
    public enum Type {
    	/** failed to connect to server. */
        CONNECT_FAILURE,
        /** operation timed out. */
        TIMEDOUT, PARSE_FAILURE,
        /** cannot process message - invalid state. */
        INVALID_STATE,
        /** something failed internally. */
        INTERNAL_FAILURE
    }

}
