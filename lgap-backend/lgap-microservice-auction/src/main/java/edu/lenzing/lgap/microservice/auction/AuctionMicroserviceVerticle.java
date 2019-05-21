package edu.lenzing.lgap.microservice.auction;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.lenzing.lgap.microservice.auction.guice.AuctionGuiceModule;
import edu.lenzing.lgap.microservice.auction.model.AuctionManager;
import edu.lenzing.lgap.microservice.auction.service.AuctionApiJWTAuthService;
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
public class AuctionMicroserviceVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionMicroserviceVerticle.class);

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        // Configure the API auth. service address
        config().getJsonObject("api")
                .getJsonObject("metadata")
                .put("endpoint.auth.service.address", AuctionApiJWTAuthService.SERVICE_ADDRESS);

        final AuctionGuiceModule guiceModule = new AuctionGuiceModule(vertx);
        final Injector guice = Guice.createInjector(guiceModule);

        // Register the API auth. service
        final AuctionApiJWTAuthService productApiAuthService = guice.getInstance(AuctionApiJWTAuthService.class);
        registerServiceProxy(ApiJWTAuthService.class, vertx, productApiAuthService, AuctionApiJWTAuthService.SERVICE_ADDRESS);

        // Deploy verticles
        final Future<String> apiFuture = Future.future();
        final AuctionApiVerticle apiVerticle = guice.getInstance(AuctionApiVerticle.class);
        vertx.deployVerticle(apiVerticle, new DeploymentOptions().setConfig(config()), apiFuture.completer());

        final Future<String> managerFuture = Future.future();
        final AuctionManagerVerticle managerVerticle = guice.getInstance(AuctionManagerVerticle.class);
        vertx.deployVerticle(managerVerticle, new DeploymentOptions().setConfig(config()), managerFuture.completer());

        CompositeFuture.all(apiFuture, managerFuture).setHandler(handler -> {
            if (handler.succeeded()) {
                future.complete();
            } else {
                LOG.error(String.format("Failed to start the auction microservice: %s", handler.cause().getMessage()), handler.cause());
                future.fail(handler.cause());
            }
        });

    }

}
