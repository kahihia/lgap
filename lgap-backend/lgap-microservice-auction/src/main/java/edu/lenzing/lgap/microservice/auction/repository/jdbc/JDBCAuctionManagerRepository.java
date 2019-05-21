package edu.lenzing.lgap.microservice.auction.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.auction.model.AuctionManager;
import edu.lenzing.lgap.microservice.auction.repository.AuctionManagerRepository;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@Singleton
public class JDBCAuctionManagerRepository extends BaseJDBCEntityRepository<AuctionManager> implements AuctionManagerRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCAuctionManagerRepository.class);

    private final static String SQL_GET_ALL = "SELECT * FROM `auction-manager`;";

    @Inject
    public JDBCAuctionManagerRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, AuctionManager.class);
    }

    @Override
    public Future<Optional<AuctionManager>> getById(final Long id) {
        return RepositoryException.failedFuture("Operation not supported");
    }

    @Override
    public Future<AuctionManager> save(final AuctionManager auctionManager) {
        return RepositoryException.failedFuture("Operation not supported");
    }

    @Override
    public Future<AuctionManager> save(final AuctionManager auctionManager, final SQLConnection transaction) {
        return RepositoryException.failedFuture("Operation not supported");
    }

    @Override
    public Future<AuctionManager> update(final AuctionManager auctionManager) {
        return RepositoryException.failedFuture("Operation not supported");
    }

    @Override
    public Future<AuctionManager> update(final AuctionManager auctionManager, final SQLConnection transaction) {
        return RepositoryException.failedFuture("Operation not supported");
    }

    @Override
    public Future<List<AuctionManager>> getAll() {
        return getAllEntities(SQL_GET_ALL, AuctionManager.class);
    }
}
