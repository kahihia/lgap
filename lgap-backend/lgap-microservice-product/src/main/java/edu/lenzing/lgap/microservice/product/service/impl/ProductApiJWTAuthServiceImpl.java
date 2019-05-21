package edu.lenzing.lgap.microservice.product.service.impl;

import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.impl.BaseApiJWTAuthServiceImpl;
import edu.lenzing.lgap.microservice.product.service.ProductApiJWTAuthService;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ProductApiJWTAuthServiceImpl extends BaseApiJWTAuthServiceImpl implements ProductApiJWTAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductApiJWTAuthServiceImpl.class);

    @Fluent
    @Override
    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {
        return super.generateToken(authInfo, handler);
    }

}
