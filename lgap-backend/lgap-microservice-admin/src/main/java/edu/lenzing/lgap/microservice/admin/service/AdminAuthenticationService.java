package edu.lenzing.lgap.microservice.admin.service;

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
public interface AdminAuthenticationService {

    String SERVICE_ADDRESS = "service-eb-admin-auth";

    @Fluent
    AdminAuthenticationService authenticate(String userName, String password, Handler<AsyncResult<String>> handler);

}
