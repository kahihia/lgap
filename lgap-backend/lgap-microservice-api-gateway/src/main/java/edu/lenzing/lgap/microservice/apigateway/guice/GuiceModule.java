package edu.lenzing.lgap.microservice.apigateway.guice;

import edu.lenzing.lgap.microservice.apigateway.service.impl.APIGatewayJWTAuthServiceImpl;
import edu.lenzing.lgap.microservice.common.guice.BaseVertxGuiceModule;
import edu.lenzing.lgap.microservice.common.service.APIGatewayJWTAuthService;
import io.vertx.core.Vertx;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class GuiceModule extends BaseVertxGuiceModule {

    public GuiceModule(final Vertx vertx) {
        super(vertx);
    }

    @Override
    protected void configure() {

        // Service
        bind(APIGatewayJWTAuthService.class).to(APIGatewayJWTAuthServiceImpl.class);

    }

}
