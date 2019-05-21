package edu.lenzing.lgap.microservice.auction.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.auction.model.*;
import edu.lenzing.lgap.microservice.auction.repository.AuctionRepository;
import edu.lenzing.lgap.microservice.auction.repository.CargoRepository;
import edu.lenzing.lgap.microservice.auction.repository.DutchAuctionRepository;
import edu.lenzing.lgap.microservice.auction.repository.DutchAuctionTurnRepository;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;

import java.util.*;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@Singleton
public class JDBCDutchAuctionRepository extends BaseJDBCEntityRepository<DutchAuction> implements DutchAuctionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCDutchAuctionRepository.class);

    private static final String SQL_GET_BY_ID = "SELECT * FROM `auction` `A`, `dutch-auction` `DA` WHERE `A`.`id` = `DA`.`auctionId` AND `A`.`id` = ?;";
    private static final String SQL_GET_ACTIVE_AUCTIONS = "SELECT * FROM `auction` `A`, `dutch-auction` `DA` WHERE `A`.`id` = `DA`.`auctionId` AND (`A`.`auctionPhase` = 'PREVIEW' OR `A`.`auctionPhase` = 'RUNNING');";
    private static final String SQL_INSERT_DUTCH_AUCTION = "INSERT INTO `dutch-auction` (`auctionId`, `cargoId`, `basePrice`, `priceModifier`) VALUES (?, ?, ?, ?);";
    private static final String SQL_UPDATE_PHASE = "UPDATE `auction` SET `auctionPhase` = ? WHERE `id` = ?;";


    @Inject
    private AuctionRepository auctionRepository;

    @Inject
    private CargoRepository cargoRepository;

    @Inject
    private DutchAuctionTurnRepository dutchAuctionTurnRepository;


    @Inject
    public JDBCDutchAuctionRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, DutchAuction.class);
    }


    @Override
    public Future<Optional<DutchAuction>> getById(final Long id) {

        final Future<Optional<DutchAuction>> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(id);

        retrieveOne(SQL_GET_BY_ID, params, handler -> {

            if (handler.succeeded()) {

                if (handler.result().isPresent()) {

                    final JsonObject json = handler.result().get();
                    final DutchAuction auction = new DutchAuction(json);

                    final Future<Optional<Cargo>> cargoFuture = cargoRepository.getById(json.getLong("cargoId"));
                    final Future<Set<UserRegion>> regionFuture = auctionRepository.getRegionsByAuctionId(auction.getId());
                    final Future<List<DutchAuctionTurn>> turnFuture = dutchAuctionTurnRepository.getAllByAuctionId(auction.getId());

                    CompositeFuture.all(cargoFuture, regionFuture, turnFuture).setHandler(compositeHandler -> {
                        if (compositeHandler.succeeded()) {

                            auction.setCargo(cargoFuture.result().get());
                            auction.setRegions(regionFuture.result());
                            auction.setTurns(turnFuture.result());

                            future.complete(Optional.of(auction));

                        } else {
                            future.fail(compositeHandler.cause());
                        }
                    });

                } else {
                    future.complete(Optional.empty());
                }

            } else {
                future.fail(handler.cause());
            }

        });

        return future;

    }

    @Override
    public Future<DutchAuction> save(final DutchAuction dutchAuction) {

        final Future<DutchAuction> future = Future.future();

        startTransaction(transactionHandler -> {

            if (transactionHandler.succeeded()) {

                final SQLConnection transaction = transactionHandler.result();

                this.save(dutchAuction, transaction).setHandler(transactionCompletionHandler(transaction, future));

            } else {
                future.fail(transactionHandler.cause());
            }

        });

        return future;
    }

    @Override
    public Future<DutchAuction> save(final DutchAuction dutchAuction, final SQLConnection transaction) {

        final Future<DutchAuction> future = Future.future();

        // Persist cargo
        cargoRepository.save(dutchAuction.getCargo(), transaction).setHandler(cargoHandler -> {

            if (cargoHandler.succeeded()) {

                // Persist auction & regions
                auctionRepository.save(dutchAuction, transaction).setHandler(auctionHandler -> {

                    if (auctionHandler.succeeded()) {

                        final DutchAuction persistedAuction = (DutchAuction) auctionHandler.result();

                        // Persist DutchAuction
                        saveDutchAuction(transaction, persistedAuction).setHandler(dutchAuctionHandler -> {

                            if (dutchAuctionHandler.succeeded()) {

                                persistedAuction.getTurns().forEach(turn -> turn.setAuction(persistedAuction));

                                // Persist dutch auction turns
                                dutchAuctionTurnRepository.saveAll(persistedAuction.getTurns(), transaction).setHandler(turnHandler -> {

                                    if (turnHandler.succeeded()) {

                                        // TODO: This is not working, so query the turns later
                                        persistedAuction.setTurns(turnHandler.result());

                                        future.complete(persistedAuction);

                                    } else {
                                        future.fail(turnHandler.cause());
                                    }

                                });

                            } else {
                                future.fail(dutchAuctionHandler.cause());
                            }

                        });


                    } else {
                        future.fail(auctionHandler.cause());
                    }

                });

            } else {
                future.fail(cargoHandler.cause());
            }

        });

        return future;

    }

    @Override
    public Future<DutchAuction> update(DutchAuction entity) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<DutchAuction> update(DutchAuction entity, SQLConnection transaction) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<List<DutchAuction>> getActiveAuctions() {

        final Future<List<DutchAuction>> future = Future.future();

        getAllEntities(SQL_GET_ACTIVE_AUCTIONS, DutchAuction.class).setHandler(handler -> {

            if (handler.succeeded()) {

                final Map<DutchAuction, Future<List<DutchAuctionTurn>>> futureMap = new HashMap<>();
                final List<Future> futures = new ArrayList<>();

                for (final DutchAuction auction : handler.result()) {

                    final Future<List<DutchAuctionTurn>> turnFuture = dutchAuctionTurnRepository.getAllByAuctionId(auction.getId());

                    futureMap.put(auction, turnFuture);
                    futures.add(turnFuture);

                }

                CompositeFuture.all(futures).setHandler(compositeHandler -> {
                   if (compositeHandler.succeeded()) {
                       for (final Map.Entry<DutchAuction, Future<List<DutchAuctionTurn>>> entry : futureMap.entrySet()) {
                           entry.getKey().setTurns(entry.getValue().result());
                       }
                       future.complete(new ArrayList<>(futureMap.keySet()));
                   } else {
                       future.fail(compositeHandler.cause());
                   }
                });

            } else {
                future.fail(handler.cause());
            }

        });

        return future;

    }

    @Override
    public Future<DutchAuction> changePhase(final DutchAuction auction, final AuctionPhase newPhase) {

        final Future<DutchAuction> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(newPhase)
                .add(auction.getId());

        executeQuery(SQL_UPDATE_PHASE, params, handler -> {
            if (handler.succeeded()) {
                auction.setAuctionPhase(newPhase);
                future.complete(auction);
            } else {
                future.fail(handler.cause());
            }
        });

        return future;

    }

    private Future<Void> saveDutchAuction(final SQLConnection transaction, final DutchAuction dutchAuction) {

        final Future<Void> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(dutchAuction.getId())
                .add(dutchAuction.getCargo().getId())
                .add(dutchAuction.getBasePrice())
                .add(dutchAuction.getPriceModifier());

        transaction.updateWithParams(SQL_INSERT_DUTCH_AUCTION, params, handler -> {
            if (handler.succeeded()) {
                future.complete();
            } else {
                LOG.error(String.format("Failed to persist dutch auction %s - %s", dutchAuction.toJson().encode(), handler.cause().getMessage()), handler.cause());
                future.fail(handler.cause());
            }
        });

        return future;

    }

}
