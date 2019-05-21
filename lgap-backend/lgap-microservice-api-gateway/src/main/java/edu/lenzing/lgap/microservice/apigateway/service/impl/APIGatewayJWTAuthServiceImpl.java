package edu.lenzing.lgap.microservice.apigateway.service.impl;

import edu.lenzing.lgap.microservice.common.service.APIGatewayJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.impl.BaseApiJWTAuthServiceImpl;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class APIGatewayJWTAuthServiceImpl extends BaseApiJWTAuthServiceImpl implements APIGatewayJWTAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(APIGatewayJWTAuthServiceImpl.class);

    public APIGatewayJWTAuthServiceImpl() {
        setTokenExpirationTime(null);
    }

    @Override
    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {
        return super.generateToken(authInfo, handler);
    }

//    @Override
//    public ApiJWTAuthService generateToken(final JsonObject authInfo, final Handler<AsyncResult<String>> handler) {
//
//        LOG.info(authInfo.encodePrettily());
//
//        Objects.requireNonNull(authInfo.getString("lgap.user.id"), "Can not generate auth. token without user id");
//        Objects.requireNonNull(authInfo.getJsonArray("lgap.user.roles"), "Can not generate auth. token without user roles");
//        Objects.requireNonNull(authInfo.getJsonArray("lgap.user.regions"), "Can not generate auth. token without user regions");
//
//        final List<String> permissions = authInfo.getJsonArray("lgap.user.roles").stream()
//                .map(Object::toString)
//                .map(UserRole::valueOf)
//                .map(UserRole::getAuthority)
//                .collect(Collectors.toList());
//
//        final JsonObject claims = new JsonObject()
//                .put("lgap.user.id", authInfo.remove("lgap.user.id"))
//                .put("lgap.user.roles", authInfo.remove("lgap.user.roles"))
//                .put("lgap.user.regions", authInfo.remove("lgap.user.regions"));
//
//        final JWTOptions options = new JWTOptions()
//                .setPermissions(permissions);
//
//        LOG.info(claims.encodePrettily());
//        LOG.info(options.toJson().encodePrettily());
//
//        final String token = authProvider.generateToken(claims, options);
//
//        if (token != null) {
//            LOG.info(String.format("User %s acquired token: %s", claims.encode(), token));
//            handler.handle(Future.succeededFuture(token));
//        } else {
//            handler.handle(Future.failedFuture("Failed to generate token"));
//        }
//
//        return this;
//    }
}
