package edu.lenzing.lgap.microservice.auction.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.auction.manager.DutchAuctionManager;
import edu.lenzing.lgap.microservice.auction.model.Bid;
import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import edu.lenzing.lgap.microservice.auction.repository.BidRepository;
import edu.lenzing.lgap.microservice.auction.service.BidService;
import edu.lenzing.lgap.microservice.common.service.exception.ServiceException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class BidServiceImpl implements BidService {

    private static final Logger LOG = LoggerFactory.getLogger(BidServiceImpl.class);

    @Inject
    private Vertx vertx;

    @Inject
    private BidRepository bidRepository;

    @Override
    public BidService bid(final Bid bid, final Handler<AsyncResult<Bid>> handler) {

        bidRepository.save(bid).setHandler(repository -> {
            if (repository.succeeded()) {

                final String eventBusAddress = String.format("%s-%d", DutchAuctionManager.CURRENT_TURN_SERVICE_ADDRESS, bid.getAuctionId());

                vertx.eventBus().send(eventBusAddress, "end", reply -> {
                    if (reply.succeeded()) {
                        handler.handle(Future.succeededFuture(repository.result()));
                    } else {
                        handler.handle(ServiceException.failedFuture(reply.cause()));
                    }
                });

            } else {
                LOG.error(String.format("Failed to save bid: %s", bid.toJson().encode()));
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }
}
