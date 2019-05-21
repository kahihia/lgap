package edu.lenzing.lgap.microservice.auction.service;

import edu.lenzing.lgap.microservice.auction.model.DutchAuction;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface DutchAuctionService {

    String SERVICE_ADDRESS = "service-eb-auction-dutch";

    @Fluent
    DutchAuctionService getById(Long id, JsonArray allowedUserRegions, Handler<AsyncResult<DutchAuction>> handler);

    @Fluent
    DutchAuctionService create(JsonObject auctionData, Handler<AsyncResult<DutchAuction>> handler);

    @Fluent
    DutchAuctionService startAuctionManagers(Handler<AsyncResult<Void>> handler);

    @Fluent
    DutchAuctionService startAuction(DutchAuction auction, Handler<AsyncResult<DutchAuction>> handler);

    @Fluent
    DutchAuctionService endAuction(DutchAuction auction, Handler<AsyncResult<DutchAuction>> handler);

    @Fluent
    DutchAuctionService cancelAuction(DutchAuction auction, Handler<AsyncResult<DutchAuction>> handler);

}
