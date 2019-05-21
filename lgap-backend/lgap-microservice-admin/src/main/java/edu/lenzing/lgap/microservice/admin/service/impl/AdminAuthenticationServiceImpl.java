package edu.lenzing.lgap.microservice.admin.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.admin.model.Admin;
import edu.lenzing.lgap.microservice.admin.repository.AdminRepository;
import edu.lenzing.lgap.microservice.admin.service.AdminAuthenticationService;
import edu.lenzing.lgap.microservice.common.exception.AuthenticationException;
import edu.lenzing.lgap.microservice.common.model.UserRole;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.exception.ServiceException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.inject.Named;
import java.util.Objects;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AdminAuthenticationServiceImpl implements AdminAuthenticationService {

    @Inject
    private AdminRepository adminRepository;

    @Inject
    @Named("APIGatewayJWTAuthService")
    private ApiJWTAuthService apiJWTAuthService;

    @Override
    public AdminAuthenticationService authenticate(final String userName, final String password, final Handler<AsyncResult<String>> handler) {

        Objects.requireNonNull(userName);
        Objects.requireNonNull(password);

        adminRepository.getByUserNameAndPassword(userName, password).setHandler(repository -> {
            if (repository.succeeded()) {
                if (repository.result().isPresent()) {

                    final Admin admin = repository.result().get();

                    final JsonObject metadata = admin.toJson();
                    metadata.remove("password");

                    final JsonObject tokenAuthInfo = new JsonObject()
                            .put("lgap.user.id", admin.getId().toString())
                            .put("lgap.user.roles", new JsonArray().add(UserRole.ADMIN))
                            .put("lgap.user.regions", new JsonArray())
                            .put("lgap.user", metadata);

                    apiJWTAuthService.generateToken(tokenAuthInfo, token -> {
                        if (token.succeeded()) {
                            handler.handle(Future.succeededFuture(token.result()));
                        } else {
                            handler.handle(ServiceException.failedFuture(token.cause()));
                        }
                    });

                } else {
                    handler.handle(AuthenticationException.failedFuture("Incorrect user name and/or password."));
                }

            } else {
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }

        });

        return this;

    }
}
