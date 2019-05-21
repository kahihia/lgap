package edu.lenzing.lgap.microservice.auction.service;

import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface DutchAuctionTurnService {

    @Fluent
    DutchAuctionTurnService getTurnsByAuctionId(Long auctionId, Handler<AsyncResult<List<DutchAuctionTurn>>> handler);

    @Fluent
    DutchAuctionTurnService getCurrentTurnByAuctionId(Long auctionId, Handler<AsyncResult<DutchAuctionTurn>> handler);

    @Fluent
    DutchAuctionTurnService markTurnAsFinished(DutchAuctionTurn turn, Handler<AsyncResult<DutchAuctionTurn>> handler);

    @Fluent
    DutchAuctionTurnService markTurnsAsFinished(List<DutchAuctionTurn> turns, Handler<AsyncResult<List<DutchAuctionTurn>>> handler);

    @Fluent
    DutchAuctionTurnService markTurnAsStarted(DutchAuctionTurn turn, Handler<AsyncResult<DutchAuctionTurn>> handler);

    @Fluent
    DutchAuctionTurnService rollNextTurn(DutchAuctionTurn currentTurn, DutchAuctionTurn nextTurn, Handler<AsyncResult<Void>> handler);

}
