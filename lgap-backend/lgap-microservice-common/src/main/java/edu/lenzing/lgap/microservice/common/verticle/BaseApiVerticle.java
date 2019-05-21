package edu.lenzing.lgap.microservice.common.verticle;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.common.model.UserRole;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class BaseApiVerticle extends BaseHttpEndpointVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(BaseApiVerticle.class);

    @Inject
    @Named("ApiJWTAuthProvider")
    private JWTAuth jwtAuthProvider;

    protected JWTAuth getJWTAuthProvider() {
        return this.jwtAuthProvider;
    }

    protected void jwtAuth(final RoutingContext routingContext, final Consumer<RoutingContext> action, final UserRole... roles) {

        final String authHeader = routingContext.request().headers().get("Authorization");

        // The Authorization header content is in the following format: "Bearer token"
        // Extract the token from the header by skipping the "Bearer" and trimming the whitespace
        final String token = authHeader.substring("Bearer".length()).trim();

        final JsonObject jwtOptions = new JsonObject()
                .put("jwt", token);

        jwtAuthProvider.authenticate(jwtOptions, authHandler -> {
            if (authHandler.succeeded()) {

                final User user = authHandler.result();

                final List<Future> roleFutures = new ArrayList<>();

                for (final UserRole role : roles) {
                    final Future<Boolean> authorisationFuture = Future.future();
                    user.isAuthorised(role.getAuthority(), authorisationFuture.completer());
                    roleFutures.add(authorisationFuture);
                }

                CompositeFuture.all(roleFutures).setHandler(compositeHandler -> {
                    if (compositeHandler.succeeded()) {
                        final boolean hasPermission = compositeHandler.result().list().stream().anyMatch(permission -> (boolean) permission);
                        if (hasPermission) {
                            routingContext.setUser(user);
                            action.accept(routingContext);
                        } else {
                            respondWithNotAuthorized(routingContext);
                        }
                    } else {
                        LOG.error("Failed to authorise user based on JWT token", compositeHandler.cause());
                        respondWithInternalError(routingContext);
                    }
                });

            } else {
                LOG.error("Failed to authenticate user based on JWT token", authHandler.cause());
                respondWithInternalError(routingContext);
            }
        });

    }

    protected void test(final RoutingContext routingContext) {
        routingContext.response().end(this.getClass().getName());
    }

}
