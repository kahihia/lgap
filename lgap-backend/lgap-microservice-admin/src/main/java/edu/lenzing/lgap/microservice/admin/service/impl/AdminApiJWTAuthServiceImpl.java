package edu.lenzing.lgap.microservice.admin.service.impl;

import edu.lenzing.lgap.microservice.admin.service.AdminApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.impl.BaseApiJWTAuthServiceImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AdminApiJWTAuthServiceImpl extends BaseApiJWTAuthServiceImpl implements AdminApiJWTAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminApiJWTAuthServiceImpl.class);

    @Fluent
    @Override
    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {
        return super.generateToken(authInfo, handler);
    }
}