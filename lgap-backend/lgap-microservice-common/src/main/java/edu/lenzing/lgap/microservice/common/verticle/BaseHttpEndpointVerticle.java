package edu.lenzing.lgap.microservice.common.verticle;

import edu.lenzing.lgap.microservice.common.util.JsonUtil;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class BaseHttpEndpointVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(BaseHttpEndpointVerticle.class);

    protected Future<HttpServer> createHttpServer(final Router router, final String host, final int port) {

        final Future<HttpServer> httpServerFuture = Future.future();

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(port, host, httpServerFuture.completer());

        return httpServerFuture;

    }

    protected void respondWithSuccess(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(200)
                .end();
    }

    protected void respondWithSuccess(final RoutingContext routingContext, final JsonObject payload) {
        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json")
                .end(payload.encode());
    }

    protected void respondWithSuccess(final RoutingContext routingContext, final JsonArray payload) {
        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json")
                .end(payload.encode());
    }

    protected void respondWithBadRequest(final RoutingContext routingContext) {
        this.respondWithBadRequest(routingContext, "Invalid request");
    }

    protected void respondWithBadRequest(final RoutingContext routingContext, final Throwable cause) {
        this.respondWithBadRequest(routingContext, cause.getMessage());
    }

    protected void respondWithBadRequest(final RoutingContext routingContext, final String message) {
        routingContext.response()
                .setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", message)
                        .encodePrettily());
    }

    protected void respondWithNotAuthenticated(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(401)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", "Not authenticated")
                        .encodePrettily());
    }

    protected void respondWithNotAuthorized(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(403)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", "Not authorized")
                        .encodePrettily());
    }

    protected void respondWithNotFound(final RoutingContext routingContext) {
        this.respondWithNotFound(routingContext, "Resource not found");
    }

    protected void respondWithNotFound(final RoutingContext routingContext, final String message) {
        routingContext.response()
                .setStatusCode(404)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", message)
                        .encodePrettily());
    }

    protected void respondWithInternalError(final RoutingContext routingContext) {
        internalServerError(routingContext, new JsonObject().put("message", "Internal Server Error"));
    }

    protected void respondWithInternalError(final RoutingContext routingContext, final Throwable cause) {
        internalServerError(routingContext, new JsonObject().put("message", JsonUtil.exceptionStackTraceToString(cause)));
    }

    protected void respondWithBadGateway(final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(502)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", "Bad gateway")
                        .encodePrettily());
    }

    private void internalServerError(final RoutingContext routingContext, final JsonObject payload) {
        routingContext.response()
                .setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(payload.encodePrettily());
    }

}
