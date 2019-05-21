package edu.lenzing.lgap.microservice.common.repository;

import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class BaseJDBCEntityRepository<E extends AbstractModel> extends BaseJDBCRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BaseJDBCEntityRepository.class);

    private final Class<E> entityClass;


    public BaseJDBCEntityRepository(Vertx vertx, JsonObject config, Class<E> entityClass) {
        super(vertx, config);

        this.entityClass = entityClass;

    }

    protected Future<E> saveEntity(final String sql, final JsonArray params, final E entity) {

        final Future<E> future = Future.future();

        executeQuery(sql, params, saveEntityHandler(future, entity));

        return future;

    }

    protected Future<E> saveEntity(final SQLConnection transaction, final String sql, final JsonArray params, final E entity) {

        final Future<E> future = Future.future();

        transaction.updateWithParams(sql, params, saveEntityHandler(future, entity));

        return future;

    }

    protected Future<Optional<E>> getEntityById(final String sql, final Long id, final Class<E> entityClass) {
        return this.getEntityByAttribute(sql, id, entityClass);
    }

    protected Future<Optional<E>> getEntityByAttribute(final String sql, final Object attribute, final Class<E> entityClass) {

        final Future<Optional<E>> future = Future.future();

        final JsonArray params = new JsonArray()
                .add(attribute);

        retrieveOne(sql, params, resultHandler -> {

            if (resultHandler.succeeded()) {

                final Optional<JsonObject> result = resultHandler.result();

                if (result.isPresent()) {

                    final JsonObject json = result.get();

                    try {
                        final E instance = entityClass.getConstructor(JsonObject.class).newInstance(json);
                        future.complete(Optional.of(instance));
                    } catch (final ReflectiveOperationException ex) {
                        future.fail(new RepositoryException(ex));
                    }

                } else {
                    future.complete(Optional.empty());
                }
            }
        });

        return future;

    }

    protected Future<List<E>> getAllEntities(final String sql, final Class<E> entityClass) {

        final Future<List<E>> future = Future.future();

        retrieveAll(sql, this.getAllEntitiesResultHandler(future, entityClass));

        return future;

    }

    protected Future<List<E>> getAllEntities(final String sql, final JsonArray params, final Class<E> entityClass) {

        final Future<List<E>> future = Future.future();

        retrieveAll(sql, params, this.getAllEntitiesResultHandler(future, entityClass));

        return future;

    }

    private Handler<AsyncResult<UpdateResult>> saveEntityHandler(final Future<E> future, final E entity) {

        return handler -> {

            if (handler.succeeded()) {

                final Long id = handler.result().getKeys().getLong(0);
                entity.setId(id);

                future.complete(entity);

            } else {

                final String message = String.format("Failed to persist entity [%s]: %s", entityClass.getName(), handler.cause().getMessage());

                LOG.error(message, handler.cause());
                future.fail(new RepositoryException(message, handler.cause()));

            }

        };

    }

    private Handler<AsyncResult<List<JsonObject>>> getAllEntitiesResultHandler(final Future<List<E>> future, final Class<E> entityClass) {

        return resultHandler -> {

            if (resultHandler.succeeded()) {

                final List<E> list = new ArrayList<>();

                try {

                    for (final JsonObject json : resultHandler.result()) {
                        final E instance = entityClass.getConstructor(JsonObject.class).newInstance(json);
                        list.add(instance);
                    }

                    future.complete(list);

                } catch (final ReflectiveOperationException ex) {
                    future.fail(new RepositoryException(ex));
                }

            } else {
                future.fail(resultHandler.cause());
            }

        };

    }

}
