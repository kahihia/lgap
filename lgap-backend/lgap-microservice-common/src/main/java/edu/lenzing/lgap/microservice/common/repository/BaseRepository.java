package edu.lenzing.lgap.microservice.common.repository;

import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;

import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface BaseRepository<T extends AbstractModel> {

    Future<Optional<T>> getById(Long id);

    Future<T> save(T entity);
    Future<T> save(T entity, SQLConnection transaction);

    Future<T> update(T entity);
    Future<T> update(T entity, SQLConnection transaction);

}
