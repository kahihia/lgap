package edu.lenzing.lgap.microservice.admin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.lenzing.lgap.microservice.admin.guice.AdminGuiceModule;
import edu.lenzing.lgap.microservice.admin.service.AdminApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.verticle.BaseMicroserviceVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AdminMicroserviceVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(AdminMicroserviceVerticle.class);

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        config().getJsonObject("api")
                .getJsonObject("metadata")
                .put("endpoint.auth.service.address", AdminApiJWTAuthService.SERVICE_ADDRESS);

        final Injector guice = Guice.createInjector(new AdminGuiceModule(vertx));

        // Register the API auth. service
        final AdminApiJWTAuthService apiAuthService = guice.getInstance(AdminApiJWTAuthService.class);
        registerServiceProxy(ApiJWTAuthService.class, vertx, apiAuthService, AdminApiJWTAuthService.SERVICE_ADDRESS);

        // Deploy the WEB Verticle
        final Future<String> webFuture = Future.future();
        final AdminWebVerticle webVerticle = guice.getInstance(AdminWebVerticle.class);
        vertx.deployVerticle(webVerticle, new DeploymentOptions().setConfig(config()), webFuture.completer());

        // Deploy the API Verticle
        final Future<String> apiFuture = Future.future();
        final AdminApiVerticle apiVerticle = guice.getInstance(AdminApiVerticle.class);
        vertx.deployVerticle(apiVerticle, new DeploymentOptions().setConfig(config()), apiFuture.completer());

        CompositeFuture.all(webFuture, apiFuture).setHandler(compositeHandler -> {
            if (compositeHandler.succeeded()) {
                future.complete();
            } else {
                future.fail(compositeHandler.cause());
            }
        });

    }

}
