package edu.lenzing.lgap.microservice.auction.service.impl;

import edu.lenzing.lgap.microservice.auction.service.AuctionApiJWTAuthService;
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
public class AuctionApiJWTAuthServiceImpl extends BaseApiJWTAuthServiceImpl implements AuctionApiJWTAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionApiJWTAuthServiceImpl.class);

    @Fluent
    @Override
    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {
        return super.generateToken(authInfo, handler);
    }

}
