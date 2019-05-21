package edu.lenzing.lgap.microservice.admin.guice;

import com.google.inject.Provides;
import edu.lenzing.lgap.microservice.admin.repository.AdminRepository;
import edu.lenzing.lgap.microservice.admin.repository.jdbc.JDBCAdminRepository;
import edu.lenzing.lgap.microservice.admin.service.AdminApiJWTAuthService;
import edu.lenzing.lgap.microservice.admin.service.AdminAuthenticationService;
import edu.lenzing.lgap.microservice.admin.service.impl.AdminApiJWTAuthServiceImpl;
import edu.lenzing.lgap.microservice.admin.service.impl.AdminAuthenticationServiceImpl;
import edu.lenzing.lgap.microservice.common.guice.BaseVertxGuiceModule;
import edu.lenzing.lgap.microservice.common.service.APIGatewayJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;

import javax.inject.Named;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AdminGuiceModule extends BaseVertxGuiceModule {

    public AdminGuiceModule(final Vertx vertx) {
        super(vertx);
    }

    @Override
    protected void configure() {

        // Repository
        bind(AdminRepository.class).to(JDBCAdminRepository.class);

        // Service
        bind(AdminAuthenticationService.class).to(AdminAuthenticationServiceImpl.class);
        bind(AdminApiJWTAuthService.class).to(AdminApiJWTAuthServiceImpl.class);

    }

    @Provides
    @Named("APIGatewayJWTAuthService")
    public ApiJWTAuthService apiGatewayAuthService() {
        return ProxyHelper.createProxy(ApiJWTAuthService.class, vertx, APIGatewayJWTAuthService.SERVICE_ADDRESS);
    }

}
