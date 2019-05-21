package edu.lenzing.lgap.microservice.auction.service;

import edu.lenzing.lgap.microservice.common.model.UserRegion;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface AuctionService {

    @Fluent
    AuctionService search(JsonObject filter, JsonArray allowedUserRegions, Handler<AsyncResult<List<JsonObject>>> handler);

    @Fluent
    AuctionService advancedSearch(JsonObject filter, Handler<AsyncResult<List<JsonObject>>> handler);

    @Fluent
    AuctionService getAuctionByIdAndType(Long auctionId, String auctionType, JsonArray allowedUserRegions, Handler<AsyncResult<JsonObject>> handler);

    @Fluent
    AuctionService getAuctionTurnByAuctionIdAndType(Long auctionId, String auctionType, Handler<AsyncResult<JsonObject>> handler);

    @Fluent
    AuctionService createAuction(JsonObject auctionData, Handler<AsyncResult<JsonObject>> handler);

    @Fluent
    AuctionService cancelAuction(Long auctionId, Handler<AsyncResult<Void>> handler);

}
