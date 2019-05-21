package edu.lenzing.lgap.microservice.customer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.verticle.BaseMicroserviceVerticle;
import edu.lenzing.lgap.microservice.customer.guice.CustomerGuiceModule;
import edu.lenzing.lgap.microservice.customer.service.CustomerApiJWTAuthService;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class CustomerMicroserviceVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMicroserviceVerticle.class);

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        config().getJsonObject("api")
                .getJsonObject("metadata")
                .put("endpoint.auth.service.address", CustomerApiJWTAuthService.SERVICE_ADDRESS);

        final Injector guice = Guice.createInjector(new CustomerGuiceModule(vertx));

        // Register the API auth. service
        final CustomerApiJWTAuthService apiAuthService = guice.getInstance(CustomerApiJWTAuthService.class);
        registerServiceProxy(ApiJWTAuthService.class, vertx, apiAuthService, CustomerApiJWTAuthService.SERVICE_ADDRESS);

        // Deploy the WEB Verticle
        final Future<String> webFuture = Future.future();
        final CustomerWebVerticle webVerticle = guice.getInstance(CustomerWebVerticle.class);
        vertx.deployVerticle(webVerticle, new DeploymentOptions().setConfig(config()), webFuture.completer());

        // Deploy the API Verticle
        final Future<String> apiFuture = Future.future();
        final CustomerApiVerticle apiVerticle = guice.getInstance(CustomerApiVerticle.class);
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
