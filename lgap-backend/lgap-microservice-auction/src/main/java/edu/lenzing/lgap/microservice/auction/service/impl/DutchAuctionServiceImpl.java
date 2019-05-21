package edu.lenzing.lgap.microservice.auction.service.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.lenzing.lgap.microservice.auction.manager.DutchAuctionManager;
import edu.lenzing.lgap.microservice.auction.model.*;
import edu.lenzing.lgap.microservice.auction.repository.DutchAuctionRepository;
import edu.lenzing.lgap.microservice.auction.repository.DutchAuctionTurnRepository;
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
public class DutchAuctionServiceImpl implements DutchAuctionService {

    private static final Logger LOG = LoggerFactory.getLogger(DutchAuctionServiceImpl.class);

    @Inject
    private Vertx vertx;

    @Inject
    private DutchAuctionRepository dutchAuctionRepository;

    @Inject
    private DutchAuctionTurnService dutchAuctionTurnService;

    @Inject
    private Provider<DutchAuctionManager> dutchAuctionManagerProvider;

    @Override
    public DutchAuctionService getById(final Long id, final JsonArray allowedUserRegions, final Handler<AsyncResult<DutchAuction>> handler) {

        final List<UserRegion> userRegions = allowedUserRegions.stream()
                .map(Object::toString)
                .map(UserRegion::valueOf)
                .collect(Collectors.toList());

        dutchAuctionRepository.getById(id).setHandler(resultHandler -> {
            if (resultHandler.succeeded()) {

                if (resultHandler.result().isPresent()) {

                    final DutchAuction auction = resultHandler.result().get();

                    if (!Collections.disjoint(auction.getRegions(), userRegions)) {
                        handler.handle(Future.succeededFuture(auction));
                    } else {
                        handler.handle(Future.succeededFuture(null));
                    }

                } else {
                    handler.handle(Future.succeededFuture(null));
                }

            } else {
                handler.handle(Future.failedFuture(resultHandler.cause()));
            }
        });

        return this;

    }

    public DutchAuctionService getManagedAuctions(final Handler<AsyncResult<List<DutchAuction>>> handler) {

        dutchAuctionRepository.getActiveAuctions().setHandler(handler);

        return this;

    }

    @Override
    public DutchAuctionService create(final JsonObject auctionData, final Handler<AsyncResult<DutchAuction>> handler) {

        LOG.info("Creating auction " + auctionData.encodePrettily());

        final JsonObject cargoData = auctionData.getJsonObject("auctionCargo");
        final JsonObject regionData = auctionData.getJsonObject("auctionRegions");
        final JsonObject extendedAuctionData = auctionData.getJsonObject("extendedAuctionData");

        // Extract cargo
        final Cargo cargo = new Cargo();
        cargo.setExtProductId(cargoData.getLong("cargoProductId"));
        cargo.setQuantity(cargoData.getDouble("cargoQuantity"));
        cargo.setLocation(cargoData.getString("cargoLocation"));
        cargo.setExpectedDeliveryDays(cargoData.getInteger("cargoExpectedDeliveryDays"));

        // Extract auction regions
        final Set<UserRegion> regions = new HashSet<>();
        if (regionData.getBoolean("euus", false)) regions.add(UserRegion.EU_US);
        if (regionData.getBoolean("northAsia", false)) regions.add(UserRegion.NORTH_ASIA);
        if (regionData.getBoolean("amea", false)) regions.add(UserRegion.AMEA);

        // Extract dutch auction
        final DutchAuction dutchAuction = new DutchAuction();
        dutchAuction.setName(auctionData.getString("auctionName"));
        dutchAuction.setDescription(auctionData.getString("auctionDescription"));
        dutchAuction.setStartTimestamp(auctionData.getLong("auctionStartTimestamp"));
        dutchAuction.setEndTimestamp(auctionData.getLong("auctionEndTimestamp"));
        dutchAuction.setReservePrice(auctionData.getDouble("auctionReservePrice"));
        dutchAuction.setAuctionType(AuctionType.DUTCH);
        dutchAuction.setAuctionPhase(AuctionPhase.PREVIEW);
        dutchAuction.setRegions(regions);
        dutchAuction.setCargo(cargo);
        dutchAuction.setBasePrice(extendedAuctionData.getDouble("dutchAuctionBasePrice"));
        dutchAuction.setPriceModifier(extendedAuctionData.getDouble("dutchAuctionPriceModifier"));
        dutchAuction.setTurns(this.generateAuctionTurns(dutchAuction));

        // Save auction
        dutchAuctionRepository.save(dutchAuction).setHandler(repository -> {
            if (repository.succeeded()) {
                final DutchAuction persistedAuction = repository.result();
                dutchAuctionTurnService.getTurnsByAuctionId(persistedAuction.getId(), turnHandler -> {
                   if (turnHandler.succeeded()) {
                       persistedAuction.setTurns(turnHandler.result());
                       handler.handle(Future.succeededFuture(persistedAuction));
                   } else {
                       handler.handle(ServiceException.failedFuture(turnHandler.cause()));
                   }
                });
            } else {
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }

    @Override
    public DutchAuctionService startAuctionManagers(final Handler<AsyncResult<Void>> handler) {

        dutchAuctionRepository.getActiveAuctions().setHandler(repository -> {
            if (repository.succeeded()) {

                for (final DutchAuction auction : repository.result()) {

                    final DutchAuctionManager manager = dutchAuctionManagerProvider.get();
                    manager.manage(auction);

                    vertx.deployVerticle(manager);

                }

                handler.handle(Future.succeededFuture());

            } else {
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }

    @Override
    public DutchAuctionService startAuction(final DutchAuction auction, final Handler<AsyncResult<DutchAuction>> handler) {

        dutchAuctionRepository.changePhase(auction, AuctionPhase.RUNNING).setHandler(handler);

        return this;
    }

    @Override
    public DutchAuctionService endAuction(final DutchAuction auction, final Handler<AsyncResult<DutchAuction>> handler) {

        dutchAuctionRepository.changePhase(auction, AuctionPhase.FINISHED).setHandler(handler);

        return this;
    }

    @Override
    public DutchAuctionService cancelAuction(final DutchAuction auction, final Handler<AsyncResult<DutchAuction>> handler) {

        dutchAuctionRepository.changePhase(auction, AuctionPhase.CANCELED).setHandler(handler);

        return this;
    }

    private List<DutchAuctionTurn> generateAuctionTurns(final DutchAuction dutchAuction) {

        final List<DutchAuctionTurn> turns = new LinkedList<>();

        final Double startingPrice  = dutchAuction.getBasePrice();
        final Double reservePrice   = dutchAuction.getReservePrice();
        final Double priceDecrement = dutchAuction.getPriceModifier();

        final long totalTurns = (long) ((startingPrice - reservePrice) / priceDecrement);
        final long turnDuration = (dutchAuction.getEndTimestamp() - dutchAuction.getStartTimestamp()) / totalTurns;

        LOG.info(String.format("Starting price:  %f", startingPrice));
        LOG.info(String.format("Reserve price:   %f", reservePrice));
        LOG.info(String.format("Price decrement: %f", priceDecrement));
        LOG.info(String.format("Total turns:     %d", totalTurns));
        LOG.info(String.format("Turn duration:   %d", turnDuration));

        long currentTurnStartTimestamp = dutchAuction.getStartTimestamp();

        for (int i = 0; i < totalTurns; i++) {

            final DutchAuctionTurn turn = new DutchAuctionTurn();
            turn.setTurnNumber(i + 1);
            turn.setStarted(false);
            turn.setFinished(false);
            turn.setTurnDuration(turnDuration);
            turn.setStartTimestamp(currentTurnStartTimestamp);
            turn.setEndTimestamp(currentTurnStartTimestamp + turnDuration);
            turn.setAuction(dutchAuction);

            turns.add(turn);

            LOG.info(turn.toJson().encode());

            currentTurnStartTimestamp += turnDuration;

        }

        return turns;

    }

}
