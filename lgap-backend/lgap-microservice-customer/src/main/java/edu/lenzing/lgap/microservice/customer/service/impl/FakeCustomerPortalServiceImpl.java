package edu.lenzing.lgap.microservice.customer.service.impl;

import edu.lenzing.lgap.microservice.common.model.UserRegion;
import edu.lenzing.lgap.microservice.customer.service.LenzingCustomerPortalService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.*;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class FakeCustomerPortalServiceImpl implements LenzingCustomerPortalService {

    private static final Logger LOG = LoggerFactory.getLogger(FakeCustomerPortalServiceImpl.class);

    private final Map<String, FakeUser> userIdRegistry;
    private final Map<String, FakeUser> userEmailRegistry;

    public FakeCustomerPortalServiceImpl() {

        this.userIdRegistry = new HashMap<>();
        this.userEmailRegistry = new HashMap<>();

        FakeUser all = new FakeUser("u-all","all@lgap.com", "all", UserRegion.EU_US, UserRegion.AMEA, UserRegion.NORTH_ASIA);
        FakeUser none = new FakeUser("u-none","none@lgap.com", "none");
        FakeUser euus = new FakeUser("u-euus","euus@lgap.com", "euus", UserRegion.EU_US);
        FakeUser amea = new FakeUser("u-amea","amea@lgap.com", "amea", UserRegion.AMEA);
        FakeUser asia = new FakeUser("u-asia","asia@lgap.com", "asia", UserRegion.NORTH_ASIA);

        userIdRegistry.put(all.getPortalUserId(), all);
        userEmailRegistry.put(all.getEmailAddress(), all);
        userIdRegistry.put(none.getPortalUserId(), none);
        userEmailRegistry.put(none.getEmailAddress(), none);
        userIdRegistry.put(euus.getPortalUserId(), euus);
        userEmailRegistry.put(euus.getEmailAddress(), euus);
        userIdRegistry.put(amea.getPortalUserId(), amea);
        userEmailRegistry.put(amea.getEmailAddress(), amea);
        userIdRegistry.put(asia.getPortalUserId(), asia);
        userEmailRegistry.put(asia.getEmailAddress(), asia);

    }

    @Override
    public LenzingCustomerPortalService signIn(String email, String password, Handler<AsyncResult<JsonObject>> handler) {

        Objects.requireNonNull(email, "Email address must be provided to perform sign-in with the portal");
        Objects.requireNonNull(password, "Password must be provided to perform sign-in with the portal");

        final JsonObject payload = new JsonObject()
                .put("user", new JsonObject()
                        .put("email", email)
                        .put("password", password)
                );

        LOG.info(String.format("Remote authentication request: %s", payload.encode()));

        final FakeUser user = userEmailRegistry.get(email);

        if (user != null && user.getPassword().equals(password)) {
            LOG.info("Fake authentication successful");
            final JsonObject portalUserData = new JsonObject()
                    .put("userId", user.getPortalUserId())
                    .put("userToken", user.getPortalAuthToken());
            handler.handle(Future.succeededFuture(portalUserData));
        } else {
            LOG.warn("Fake authentication failed: incorrect user information");
            handler.handle(Future.failedFuture("Incorrect user data"));
        }

        return this;

    }

    @Override
    public LenzingCustomerPortalService getUserRegions(String portalUserId, String portalAuthToken, Handler<AsyncResult<JsonObject>> handler) {

        Objects.requireNonNull(portalUserId, "Portal user ID must be provided to request user regions from the portal");
        Objects.requireNonNull(portalAuthToken, "Portal auth token for user must be provided to request user regions from the portal");

        final JsonObject payload = new JsonObject()
                .put("id", portalUserId)
                .put("token", portalAuthToken);

        LOG.info(String.format("Fake user region request: %s", payload.encode()));

        final FakeUser user = userIdRegistry.get(portalUserId);

        if (user != null && user.getPortalAuthToken().equals(portalAuthToken)) {
            LOG.info("Fake user region request successful");
            final JsonArray transformedRegions = new JsonArray();
            for (int i = 0; i < user.getRegions().size(); i++) {
                transformedRegions.add(user.getRegions().get(i));
            }
            handler.handle(Future.succeededFuture(new JsonObject().put("regions", transformedRegions)));
        } else {
            LOG.error("Fake user region request failed");
            handler.handle(Future.failedFuture("Request failed"));
        }

        return this;

    }

    private class FakeUser {

        private final String portalUserId;
        private final String portalAuthToken;

        private final String emailAddress;
        private final String password;
        private final List<UserRegion> regions;

        FakeUser(String portalUserId, String emailAddress, String password, UserRegion... regions) {
            this.portalUserId = portalUserId;
            this.portalAuthToken = UUID.randomUUID().toString();
            this.emailAddress = emailAddress;
            this.password = password;
            this.regions = Arrays.asList(regions);
        }

        public String getPortalUserId() {
            return portalUserId;
        }

        public String getPortalAuthToken() {
            return portalAuthToken;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getPassword() {
            return password;
        }

        public List<UserRegion> getRegions() {
            return regions;
        }

        @Override
        public String toString() {
            return "FakeUser{" +
                    "portalUserId='" + portalUserId + '\'' +
                    ", portalAuthToken='" + portalAuthToken + '\'' +
                    ", emailAddress='" + emailAddress + '\'' +
                    ", password='" + password + '\'' +
                    ", regions=" + regions +
                    '}';
        }
    }

}
