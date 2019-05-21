package edu.lenzing.lgap.microservice.auction.service.impl;

import edu.lenzing.lgap.microservice.auction.model.Auction;
import edu.lenzing.lgap.microservice.auction.service.AuctionBroadcastService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionBroadcastServiceImpl implements AuctionBroadcastService {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionBroadcastServiceImpl.class);

    @Override
    public AuctionBroadcastService auctionStarted(final Auction auction, final Handler<AsyncResult<Void>> handler) {
        LOG.info("AUCTION STARTED " + auction.toJson().encode());
        handler.handle(null);
        return this;
    }

    @Override
    public AuctionBroadcastService auctionEnded(final Auction auction, final Handler<AsyncResult<Void>> handler) {
        LOG.info("AUCTION ENDED " + auction.toJson().encode());
        handler.handle(null);
        return this;
    }

    @Override
    public AuctionBroadcastService auctionCanceled(final Auction auction, final Handler<AsyncResult<Void>> handler) {
        LOG.info("AUCTION CANCELED " + auction.toJson().encode());
        handler.handle(null);
        return this;
    }

    @Override
    public AuctionBroadcastService turnEnded(final Auction auction, final int turnNumber, final Handler<AsyncResult<Void>> handler) {
        LOG.info("AUCTION TURN #" + turnNumber + " ENDED " + auction.toJson().encode());
        handler.handle(null);
        return this;
    }

    @Override
    public AuctionBroadcastService turnStarted(final Auction auction, final int turnNumber, final Handler<AsyncResult<Void>> handler) {
        LOG.info("AUCTION TURN #" + turnNumber + " STARTED " + auction.toJson().encode());
        handler.handle(null);
        return this;
    }
}
