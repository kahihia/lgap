package edu.lenzing.lgap.microservice.auction.service;

import edu.lenzing.lgap.microservice.auction.model.Auction;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@VertxGen
@ProxyGen
public interface AuctionBroadcastService {

    @Fluent
    AuctionBroadcastService auctionStarted(final Auction auction, final Handler<AsyncResult<Void>> handler);

    @Fluent
    AuctionBroadcastService auctionEnded(final Auction auction, final Handler<AsyncResult<Void>> handler);

    @Fluent
    AuctionBroadcastService auctionCanceled(final Auction auction, final Handler<AsyncResult<Void>> handler);

    @Fluent
    AuctionBroadcastService turnEnded(final Auction auction, final int turnNumber, final Handler<AsyncResult<Void>> handler);

    @Fluent
    AuctionBroadcastService turnStarted(final Auction auction, final int turnNumber, final Handler<AsyncResult<Void>> handler);

}
