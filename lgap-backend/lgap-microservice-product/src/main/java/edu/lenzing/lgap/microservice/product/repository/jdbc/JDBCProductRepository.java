package edu.lenzing.lgap.microservice.product.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import edu.lenzing.lgap.microservice.product.model.Product;
import edu.lenzing.lgap.microservice.product.repository.ProductRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.List;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.com>
 */
@Singleton
public class JDBCProductRepository extends BaseJDBCEntityRepository<Product> implements ProductRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCProductRepository.class);

    private static final String SQL_GET_BY_ID      = "SELECT * FROM `product` WHERE id = ?;";
    private static final String SQL_GET_ALL        = "SELECT * FROM `product`;";
    private static final String SQL_SAVE_PRODUCT   = "INSERT INTO `product` (`name`, `description`, `imageUrl`) VALUES (?, ?, ?);";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE `product` SET `name`=?, `description`=?, `imageUrl`=? WHERE `id`=?;";

    @Inject
    public JDBCProductRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, Product.class);
    }

    @Override
    public Future<Optional<Product>> getById(final Long id) {
        return getEntityById(SQL_GET_BY_ID, id, Product.class);
    }

    @Override
    public Future<List<Product>> getAll() {
        return getAllEntities(SQL_GET_ALL, Product.class);
    }

    @Override
    public Future<Product> save(final Product product) {

        final Future<Product> future = Future.future();

        final JsonArray queryParams = new JsonArray()
                .add(product.getName())
                .add(product.getDescription())
                .add(product.getImageUrl());

        executeQuery(SQL_SAVE_PRODUCT, queryParams, this.saveResultHandler(future, product));

        return future;

    }

    @Override
    public Future<Product> save(final Product product, final SQLConnection transaction) {

        final Future<Product> future = Future.future();

        final JsonArray queryParams = new JsonArray()
                .add(product.getName())
                .add(product.getDescription())
                .add(product.getImageUrl());

        executeQuery(SQL_SAVE_PRODUCT, queryParams, transaction, this.saveResultHandler(future, product));

        return future;

    }

    @Override
    public Future<Product> update(final Product product) {

        final Future<Product> future = Future.future();

        final JsonArray queryParams = new JsonArray()
                .add(product.getName())
                .add(product.getDescription())
                .add(product.getImageUrl())
                .add(product.getId());

        executeQuery(SQL_UPDATE_PRODUCT, queryParams, this.updateResultHandler(future, product));

        return future;

    }

    @Override
    public Future<Product> update(final Product product, final SQLConnection transaction) {

        final Future<Product> future = Future.future();

        final JsonArray queryParams = new JsonArray()
                .add(product.getName())
                .add(product.getDescription())
                .add(product.getImageUrl())
                .add(product.getId());

        executeQuery(SQL_UPDATE_PRODUCT, queryParams, transaction, this.updateResultHandler(future, product));

        return future;

    }

    private Handler<AsyncResult<UpdateResult>> saveResultHandler(final Future<Product> future, final Product product) {

        return resultHandler -> {
            final UpdateResult result = resultHandler.result();
            if (resultHandler.succeeded()) {
                final long id = result.getKeys().getLong(0);
                product.setId(id);
                future.complete(product);
            } else {
                final Exception ex = new RepositoryException(String.format("Failed to save product with id: [%d]", product.getId()), resultHandler.cause());
                LOG.error(ex);
                future.fail(ex);
            }
        };

    }

    private Handler<AsyncResult<UpdateResult>> updateResultHandler(final Future<Product> future, final Product product) {

        return resultHandler -> {
            if (resultHandler.succeeded()) {
                future.complete(product);
            } else {
                final Exception ex = new RepositoryException(String.format("Failed to update product with id: [%d]", product.getId()), resultHandler.cause());
                LOG.error(ex);
                future.fail(ex);
            }
        };

    }

}
