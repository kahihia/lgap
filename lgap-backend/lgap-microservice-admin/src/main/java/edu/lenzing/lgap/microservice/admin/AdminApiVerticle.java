package edu.lenzing.lgap.microservice.admin;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.admin.service.AdminAuthenticationService;
import edu.lenzing.lgap.microservice.common.exception.AuthenticationException;
import edu.lenzing.lgap.microservice.common.verticle.BaseApiVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AdminApiVerticle extends BaseApiVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(AdminApiVerticle.class);

    @Inject
    private AdminAuthenticationService authenticationService;

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/login").handler(this::apiLoginHandler);

        final JsonObject apiConfig = config().getJsonObject("api");
        createHttpServer(router, apiConfig.getString("http.address"), apiConfig.getInteger("http.port"))
                .compose(serverCreated -> publishHttpEndpoint(apiConfig))
                .setHandler(future.completer());

    }

    /**
     * input: {
     *     userName: String,
     *     password: String
     * }
     * output: {
     *     authToken: String
     * }
     */
    private void apiLoginHandler(final RoutingContext routingContext) {

        try {

            final JsonObject authInfo = routingContext.getBodyAsJson();

            final String userName = authInfo.getString("username");
            final String password = authInfo.getString("password");

            if (userName == null || password == null) {

                respondWithBadRequest(routingContext, "User name or password is missing");

            } else {

                authenticationService.authenticate(userName, password, handler -> {
                    if (handler.succeeded()) {

                        respondWithSuccess(routingContext, new JsonObject().put("authToken", handler.result()));

                    } else {

                        LOG.error(String.format("Authentication attempt failed for user [%s]: %s", userName, handler.cause().getMessage()));

                        if (handler.cause() instanceof AuthenticationException) {
                            respondWithNotAuthenticated(routingContext);
                        } else {
                            respondWithInternalError(routingContext);
                        }

                    }
                });
            }

        } catch (final DecodeException ex) {
            LOG.error("Invalid user data received", ex);
            respondWithBadRequest(routingContext);
        }

    }

}
