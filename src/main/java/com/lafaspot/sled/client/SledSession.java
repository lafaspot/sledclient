/**
 *
 */
package com.lafaspot.sled.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;

import com.lafaspot.logfast.logging.Logger;
import com.lafaspot.sled.client.SledException.Type;

/**
 * @author kraman
 *
 */
public class SledSession {

    private final AtomicReference<State> stateRef = new AtomicReference<>(State.NULL);

    private final Bootstrap bootstrap;

    @Nonnull
    private final String server;

    private final int port;

    private final Logger logger;

    private Channel sessionChannel;

    public enum State {
        NULL, CONNECT_SENT, CONNECTED, COMMAND_SENT
    }

    public SledSession(Bootstrap bootstrap, String server, int port, Logger logger) {
        this.bootstrap = bootstrap;
        this.server = server;
        this.port = port;
        this.logger = logger;
    }

    public SledFuture<Boolean> connect(final int connectTimeout, final int inactivityTimeout) throws SledException {
        logger.debug(" +++ connect to  " + server, null);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
        ChannelFuture future = bootstrap.connect(server, port);

        // future.channel().pipeline().addLast("inactivityHandler", new PopInactivityHandler(this, inactivityTimeout, logger));

        // future.awaitUninterruptibly();
        stateRef.compareAndSet(State.NULL, State.CONNECT_SENT);

        sessionChannel = future.channel();

        final SledSession thisSession = this;
        final SledFuture<Boolean> futureToReturn = new SledFuture<Boolean>(future);
        future.addListener(new GenericFutureListener<Future<? super Void>>() {

            @Override
            public void operationComplete(final Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {

                    if (!stateRef.compareAndSet(State.CONNECT_SENT, State.CONNECTED)) {
                        logger.error("Connect success in invalid state " + stateRef.get().name(), null);
                        return;
                    }


                    sessionChannel.pipeline().addLast(new SledMessageDecoder(thisSession, logger));

                    futureToReturn.done(Boolean.TRUE);
                }
            }
        });
        return futureToReturn;
    }

    SledFuture<String> currentFuture;

    public SledFuture<String> getSled() throws SledException {

        if (stateRef.get() != State.CONNECTED) {
            throw new SledException(Type.INVALID_STATE);
        }

        Future f = sessionChannel.writeAndFlush("\n");
        currentFuture = new SledFuture<String>(f);
        return currentFuture;
    }

    public void onResponse(String msg) {
        currentFuture.done(msg);
        this.sessionChannel.close();
    }


}
