package edu.lenzing.lgap.microservice.common.repository;

import edu.lenzing.lgap.microservice.common.repository.exception.TransactionException;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.*;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class BaseJDBCRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BaseJDBCRepository.class);

    protected final JDBCClient jdbc;
    protected final JsonObject config;

    public BaseJDBCRepository(final Vertx vertx, final JsonObject config) {
        this.jdbc = JDBCClient.createShared(vertx, config.getJsonObject("data.source", config));
        this.config = config;
    }

    protected void retrieveOne(final String sql, final JsonArray queryParams, final Handler<AsyncResult<Optional<JsonObject>>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {
                final SQLConnection connection = connectionHandler.result();
                connection.queryWithParams(sql, queryParams, queryHandler -> {
                    if (queryHandler.succeeded()) {
                        final List<JsonObject> results = queryHandler.result().getRows();
                        if (results.isEmpty()) {
                            resultHandler.handle(Future.succeededFuture(Optional.empty()));
                        } else {
                            final JsonObject result = results.get(0);
                            resultHandler.handle(Future.succeededFuture(Optional.of(result)));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(queryHandler.cause()));
                    }
                    connection.close();
                });
            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }
        });

    }

    protected void retrieveAll(final String sql, final Handler<AsyncResult<List<JsonObject>>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {
                final SQLConnection connection = connectionHandler.result();
                connection.query(sql, queryHandler -> {
                    if (queryHandler.succeeded()) {
                        final List<JsonObject> results = queryHandler.result().getRows();
                        if (results.isEmpty()) {
                            resultHandler.handle(Future.succeededFuture(Collections.emptyList()));
                        } else {
                            resultHandler.handle(Future.succeededFuture(results));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(queryHandler.cause()));
                    }
                    connection.close();
                });
            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }
        });

    }

    protected void retrieveAll(final String sql, final JsonArray queryParams, final Handler<AsyncResult<List<JsonObject>>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {
                final SQLConnection connection = connectionHandler.result();
                connection.queryWithParams(sql, queryParams, queryHandler -> {
                    if (queryHandler.succeeded()) {
                        final List<JsonObject> results = queryHandler.result().getRows();
                        if (results.isEmpty()) {
                            resultHandler.handle(Future.succeededFuture(Collections.emptyList()));
                        } else {
                            resultHandler.handle(Future.succeededFuture(results));
                        }
                    } else {
                        resultHandler.handle(Future.failedFuture(queryHandler.cause()));
                    }
                    connection.close();
                });
            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }
        });

    }

    protected void executeQuery(final String sql, final Handler<AsyncResult<Void>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {
                final SQLConnection connection = connectionHandler.result();
                connection.execute(sql, queryHandler -> {
                    resultHandler.handle(queryHandler);
                    connection.close();
                });
            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }
        });

    }

    protected void executeQuery(final String sql, final JsonArray queryParams, final Handler<AsyncResult<UpdateResult>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {
                final SQLConnection connection = connectionHandler.result();
                this.executeQuery(sql, queryParams, connection, queryHandler -> {
                    resultHandler.handle(queryHandler);
                    connection.close();
                });
            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }
        });

    }

    protected void executeQuery(final String sql, final JsonArray queryParams, final SQLConnection transaction, final Handler<AsyncResult<UpdateResult>> resultHandler) {
        transaction.updateWithParams(sql, queryParams, queryHandler -> {
            if (queryHandler.succeeded()) {
                resultHandler.handle(Future.succeededFuture(queryHandler.result()));
            } else {
                resultHandler.handle(Future.failedFuture(queryHandler.cause()));
            }
        });
    }

    protected void executeBatch(final String sql, final List<JsonArray> queryParams, final Handler<AsyncResult<List<Integer>>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {
                final SQLConnection connection = connectionHandler.result();
                this.executeBatch(sql, queryParams, connection, queryHandler -> {
                    resultHandler.handle(queryHandler);
                    connection.close();
                });
            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }
        });

    }

    protected void executeBatch(final String sql, final List<JsonArray> queryParams, final SQLConnection transaction, final Handler<AsyncResult<List<Integer>>> resultHandler) {
        transaction.batchWithParams(sql, queryParams, queryHandler -> {
            if (queryHandler.succeeded()) {
                resultHandler.handle(Future.succeededFuture(queryHandler.result()));
            } else {
                resultHandler.handle(Future.failedFuture(queryHandler.cause()));
            }
        });
    }

    protected void startTransaction(final Handler<AsyncResult<SQLConnection>> resultHandler) {

        jdbc.getConnection(connectionHandler -> {
            if (connectionHandler.succeeded()) {

                final SQLConnection connection = connectionHandler.result();

                connection.setAutoCommit(false, transactionHandler -> {
                    if (transactionHandler.succeeded()) {
                        resultHandler.handle(Future.succeededFuture(connection));
                    } else {
                        connection.close();
                        resultHandler.handle(Future.failedFuture(transactionHandler.cause()));
                    }
                });

            } else {
                resultHandler.handle(Future.failedFuture(connectionHandler.cause()));
            }

        });

    }

    protected Future<Void> commitTransaction(final SQLConnection transaction) {

        final Future<Void> future = Future.future();

        transaction.commit(commitHandler -> {
            if (commitHandler.succeeded()) {
                future.complete();
            } else {
                future.fail(commitHandler.cause());
            }
            transaction.close();
        });

        return future;

    }

    protected Future<Void> rollbackTransaction(final SQLConnection transaction) {

        final Future<Void> future = Future.future();

        transaction.rollback(rollbackHandler -> {
            if (rollbackHandler.succeeded()) {
                future.complete();
            } else {
                future.fail(rollbackHandler.cause());
            }
            transaction.close();
        });

        return future;

    }

    protected <T> Handler<AsyncResult<T>> transactionCompletionHandler(final SQLConnection transaction, final Future<T> future) {

        return handler -> {

            if (handler.failed()) {

                final RepositoryException exception = new RepositoryException("Transaction failed", handler.cause());
                LOG.error(exception);
                future.fail(exception);

                rollbackTransaction(transaction);

            } else {

                commitTransaction(transaction).setHandler(commitHandler -> {

                    if (commitHandler.failed()) {
                        final TransactionException exception = new TransactionException("Failed to commit transaction", commitHandler.cause());
                        LOG.error(exception);
                        future.fail(exception);
                    } else {
                        future.complete(handler.result());
                    }

                });

            }

        };

    }

}
