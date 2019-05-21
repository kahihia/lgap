package edu.lenzing.lgap.microservice.customer.guice;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import edu.lenzing.lgap.microservice.common.guice.BaseVertxGuiceModule;
import edu.lenzing.lgap.microservice.common.service.APIGatewayJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.customer.service.CustomerApiJWTAuthService;
import edu.lenzing.lgap.microservice.customer.service.CustomerAuthenticationService;
import edu.lenzing.lgap.microservice.customer.service.LenzingCustomerPortalService;
import edu.lenzing.lgap.microservice.customer.service.impl.CustomerApiJWTAuthServiceImpl;
import edu.lenzing.lgap.microservice.customer.service.impl.CustomerAuthenticationServiceImpl;
import edu.lenzing.lgap.microservice.customer.service.impl.FakeCustomerPortalServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

import javax.inject.Named;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class CustomerGuiceModule extends BaseVertxGuiceModule {

    public CustomerGuiceModule(final Vertx vertx) {
        super(vertx);
    }

    @Override
    protected void configure() {

        // Service
        //bind(LenzingCustomerPortalService.class).to(LenzingCustomerPortalServiceImpl.class);
        bind(LenzingCustomerPortalService.class).to(FakeCustomerPortalServiceImpl.class);
        bind(CustomerAuthenticationService.class).to(CustomerAuthenticationServiceImpl.class);
        bind(CustomerApiJWTAuthService.class).to(CustomerApiJWTAuthServiceImpl.class);

    }

    @Provides
    @Singleton
    @Named("LenzingCustomerPortalConfig")
    public JsonObject lenzingCustomerPortalConfig() {
        return context.config().getJsonObject("lenzing.customer.portal").copy();
    }

    @Provides
    @Named("ApiGatewayJWTAuthService")
    public ApiJWTAuthService apiGatewayJWTAuthService() {
        return ProxyHelper.createProxy(ApiJWTAuthService.class, vertx, APIGatewayJWTAuthService.SERVICE_ADDRESS);
    }

}
