package edu.lenzing.lgap.microservice.common.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.common.model.UserRole;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class BaseApiJWTAuthServiceImpl implements ApiJWTAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(BaseApiJWTAuthServiceImpl.class);

    private Long tokenExpirationTime;

    @Inject
    @Named("ApiJWTAuthProvider")
    private JWTAuth authProvider;

    public BaseApiJWTAuthServiceImpl() {
        tokenExpirationTime = 60L;
    }

    @Fluent
    @Override
    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {

        final List<String> permissions = authInfo.getJsonArray("lgap.user.roles").stream()
                .map(Object::toString)
                .map(UserRole::valueOf)
                .map(UserRole::getAuthority)
                .collect(Collectors.toList());

        final JsonObject claims = new JsonObject()
                .put("lgap.user.id", authInfo.remove("lgap.user.id"))
                .put("lgap.user.roles", authInfo.remove("lgap.user.roles"))
                .put("lgap.user.regions", authInfo.remove("lgap.user.regions"))
                .put("metadata", authInfo.copy());

        final JWTOptions options = new JWTOptions()
                .setPermissions(permissions)
                .setExpiresInSeconds(tokenExpirationTime);

        final String token = authProvider.generateToken(claims, options);

        if (token != null) {
            handler.handle(Future.succeededFuture(token));
        } else {
            handler.handle(Future.failedFuture("Failed to generate token"));
        }

        return this;
    }

    public Long getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(final Long tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
}
