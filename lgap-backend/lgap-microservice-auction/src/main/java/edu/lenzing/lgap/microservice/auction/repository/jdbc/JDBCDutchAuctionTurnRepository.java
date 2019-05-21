package edu.lenzing.lgap.microservice.auction.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import edu.lenzing.lgap.microservice.auction.repository.DutchAuctionTurnRepository;
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
import io.vertx.ext.sql.UpdateResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@Singleton
public class JDBCDutchAuctionTurnRepository extends BaseJDBCEntityRepository<DutchAuctionTurn> implements DutchAuctionTurnRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCDutchAuctionTurnRepository.class);

    private static final String SQL_GET_ALL_TURNS_BY_AUCTION_ID = "SELECT * FROM `dutch-auction-turn` WHERE `dutchAuctionId` = ?;";
    private static final String SQL_INSERT_ALL_TURNS = "INSERT INTO `dutch-auction-turn` (`dutchAuctionId`, `turnNumber`, `turnDuration`, `started`, `startDateTimestamp`, `finished`, `endDateTimestamp`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_STARTED_STATUS = "UPDATE `dutch-auction-turn` SET `started` = ? WHERE `id` = ?;";
    private static final String SQL_UPDATE_FINISHED_STATUS = "UPDATE `dutch-auction-turn` SET `finished` = ? WHERE `id` = ?;";


    @Inject
    public JDBCDutchAuctionTurnRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, DutchAuctionTurn.class);
    }

    @Override
    public Future<Optional<DutchAuctionTurn>> getById(Long id) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<DutchAuctionTurn> save(DutchAuctionTurn entity) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<DutchAuctionTurn> save(DutchAuctionTurn entity, SQLConnection transaction) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<DutchAuctionTurn> update(DutchAuctionTurn entity) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<DutchAuctionTurn> update(DutchAuctionTurn entity, SQLConnection transaction) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<List<DutchAuctionTurn>> saveAll(final List<DutchAuctionTurn> turns, final SQLConnection transaction) {

        final Future<List<DutchAuctionTurn>> future = Future.future();

        final List<JsonArray> batchParams = new LinkedList<>();

        for (final DutchAuctionTurn turn : turns) {

            final JsonArray params = new JsonArray()
                    .add(turn.getAuction().getId())
                    .add(turn.getTurnNumber())
                    .add(turn.getTurnDuration())
                    .add(turn.getStarted())
                    .add(turn.getStartTimestamp())
                    .add(turn.getFinished())
                    .add(turn.getEndTimestamp());

            batchParams.add(params);

        }

        transaction.batchWithParams(SQL_INSERT_ALL_TURNS, batchParams, handler -> {

            if (handler.succeeded()) {
                // TODO: figure out and set the turn IDs
                future.complete(turns);
            } else {
                LOG.error("Failed to persist dutch auction turns", handler.cause());
                future.fail(handler.cause());
            }

        });

        return future;

    }

    @Override
    public Future<List<DutchAuctionTurn>> getAllByAuctionId(final Long id) {

        final Future<List<DutchAuctionTurn>> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(id);

        getAllEntities(SQL_GET_ALL_TURNS_BY_AUCTION_ID, params, DutchAuctionTurn.class).setHandler(handler -> {
            if (handler.succeeded()) {
                future.complete(handler.result());
            } else {
                future.fail(handler.cause());
            }
        });

        return future;

    }

    @Override
    public Future<DutchAuctionTurn> markAsStarted(final DutchAuctionTurn turn) {

        final Future<DutchAuctionTurn> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(turn.getStarted())
                .add(turn.getId());

        executeQuery(SQL_UPDATE_STARTED_STATUS, params, handler -> {
            if (handler.succeeded()) {
                future.complete(turn);
            } else {
                future.fail(handler.cause());
            }
        });

        return future;

    }

    @Override
    public Future<DutchAuctionTurn> markAsFinished(final DutchAuctionTurn turn) {

        final Future<DutchAuctionTurn> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(turn.getFinished())
                .add(turn.getId());

        executeQuery(SQL_UPDATE_FINISHED_STATUS, params, handler -> {
            if (handler.succeeded()) {
                future.complete(turn);
            } else {
                future.fail(handler.cause());
            }
        });

        return future;

    }

    @Override
    public Future<List<DutchAuctionTurn>> markAllAsFinished(final List<DutchAuctionTurn> turns) {

        final Future<List<DutchAuctionTurn>> future = Future.future();

        final List<JsonArray> params = new LinkedList<>();
        for (final DutchAuctionTurn turn : turns) {
            turn.setFinished(true);
            params.add(new JsonArray()
                    .add(turn.getFinished())
                    .add(turn.getId())
            );
        }

        executeBatch(SQL_UPDATE_FINISHED_STATUS, params, handler -> {
            if (handler.succeeded()) {
                future.complete(turns);
            } else {
                future.fail(handler.cause());
            }
        });

        return future;

    }

    @Override
    public Future<Void> rollNextTurn(final DutchAuctionTurn currentTurn, final DutchAuctionTurn nextTurn) {

        final Future<Void> future = Future.future();

        startTransaction(transactionHandler -> {

            if (transactionHandler.failed()) {
                future.fail(transactionHandler.cause());
            } else {

                final SQLConnection transaction = transactionHandler.result();

                final JsonArray currentTurnParams = new JsonArray()
                        .add(true)
                        .add(currentTurn.getId());

                final Future<UpdateResult> currentTurnFuture = Future.future();
                executeQuery(SQL_UPDATE_FINISHED_STATUS, currentTurnParams, currentTurnFuture.completer());

                final JsonArray nextTurnParams = new JsonArray()
                        .add(true)
                        .add(nextTurn.getId());

                final Future<UpdateResult> nextTurnFuture = Future.future();
                executeQuery(SQL_UPDATE_STARTED_STATUS, nextTurnParams, nextTurnFuture.completer());

                CompositeFuture.all(currentTurnFuture, nextTurnFuture).setHandler(compositeHandler ->{

                    if (compositeHandler.failed()) {
                        rollbackTransaction(transaction);
                        future.fail(compositeHandler.cause());
                    } else {
                        commitTransaction(transaction).setHandler(future.completer());
                    }

                });

            }

        });

        return future;

    }
}
