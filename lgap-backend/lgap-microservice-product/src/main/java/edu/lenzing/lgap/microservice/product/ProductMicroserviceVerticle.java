package edu.lenzing.lgap.microservice.product;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.verticle.BaseMicroserviceVerticle;
import edu.lenzing.lgap.microservice.product.service.ProductApiJWTAuthService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import edu.lenzing.lgap.microservice.product.guice.ProductGuiceModule;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ProductMicroserviceVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMicroserviceVerticle.class);

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        // Configure the API auth. service address
        config().getJsonObject("api")
                .getJsonObject("metadata")
                .put("endpoint.auth.service.address", ProductApiJWTAuthService.SERVICE_ADDRESS);

        final Injector guice = Guice.createInjector(new ProductGuiceModule(vertx));

        // Register the API auth. service
        final ProductApiJWTAuthService productApiAuthService = guice.getInstance(ProductApiJWTAuthService.class);
        registerServiceProxy(ApiJWTAuthService.class, vertx, productApiAuthService, ProductApiJWTAuthService.SERVICE_ADDRESS);

        final ProductApiVerticle productApi = guice.getInstance(ProductApiVerticle.class);

        final Future deploymentFuture = Future.future();
        vertx.deployVerticle(productApi, new DeploymentOptions().setConfig(config()), deploymentFuture.completer());
        deploymentFuture.setHandler(future.completer());

    }

}
