package edu.lenzing.lgap.microservice.auction.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.auction.model.Bid;
import edu.lenzing.lgap.microservice.auction.repository.BidRepository;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class JDBCBidRepository extends BaseJDBCEntityRepository<Bid> implements BidRepository {

    private static final String SQL_GET_BID_BY_ID = "SELECT * FROM `bid` WHERE `id` = ?;";
    private static final String SQL_INSERT_BID = "INSERT INTO `bid` (`auctionId`, `extCustomerId`, `value`, `timestamp`) VALUES (?, ?, ?, ?);";

    @Inject
    public JDBCBidRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, Bid.class);
    }

    @Override
    public Future<Optional<Bid>> getById(final Long id) {

        final Future<Optional<Bid>> future = Future.future();

        getEntityById(SQL_GET_BID_BY_ID, id, Bid.class).setHandler(handler -> {
            if (handler.succeeded()) {
                if (handler.result().isPresent()) {
                    future.complete(Optional.of(handler.result().get()));
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
    public Future<Bid> save(final Bid bid) {

        final Future<Bid> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(bid.getAuctionId())
                .add(bid.getExtCustomerId())
                .add(bid.getBid())
                .add(bid.getBidTime().getTime());

        saveEntity(SQL_INSERT_BID, params, bid).setHandler(future.completer());

        return future;

    }

    @Override
    public Future<Bid> save(final Bid entity, final SQLConnection transaction) {
        return null;
    }

    @Override
    public Future<Bid> update(final Bid entity) {
        return null;
    }

    @Override
    public Future<Bid> update(final Bid entity, final SQLConnection transaction) {
        return null;
    }
}
