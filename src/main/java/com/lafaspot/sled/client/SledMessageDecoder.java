/**
 *
 */
package com.lafaspot.sled.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import com.lafaspot.logfast.logging.Logger;

/**
 * Class to decode messages from POP server.
 *
 * @author kraman
 *
 */
public class SledMessageDecoder extends MessageToMessageDecoder<String> {

    private final SledSession session;

    public SledMessageDecoder(@Nonnull SledSession session, @Nonnull Logger logger) {
        this.session = session;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final String msg, final List<Object> out) throws IOException {
        session.onResponse(msg);
    }
}
