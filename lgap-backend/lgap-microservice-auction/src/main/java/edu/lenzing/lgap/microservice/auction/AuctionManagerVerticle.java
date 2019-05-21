package edu.lenzing.lgap.microservice.auction;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.auction.service.DutchAuctionService;
import edu.lenzing.lgap.microservice.common.verticle.BaseMicroserviceVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionManagerVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionManagerVerticle.class);

    @Inject
    private DutchAuctionService dutchAuctionService;

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        dutchAuctionService.startAuctionManagers(handler -> {
            if (handler.succeeded()) {
                future.complete();
            } else {
                LOG.error("Failed to start dutch auction managers", handler.cause());
                future.fail(handler.cause());
            }
        });

    }

}
