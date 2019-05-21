package edu.lenzing.lgap.microservice.customer.service.impl;

import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.impl.BaseApiJWTAuthServiceImpl;
import edu.lenzing.lgap.microservice.customer.service.CustomerApiJWTAuthService;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class CustomerApiJWTAuthServiceImpl extends BaseApiJWTAuthServiceImpl implements CustomerApiJWTAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerApiJWTAuthServiceImpl.class);

    @Fluent
    @Override
    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {
        return super.generateToken(authInfo, handler);
    }
}
