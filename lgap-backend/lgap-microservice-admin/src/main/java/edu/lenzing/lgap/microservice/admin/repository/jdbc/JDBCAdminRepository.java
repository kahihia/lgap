package edu.lenzing.lgap.microservice.admin.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.admin.model.Admin;
import edu.lenzing.lgap.microservice.admin.repository.AdminRepository;
import edu.lenzing.lgap.microservice.admin.util.PasswordEncryptor;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@Singleton
public class JDBCAdminRepository extends BaseJDBCEntityRepository<Admin> implements AdminRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCAdminRepository.class);

    private static final String SQL_GET_BY_ID = "SELECT * FROM `admin` WHERE id = ?;";
    private static final String SQL_GET_BY_USER_NAME = "SELECT * FROM `admin` WHERE `userName` = ?;";


    @Inject
    public JDBCAdminRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, Admin.class);
    }


    @Override
    public Future<Optional<Admin>> getByUserNameAndPassword(final String userName, final String password) {

        final Future<Optional<Admin>> future = Future.future();

        getEntityByAttribute(SQL_GET_BY_USER_NAME, userName, Admin.class).setHandler(resultHandler -> {

            if (resultHandler.succeeded()) {

                if (resultHandler.result().isPresent()) {

                    final Admin admin = resultHandler.result().get();
                    final JsonObject encryptionOptions = config.getJsonObject("encryption", new JsonObject());
                    final String hashedPassword = PasswordEncryptor.generateHashedPassword(password, encryptionOptions);

                    if (Objects.equals(hashedPassword, admin.getPassword())) {
                        future.complete(Optional.of(admin));
                    } else {
                        // Incorrect password
                        future.complete(Optional.empty());
                    }

                } else {
                    // Incorrect user name
                    future.complete(Optional.empty());
                }

            } else {
                final String message = String.format("failed to fetch admin user with userName: [%s]", userName);
                LOG.error(message, resultHandler.cause());
                future.fail(new RepositoryException(message, resultHandler.cause()));
            }
        });

        return future;

    }

    @Override
    public Future<Optional<Admin>> getById(final Long id) {
        return getEntityById(SQL_GET_BY_ID, id, Admin.class);
    }

    @Override
    public Future<Admin> save(final Admin admin) {
        return null;
    }

    @Override
    public Future<Admin> save(final Admin admin, final SQLConnection transaction) {
        return null;
    }

    @Override
    public Future<Admin> update(final Admin admin) {
        return null;
    }

    @Override
    public Future<Admin> update(final Admin admin, final SQLConnection transaction) {
        return null;
    }

}
