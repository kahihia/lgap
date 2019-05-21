package edu.lenzing.lgap.microservice.customer.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface CustomerAuthenticationService {

    String SERVICE_ADDRESS = "service-eb-customer-auth";

    @Fluent
    CustomerAuthenticationService authenticate(String email, String password, Handler<AsyncResult<String>> handler);

}
