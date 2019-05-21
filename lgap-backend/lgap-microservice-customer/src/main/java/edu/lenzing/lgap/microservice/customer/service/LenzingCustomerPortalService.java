package edu.lenzing.lgap.microservice.customer.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface LenzingCustomerPortalService {

    @Fluent
    LenzingCustomerPortalService signIn(String email, String password, Handler<AsyncResult<JsonObject>> handler);

    @Fluent
    LenzingCustomerPortalService getUserRegions(String portalUserId, String portalAuthToken, Handler<AsyncResult<JsonObject>> handler);

}
