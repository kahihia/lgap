package edu.lenzing.lgap.microservice.admin;

import edu.lenzing.lgap.microservice.common.verticle.BaseHttpEndpointVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AdminWebVerticle extends BaseHttpEndpointVerticle {

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        final Router router = Router.router(vertx);

        // Enable CORS
        router.route().handler(CorsHandler.create("*")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedMethod(HttpMethod.GET)
        );

        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        router.route("/admin/*").handler(StaticHandler.create("webroot").setCachingEnabled(false));

        final JsonObject serverConfig = config().getJsonObject("web");

        createHttpServer(router, serverConfig.getString("http.address"), serverConfig.getInteger("http.port"))
                .compose(serverCreated -> publishHttpEndpoint(serverConfig))
                .setHandler(future.completer());

    }

}
