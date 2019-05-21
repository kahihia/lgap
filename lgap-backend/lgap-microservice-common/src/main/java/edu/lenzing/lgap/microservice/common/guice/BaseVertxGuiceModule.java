package edu.lenzing.lgap.microservice.common.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;

import javax.inject.Named;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class BaseVertxGuiceModule extends AbstractModule {

    protected final Vertx vertx;
    protected final Context context;

    public BaseVertxGuiceModule(final Vertx vertx) {
        this.vertx = vertx;
        this.context = vertx.getOrCreateContext();
    }

    @Provides
    @Singleton
    public Vertx vertx() {
        return this.vertx;
    }

    @Provides
    @Named("VertxConfig")
    public JsonObject vertxConfig() {
        return context.config();
    }

    @Provides
    @Singleton
    @Named("ApiJWTAuthProvider")
    public JWTAuth apiJwtAuthProvider() {

        final JsonObject vertxConfig = context.config().getJsonObject("jwt.auth.provider", context.config());

        final JsonObject keyStoreConfig = new JsonObject()
                .put("path", vertxConfig.getValue("keystore.file"))
                .put("type", vertxConfig.getValue("keystore.type"))
                .put("algorithm", vertxConfig.getValue("keystore.algorithm"))
                .put("password", vertxConfig.getValue("keystore.password"));

        final JsonObject jwtConfig = new JsonObject()
                .put("keyStore", keyStoreConfig);

        return JWTAuth.create(vertx, jwtConfig);

    }

}
