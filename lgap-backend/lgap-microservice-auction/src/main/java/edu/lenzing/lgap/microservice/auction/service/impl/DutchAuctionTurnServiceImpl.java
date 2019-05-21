package edu.lenzing.lgap.microservice.auction.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.auction.manager.DutchAuctionManager;
import edu.lenzing.lgap.microservice.auction.service.DutchAuctionTurnService;
import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import edu.lenzing.lgap.microservice.auction.repository.DutchAuctionTurnRepository;
import edu.lenzing.lgap.microservice.common.service.exception.ServiceException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class DutchAuctionTurnServiceImpl implements DutchAuctionTurnService {

    @Inject
    private Vertx vertx;

    @Inject
    private DutchAuctionTurnRepository turnRepository;

    @Override
    public DutchAuctionTurnService getTurnsByAuctionId(final Long auctionId, final Handler<AsyncResult<List<DutchAuctionTurn>>> handler) {

        turnRepository.getAllByAuctionId(auctionId).setHandler(handler);

        return this;
    }

    @Override
    public DutchAuctionTurnService getCurrentTurnByAuctionId(final Long auctionId, final Handler<AsyncResult<DutchAuctionTurn>> handler) {

        final String currentTurnEventBusAddress = String.format("%s-%d", DutchAuctionManager.CURRENT_TURN_SERVICE_ADDRESS, auctionId);

        vertx.eventBus().send(currentTurnEventBusAddress, "current-turn", new DeliveryOptions().setSendTimeout(2000L), reply -> {
           if (reply.succeeded()) {
                final Message<Object> message = reply.result();
                DutchAuctionTurn turn = null;
                if (message.body() != null) {
                    turn = new DutchAuctionTurn((JsonObject) message.body());
                }
                handler.handle(Future.succeededFuture(turn));
           } else {
               handler.handle(ServiceException.failedFuture(reply.cause()));
           }
        });

        return this;
    }

    @Override
    public DutchAuctionTurnService markTurnAsFinished(final DutchAuctionTurn turn, final Handler<AsyncResult<DutchAuctionTurn>> handler) {

        turnRepository.markAsFinished(turn).setHandler(handler);

        return this;
    }

    @Override
    public DutchAuctionTurnService markTurnsAsFinished(final List<DutchAuctionTurn> turns, final Handler<AsyncResult<List<DutchAuctionTurn>>> handler) {

        turnRepository.markAllAsFinished(turns).setHandler(handler);

        return this;
    }

    @Override
    public DutchAuctionTurnService markTurnAsStarted(final DutchAuctionTurn turn, final Handler<AsyncResult<DutchAuctionTurn>> handler) {

        turnRepository.markAsStarted(turn).setHandler(handler);

        return this;
    }

    @Override
    public DutchAuctionTurnService rollNextTurn(final DutchAuctionTurn currentTurn, final DutchAuctionTurn nextTurn, final Handler<AsyncResult<Void>> handler) {

        turnRepository.rollNextTurn(currentTurn, nextTurn).setHandler(handler);

        return this;
    }
}
