package edu.lenzing.lgap.microservice.customer.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.common.exception.AuthenticationException;
import edu.lenzing.lgap.microservice.common.model.UserRole;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.exception.ServiceException;
import edu.lenzing.lgap.microservice.customer.model.Customer;
import edu.lenzing.lgap.microservice.customer.service.CustomerAuthenticationService;
import edu.lenzing.lgap.microservice.customer.service.LenzingCustomerPortalService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import javax.inject.Named;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAuthenticationServiceImpl.class);

    @Inject
    private LenzingCustomerPortalService portalService;

    @Inject
    @Named("ApiGatewayJWTAuthService")
    private ApiJWTAuthService apiGatewayAuthService;

    @Override
    public CustomerAuthenticationService authenticate(final String email, final String password, final Handler<AsyncResult<String>> handler) {

        if (email == null || password == null) {

            handler.handle(AuthenticationException.failedFuture("No email address and/or password specified for authentication"));

        } else {

            portalService.signIn(email, password, signInHandler -> {

                if (signInHandler.failed()) {
                    LOG.error(String.format("Portal authentication failed: %s", signInHandler.cause().getMessage()));
                    handler.handle(AuthenticationException.failedFuture(signInHandler.cause()));
                } else {

                    final String portalUserId = signInHandler.result().getString("userId");
                    final String portalAuthToken = signInHandler.result().getString("userToken");

                    LOG.info(String.format("User [%s] authenticated successfully [%s]", portalUserId, portalAuthToken));

                    // Portal authentication successful, now fetch the user's regions
                    portalService.getUserRegions(portalUserId, portalAuthToken, regionHandler -> {

                        if (regionHandler.failed()) {
                            LOG.error(String.format("Failed to fetch user regions from Portal: %s", regionHandler.cause().getMessage()));
                            handler.handle(ServiceException.failedFuture(regionHandler.cause()));
                        } else {

                            final JsonArray regions = regionHandler.result().getJsonArray("regions");

                            LOG.info(String.format("User [%s] may access the following regions: %s", portalUserId, regions.encode()));

                            final Customer customer = new Customer();
                            customer.setPortalId(portalUserId);

                            final JsonObject apiGatewayAuthInfo = new JsonObject()
                                    .put("lgap.user.id", portalUserId)
                                    .put("lgap.user.roles", new JsonArray().add(UserRole.CUSTOMER))
                                    .put("lgap.user.regions", regions)
                                    .put("lgap.user", customer.toJson());

                            LOG.info(String.format("API auth. information bundle: %s", apiGatewayAuthInfo.encode()));

                            // User's regions acquired successfully, now generate an API Gateway token
                            apiGatewayAuthService.generateToken(apiGatewayAuthInfo, apiAuthHandler -> {

                                if (apiAuthHandler.failed()) {
                                    LOG.error(String.format("Failed to acquire API Gateway token: %s", apiAuthHandler.cause().getMessage()));
                                    handler.handle(ServiceException.failedFuture(apiAuthHandler.cause()));
                                } else {
                                    handler.handle(Future.succeededFuture(apiAuthHandler.result()));
                                }

                            }); // API auth. service, auth

                        }

                    }); // Portal service, get user regions

                }

            }); // Portal service, sign in

        }

        return this;
    }
}
