package edu.lenzing.lgap.microservice.customer.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import edu.lenzing.lgap.microservice.customer.service.LenzingCustomerPortalService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

import javax.inject.Named;
import java.util.Objects;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class LenzingCustomerPortalServiceImpl implements LenzingCustomerPortalService {

    private static final Logger LOG = LoggerFactory.getLogger(LenzingCustomerPortalServiceImpl.class);

    @Inject
    private Vertx vertx;

    @Inject
    @Named("LenzingCustomerPortalConfig")
    private JsonObject portalConfig;

    @Override
    public LenzingCustomerPortalService signIn(final String email, final String password, final Handler<AsyncResult<JsonObject>> handler) {

        Objects.requireNonNull(email, "Email address must be provided to perform sign-in with the portal");
        Objects.requireNonNull(password, "Password must be provided to perform sign-in with the portal");

        final JsonObject payload = new JsonObject()
                .put("user", new JsonObject()
                        .put("email", email)
                        .put("password", password)
                );

        LOG.info(String.format("Remote authentication request: %s", payload.encode()));

        final Integer portalPort = portalConfig.getInteger("http.port");
        final String portalHost = portalConfig.getString("http.host");
        final String portalSignInUri = portalConfig.getString("request.uri.signIn");

        final WebClient client = WebClient.create(vertx);

        client.post(portalPort, portalHost, portalSignInUri)
                .as(BodyCodec.jsonObject())
                .putHeader("Content-Type", "application/json")
                .putHeader("Accept", "application/json")
                .sendJsonObject(payload, this.portalSignInRequestResponseHandler(client, handler));

        LOG.info("Remote authentication request sent");

        return this;

    }

    @Override
    public LenzingCustomerPortalService getUserRegions(final String portalUserId, final String portalAuthToken, Handler<AsyncResult<JsonObject>> handler) {

        Objects.requireNonNull(portalUserId, "Portal user ID must be provided to request user regions from the portal");
        Objects.requireNonNull(portalAuthToken, "Portal auth token for user must be provided to request user regions from the portal");

        final JsonObject payload = new JsonObject()
                .put("id", portalUserId)
                .put("token", portalAuthToken);

        LOG.info(String.format("Remote user region request: %s", payload.encode()));

        final Integer portalPort = portalConfig.getInteger("http.port");
        final String portalHost = portalConfig.getString("http.host");
        final String portalSignInUri = portalConfig.getString("request.uri.user.regions");

        final WebClient client = WebClient.create(vertx);

        client.post(portalPort, portalHost, portalSignInUri)
                .as(BodyCodec.jsonObject())
                .putHeader("Content-Type", "application/json")
                .putHeader("Accept", "application/json")
                .sendJsonObject(payload, this.portalRegionRequestResponseHandler(client, handler));

        LOG.info("Remote user region request sent");

        return this;

    }

    private Handler<AsyncResult<HttpResponse<JsonObject>>> portalSignInRequestResponseHandler(final WebClient client, final Handler<AsyncResult<JsonObject>> handler) {

        return responseHandler -> {

            if (responseHandler.succeeded()) {

                final HttpResponse<JsonObject> response = responseHandler.result();

                LOG.info(String.format("Portal API response: %d, %s", response.statusCode(), response.body().encode()));

                if (response.statusCode() == 200) {

                    try {

                        final JsonObject responseBody = response.body();

                        if (responseBody.getString("auth_token") == null || responseBody.getLong("user_id") == null) {
                            LOG.error(String.format("Remote authentication failed. Portal responded with bad or incomplete data: %s", responseBody.encode()));
                            handler.handle(Future.failedFuture("Portal sent incorrect data"));
                        } else {
                            LOG.info("Remote authentication successful");
                            final JsonObject portalUserData = new JsonObject()
                                    .put("userId", responseBody.getValue("user_id").toString())
                                    .put("userToken", responseBody.getString("auth_token"));
                            handler.handle(Future.succeededFuture(portalUserData));
                        }

                    } catch (final DecodeException ex) {
                        LOG.info("Remote authentication failed. Portal responded with incorrect data.", ex);
                        handler.handle(Future.failedFuture(ex));
                    }

                }

                else if (response.statusCode() == 401) {
                    LOG.warn("Remote authentication failed: incorrect user information");
                    handler.handle(Future.failedFuture("Incorrect user data"));
                }

                else {
                    LOG.info(String.format("Remote authentication failed: portal responded with unknown status code: [%d] -> %s", response.statusCode(), response.bodyAsString()));
                    handler.handle(Future.failedFuture("Incorrect response from the portal"));
                }

            } else {
                LOG.error("Remote authentication failed", responseHandler.cause());
                handler.handle(Future.failedFuture("Authentication failed"));
            }

            client.close();

        };

    }

    private Handler<AsyncResult<HttpResponse<JsonObject>>> portalRegionRequestResponseHandler(final WebClient client, final Handler<AsyncResult<JsonObject>> handler) {

        return responseHandler -> {

            if (responseHandler.succeeded()) {

                final HttpResponse<JsonObject> response = responseHandler.result();

                LOG.info(String.format("Portal API response: %d, %s", response.statusCode(), response.body().encode()));

                if (response.statusCode() == 200) {

                    try {

                        final JsonObject responseBody = response.body();

                        final JsonArray userRegions = responseBody.getJsonArray("regions");

                        if (userRegions == null) {
                            LOG.error(String.format("Remote request failed. Portal responded with bad or incomplete data: %s", responseBody.encode()));
                            handler.handle(Future.failedFuture("Portal sent incorrect data"));
                        } else {
                            LOG.info("Remote user region request successful");
                            try {
                                final JsonArray transformedRegions = new JsonArray();
                                for (int i = 0; i < userRegions.size(); i++) {
                                    final UserRegion region = this.transformPortalUserRegionName(userRegions.getString(i));
                                    transformedRegions.add(region);
                                }
                                handler.handle(Future.succeededFuture(new JsonObject().put("regions", transformedRegions)));
                            } catch (final IllegalArgumentException ex) {
                                LOG.error("Portal responded with one or more illegal region names");
                                handler.handle(Future.failedFuture("Invalid user regions found"));
                            }
                        }

                    } catch (final DecodeException ex) {
                        LOG.info(ex.getMessage(), ex);
                        handler.handle(Future.failedFuture(ex));
                    }

                }

                else if (response.statusCode() == 401) {
                    LOG.warn("Remote request failed: the portal refused the authentication data");
                    handler.handle(Future.failedFuture("Incorrect authentication data"));
                }

                else {
                    LOG.info(String.format("Remote request failed: portal responded with unknown status code: [%d] -> %s", response.statusCode(), response.bodyAsString()));
                    handler.handle(Future.failedFuture("Incorrect response from the portal"));
                }

            } else {
                LOG.error("Remote request failed", responseHandler.cause());
                handler.handle(Future.failedFuture("Request failed"));
            }

            client.close();

        };

    }

    private UserRegion transformPortalUserRegionName(final String portalUserRegionName) throws IllegalArgumentException {

        switch (portalUserRegionName) {

            case "Europe and US": return UserRegion.EU_US;
            case "North Asia":    return UserRegion.NORTH_ASIA;
            case "AMEA":          return UserRegion.AMEA;

            default: throw new IllegalArgumentException(String.format("The user region name [%s] can not be matched with any local region names", portalUserRegionName));

        }

    }
}
