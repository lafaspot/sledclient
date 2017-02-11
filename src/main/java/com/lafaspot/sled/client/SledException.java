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

    private String message = null;

    private Type type;

    public SledException(@Nonnull Type type) {
        super(type.toString());
        this.type = type;
    }

    public SledException(@Nonnull final Type failureType, @Nullable final Throwable cause) {
        super(failureType.toString(), cause);
    }

    public enum Type {
        CONNECT_FAILURE, TIMEDOUT, PARSE_FAILURE, INVALID_STATE, INTERNAL_FAILURE
    }

}
