package edu.lenzing.lgap.launcher;

import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ClusteredLauncher extends Launcher {

    private static final Logger LOG = LoggerFactory.getLogger(ClusteredLauncher.class);

    public static void main(String[] args) {
        new ClusteredLauncher().dispatch(args);
    }

    @Override
    public void beforeStartingVertx(final VertxOptions options) {
        // TODO: load cluster host from config file
        options.setClusterHost("127.0.0.1").setClustered(true);
    }
}
