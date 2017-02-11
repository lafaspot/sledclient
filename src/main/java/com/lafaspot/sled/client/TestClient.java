/**
 *
 */
package com.lafaspot.sled.client;

import java.util.concurrent.ExecutionException;

import com.lafaspot.logfast.logging.LogContext;
import com.lafaspot.logfast.logging.LogManager;
import com.lafaspot.logfast.logging.Logger;
import com.lafaspot.logfast.logging.Logger.Level;

/**
 * @author kraman
 *
 */
public class TestClient {

    public static void main(String args[]) throws SledException, InterruptedException, ExecutionException {

        LogManager logManager = new LogManager(Level.DEBUG, 5);
        logManager.setLegacy(true);
        Logger logger = logManager.getLogger(new LogContext(TestClient.class.getName()) {
        });
        SledClient cli = new SledClient(10, logManager);

        final String server = "localhost";
        final int port = 14053;
        final int connectTimeout = 1000;
        final int inactivityTimeout = 1000;

        SledSession sess = cli.createSession(server, port);

        System.out.println("connecting to " + server + ", " + port);
        SledFuture<Boolean> f1 = sess.connect(connectTimeout, inactivityTimeout);

        f1.get();
        System.out.println("connected");

        SledFuture<String> f2 = sess.getSled();
        System.out.println("getting sled");
        System.out.println(" SLED " + f2.get());
        System.out.println("done");
        cli.shutdown();
    }

}
