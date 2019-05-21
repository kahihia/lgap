package edu.lenzing.lgap.microservice.auction.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.auction.model.Cargo;
import edu.lenzing.lgap.microservice.auction.repository.CargoRepository;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@Singleton
public class JDBCCargoRepository extends BaseJDBCEntityRepository<Cargo> implements CargoRepository {

    private static final String SQL_GET_CARGO_BY_ID = "SELECT * FROM `cargo` WHERE `id` = ?;";
    private static final String SQL_INSERT_CARGO = "INSERT INTO `cargo` (`extProductId`, `quantity`, `location`, `expectedDeliveryDays`) VALUES (?, ?, ?, ?);";


    @Inject
    public JDBCCargoRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, Cargo.class);
    }


    @Override
    public Future<Optional<Cargo>> getById(final Long id) {

        final Future<Optional<Cargo>> future = Future.future();

        getEntityById(SQL_GET_CARGO_BY_ID, id, Cargo.class).setHandler(handler -> {
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
    public Future<Cargo> save(final Cargo cargo) {

        final Future<Cargo> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(cargo.getExtProductId())
                .add(cargo.getQuantity())
                .add(cargo.getLocation());

        saveEntity(SQL_INSERT_CARGO, params, cargo).setHandler(future.completer());

        return future;

    }

    @Override
    public Future<Cargo> save(final Cargo cargo, final SQLConnection transaction) {

        final Future<Cargo> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(cargo.getExtProductId())
                .add(cargo.getQuantity())
                .add(cargo.getLocation())
                .add(cargo.getExpectedDeliveryDays());

        saveEntity(transaction, SQL_INSERT_CARGO, params, cargo).setHandler(future.completer());

        return future;

    }

    @Override
    public Future<Cargo> update(Cargo entity) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<Cargo> update(Cargo entity, SQLConnection transaction) {
        throw new RepositoryException("Not yet implement");
    }
}
