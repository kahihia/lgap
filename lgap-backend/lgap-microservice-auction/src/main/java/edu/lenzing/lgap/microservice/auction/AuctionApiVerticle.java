package edu.lenzing.lgap.microservice.auction;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.auction.model.Bid;
import edu.lenzing.lgap.microservice.auction.service.AuctionService;
import edu.lenzing.lgap.microservice.auction.service.BidService;
import edu.lenzing.lgap.microservice.common.model.UserRole;
import edu.lenzing.lgap.microservice.common.verticle.BaseApiVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

import java.util.Collections;
import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionApiVerticle extends BaseApiVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionApiVerticle.class);

    @Inject
    private AuctionService auctionService;

    @Inject
    private BidService bidService;

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.route().handler(JWTAuthHandler.create(super.getJWTAuthProvider()));

        // Get auction by ID
        router.get("/get/:auctionType/:auctionId").handler(context ->
                jwtAuth(context, this::apiGetAuctionById, UserRole.CUSTOMER, UserRole.ADMIN));

        // Get current auction by auction ID
        router.get("/get/turn/:auctionType/:auctionId").handler(context ->
                jwtAuth(context, this::apiGetCurrentAuctionTurnByAuctionId, UserRole.CUSTOMER, UserRole.ADMIN));

        // Simple Search
        router.post("/search").handler(context ->
                jwtAuth(context, this::apiAuctionSearch, UserRole.CUSTOMER));

        // Advanced Search
        router.post("/search/advanced").handler(context ->
                jwtAuth(context, this::apiAdvancedAuctionSearch, UserRole.ADMIN));

        // Create Auction
        router.post("/create").handler(context ->
                jwtAuth(context, this::apiCreateAuction, UserRole.ADMIN));

        // Cancel Auction
        router.post("/cancel/:auctionId").handler(context ->
                jwtAuth(context, this::apiCancelAuction, UserRole.ADMIN));

        // Place bid
        router.post("/bid").handler(context ->
                jwtAuth(context, this::apiPlaceBid, UserRole.CUSTOMER));

        final JsonObject apiConfig = config().getJsonObject("api");
        createHttpServer(router, apiConfig.getString("http.address"), apiConfig.getInteger("http.port"))
                .compose(serverCreated -> publishHttpEndpoint(apiConfig))
                .setHandler(future.completer());

    }

    private void apiAuctionSearch(final RoutingContext routingContext) {

        try {

            final JsonObject filter = routingContext.getBodyAsJson();
            final JsonArray regions = routingContext.user().principal().getJsonArray("lgap.user.regions");

            auctionService.search(filter, regions, handler -> {
                if (handler.succeeded()) {
                    final List<JsonObject> result = handler.result() != null ? handler.result() : Collections.emptyList();
                    respondWithSuccess(routingContext, new JsonArray(result));
                } else {
                    LOG.error(handler.cause());
                    respondWithInternalError(routingContext, handler.cause());
                }
            });

        } catch (final DecodeException ex) {
            LOG.error("Invalid or no filter data received", ex);
            respondWithBadRequest(routingContext);
        }

    }

    private void apiAdvancedAuctionSearch(final RoutingContext routingContext) {

        try {

            final JsonObject filter = routingContext.getBodyAsJson();

            auctionService.advancedSearch(filter, handler -> {
                if (handler.succeeded()) {
                    final List<JsonObject> result = handler.result() != null ? handler.result() : Collections.emptyList();
                    respondWithSuccess(routingContext, new JsonArray(result));
                } else {
                    LOG.error(handler.cause());
                    respondWithInternalError(routingContext, handler.cause());
                }
            });

        } catch (final DecodeException ex) {
            LOG.error("Invalid or no filter data received", ex);
            respondWithBadRequest(routingContext);
        }

    }

    private void apiGetAuctionById(final RoutingContext routingContext) {

         try {

            final String auctionTypeParam = routingContext.request().getParam("auctionType");
            final String auctionIdParam = routingContext.request().getParam("auctionId");

            final Long auctionId = Long.parseLong(auctionIdParam);
             final JsonArray regions = routingContext.user().principal().getJsonArray("lgap.user.regions");

            auctionService.getAuctionByIdAndType(auctionId, auctionTypeParam, regions, handler -> {
                if (handler.succeeded()) {
                    if (handler.result() != null) {
                        respondWithSuccess(routingContext, handler.result());
                    } else {
                        respondWithNotFound(routingContext);
                    }
                } else {
                    LOG.error(handler.cause());
                    respondWithInternalError(routingContext, handler.cause());
                }
            });

        } catch (final NumberFormatException ex) {
            LOG.error(String.format("Invalid parameter: %s", ex.getMessage()), ex);
            respondWithBadRequest(routingContext);
        }

    }

    private void apiGetCurrentAuctionTurnByAuctionId(final RoutingContext routingContext) {

        try {

            final String auctionTypeParam = routingContext.request().getParam("auctionType");
            final String auctionIdParam = routingContext.request().getParam("auctionId");

            final Long auctionId = Long.parseLong(auctionIdParam);

            auctionService.getAuctionTurnByAuctionIdAndType(auctionId, auctionTypeParam, handler -> {
                if (handler.succeeded()) {
                    if (handler.result() != null) {
                        respondWithSuccess(routingContext, handler.result());
                    } else {
                        respondWithNotFound(routingContext);
                    }
                } else {
                    LOG.error(handler.cause());
                    respondWithInternalError(routingContext, handler.cause());
                }
            });

        } catch (final NumberFormatException ex) {
            LOG.error(String.format("Invalid parameter: %s", ex.getMessage()), ex);
            respondWithBadRequest(routingContext);
        }

    }

    private void apiCreateAuction(final RoutingContext routingContext) {

        try {

            final JsonObject auctionData = routingContext.getBodyAsJson();

            auctionService.createAuction(auctionData, handler -> {
                if (handler.succeeded()) {
                    respondWithSuccess(routingContext, handler.result());
                } else {
                    LOG.error(handler.cause());
                    respondWithInternalError(routingContext, handler.cause());
                }
            });

        } catch (final DecodeException ex) {
            LOG.error("Invalid auction data received", ex);
            respondWithBadRequest(routingContext);
        }

    }

    private void apiCancelAuction(final RoutingContext routingContext) {}

    private void apiPlaceBid(final RoutingContext routingContext) {

        try {

            final JsonObject json = routingContext.getBodyAsJson();
            final Bid bid = new Bid(json);

            bidService.bid(bid, handler -> {
                if (handler.succeeded()) {
                    respondWithSuccess(routingContext, handler.result().toJson());
                } else {
                    LOG.error(handler.cause());
                    respondWithInternalError(routingContext, handler.cause());
                }
            });

        } catch (final DecodeException ex) {
            LOG.error("Invalid or no bid data received", ex);
            respondWithBadRequest(routingContext);
        }

    }

}
