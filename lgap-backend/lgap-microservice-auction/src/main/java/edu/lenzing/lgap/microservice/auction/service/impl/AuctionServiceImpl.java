package edu.lenzing.lgap.microservice.auction.service.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.lenzing.lgap.microservice.auction.manager.DutchAuctionManager;
import edu.lenzing.lgap.microservice.auction.model.*;
import edu.lenzing.lgap.microservice.auction.repository.AuctionRepository;
import edu.lenzing.lgap.microservice.auction.service.AuctionService;
import edu.lenzing.lgap.microservice.auction.service.DutchAuctionService;
import edu.lenzing.lgap.microservice.auction.service.DutchAuctionTurnService;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import edu.lenzing.lgap.microservice.common.service.exception.ServiceException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionServiceImpl implements AuctionService {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionServiceImpl.class);

    @Inject
    private Vertx vertx;

    @Inject
    private DutchAuctionService dutchAuctionService;

    @Inject
    private DutchAuctionTurnService dutchAuctionTurnService;

    @Inject
    private AuctionRepository auctionRepository;

    @Inject
    private Provider<DutchAuctionManager> dutchAuctionManagerProvider;

    @Override
    public AuctionService search(final JsonObject filter, final JsonArray allowedUserRegions, final Handler<AsyncResult<List<JsonObject>>> handler) {

        final List<UserRegion> userRegions = allowedUserRegions.stream()
                .map(Object::toString)
                .map(UserRegion::valueOf)
                .collect(Collectors.toList());

        LOG.info("Filter: " + filter.encode());
        LOG.info("Regions: " + allowedUserRegions.encode());

        auctionRepository.search(filter).setHandler(repository -> {
            if (repository.succeeded()) {

                final List<JsonObject> resultList = repository.result()
                        .stream()
                        .filter(auction -> !Collections.disjoint(auction.getRegions(), userRegions))
                        .map(Auction::toJson)
                        .collect(Collectors.toList());

                handler.handle(Future.succeededFuture(resultList));

            } else {
                LOG.error(String.format("Search operation failed: %s", repository.cause().getMessage()), repository.cause());
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }

    @Override
    public AuctionService advancedSearch(final JsonObject filter, final Handler<AsyncResult<List<JsonObject>>> handler) {

        auctionRepository.search(filter).setHandler(repository -> {
            if (repository.succeeded()) {

                final List<JsonObject> resultList = repository.result()
                        .stream()
                        .map(Auction::toJson)
                        .collect(Collectors.toList());

                handler.handle(Future.succeededFuture(resultList));

            } else {
                LOG.error(String.format("Advanced search operation failed: %s", repository.cause().getMessage()), repository.cause());
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }

    @Override
    public AuctionService getAuctionByIdAndType(final Long auctionId, final String auctionType, final JsonArray allowedUserRegions, Handler<AsyncResult<JsonObject>> handler) {

        try {

            final AuctionType type = AuctionType.valueOf(auctionType.toUpperCase());

            switch (type) {

                case DUTCH:
                    this.getDutchAuctionById(auctionId, allowedUserRegions, handler);
                    break;

                default:
                    handler.handle(ServiceException.failedFuture(String.format("Unsupported auction type [%s]", auctionType)));
                    break;

            }

        } catch (final IllegalArgumentException ex) {
            handler.handle(ServiceException.failedFuture(String.format("Illegal auction type [%s]", auctionType)));
        }

        return this;
    }

    @Override
    public AuctionService getAuctionTurnByAuctionIdAndType(Long auctionId, String auctionType, Handler<AsyncResult<JsonObject>> handler) {

        try {

            final AuctionType type = AuctionType.valueOf(auctionType.toUpperCase());

            switch (type) {

                case DUTCH:
                    this.getDutchAuctionTurnByAuctionId(auctionId, handler);
                    break;

                default:
                    handler.handle(ServiceException.failedFuture(String.format("Unsupported auction type [%s]", auctionType)));
                    break;

            }

        } catch (final IllegalArgumentException ex) {
            handler.handle(ServiceException.failedFuture(String.format("Illegal auction type [%s]", auctionType)));
        }

        return this;
    }

    @Override
    public AuctionService createAuction(final JsonObject auctionData, final Handler<AsyncResult<JsonObject>> handler) {

        final String auctionType = auctionData.getString("auctionType");

        if (auctionType == null) {

            LOG.error(String.format("Incorrect auction data: auction type [auctionType] is missing from the object %s", auctionData.encode()));
            handler.handle(ServiceException.failedFuture("Can not create auction: incorrect data"));

        } else {

            try {

                final AuctionType type = AuctionType.valueOf(auctionType.toUpperCase());

                switch (type) {

                    case DUTCH:
                        this.createDutchAuction(auctionData, handler);
                        break;

                    default:
                        handler.handle(ServiceException.failedFuture(String.format("Unsupported auction type [%s]", auctionType)));
                        break;

                }

            } catch (final IllegalArgumentException ex) {
                handler.handle(ServiceException.failedFuture(String.format("Illegal auction type [%s]", auctionType)));
            }

        }

        return this;
    }

    @Override
    public AuctionService cancelAuction(final Long auctionId, final Handler<AsyncResult<Void>> handler) {
        return this;
    }

    private void getDutchAuctionById(final Long id, final JsonArray allowedUserRegions, final Handler<AsyncResult<JsonObject>> handler) {

        dutchAuctionService.getById(id, allowedUserRegions, service -> {
            if (service.succeeded()) {
                final JsonObject auction = service.result() != null ? service.result().toJson() : null;
                handler.handle(Future.succeededFuture(auction));
            } else {
                LOG.error(String.format("Failed to get Dutch auction [%d]: %s", id, service.cause().getMessage()), service.cause());
                handler.handle(ServiceException.failedFuture(service.cause()));
            }
        });

    }

    private void getDutchAuctionTurnByAuctionId(final Long auctionId, final Handler<AsyncResult<JsonObject>> handler) {

        dutchAuctionTurnService.getCurrentTurnByAuctionId(auctionId, service -> {
            if (service.succeeded()) {
                final DutchAuctionTurn turn = service.result();
                JsonObject json = null;
                if (turn != null) {
                    json = turn.toJson();
                }
                handler.handle(Future.succeededFuture(json));
            } else {
                handler.handle(ServiceException.failedFuture(service.cause()));
            }
        });

    }

    private void createDutchAuction(final JsonObject auctionData, final Handler<AsyncResult<JsonObject>> handler) {

        dutchAuctionService.create(auctionData, service -> {
            if (service.succeeded()) {

                final DutchAuction auction = service.result();

                handler.handle(Future.succeededFuture(auction.toJson()));

                final DutchAuctionManager manager = dutchAuctionManagerProvider.get();
                manager.manage(auction);

                vertx.deployVerticle(manager);

            } else {
                LOG.error(String.format("Failed to create Dutch auction: %s", service.cause().getMessage()), service.cause());
                handler.handle(ServiceException.failedFuture(service.cause()));
            }
        });

    }
}
