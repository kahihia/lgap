package edu.lenzing.lgap.microservice.product;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.common.model.UserRole;
import edu.lenzing.lgap.microservice.common.verticle.BaseApiVerticle;
import edu.lenzing.lgap.microservice.product.model.Product;
import edu.lenzing.lgap.microservice.product.service.ProductService;
import io.vertx.core.Future;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ProductApiVerticle extends BaseApiVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ProductApiVerticle.class);

    @Inject
    private ProductService productService;

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.route().handler(JWTAuthHandler.create(super.getJWTAuthProvider()));

        router.get("/test0").handler(context -> jwtAuth(context, this::test));
        router.get("/test1").handler(context -> jwtAuth(context, this::test, UserRole.CUSTOMER));
        router.get("/test2").handler(context -> jwtAuth(context, this::test, UserRole.ADMIN));
        router.get("/test3").handler(context -> jwtAuth(context, this::test, UserRole.CUSTOMER, UserRole.ADMIN));

        router.get("/get/all").handler(context ->
                jwtAuth(context, this::apiGetAllProducts, UserRole.ADMIN));

        router.get("/get/:productId").handler(context ->
                jwtAuth(context, this::apiGetProductById, UserRole.ADMIN, UserRole.CUSTOMER));

        router.post("/add").handler(context ->
                jwtAuth(context, this::apiAddProduct, UserRole.ADMIN));

        final JsonObject apiConfig = config().getJsonObject("api");
        createHttpServer(router, apiConfig.getString("http.address"), apiConfig.getInteger("http.port"))
                .compose(serverCreated -> publishHttpEndpoint(apiConfig))
                .setHandler(future.completer());

    }

    private void apiGetAllProducts(final RoutingContext routingContext) {

        productService.getAllProducts(resultHandler -> {
            if (resultHandler.succeeded()) {
                final JsonArray payload = new JsonArray();
                resultHandler.result().stream().map(Product::toJson).forEach(payload::add);
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json")
                        .end(payload.encodePrettily());
            } else {
                LOG.error(resultHandler.cause());
                respondWithInternalError(routingContext);
            }
        });

    }

    private void apiGetProductById(final RoutingContext routingContext) {

        final String productIdParam = routingContext.request().getParam("productId");

        try {

            final Long productId = Long.parseLong(productIdParam);

            productService.getProductById(productId, resultHandler -> {
                if (resultHandler.succeeded()) {
                    if (resultHandler.result() != null) {
                        routingContext.response()
                                .setStatusCode(200)
                                .putHeader("content-type", "application/json")
                                .end(resultHandler.result().toJson().encodePrettily());
                    } else {
                        respondWithNotFound(routingContext);
                    }
                } else {
                    LOG.error(resultHandler.cause());
                    respondWithInternalError(routingContext);
                }
            });

        } catch (final NumberFormatException ex) {
            respondWithBadRequest(routingContext);
        }

    }

    private void apiAddProduct(final RoutingContext routingContext) {

        try {

            final JsonObject requestBody = new JsonObject(routingContext.getBodyAsString());
            final Product product = new Product(requestBody);

            productService.saveProduct(product, resultHandler -> {
                if (resultHandler.succeeded()) {
                    final Product persistedProduct = resultHandler.result();
                    routingContext.response()
                            .setStatusCode(201)
                            .putHeader("content-type", "application/json")
                            .end(new JsonObject()
                                    .put("message", "Product added")
                                    .put("payload", new JsonObject().put("productId", persistedProduct.getId()))
                                    .encodePrettily()
                            );
                } else {
                    LOG.error(resultHandler.cause());
                    respondWithInternalError(routingContext);
                }
            });

        } catch (final DecodeException ex) {
            respondWithBadRequest(routingContext, ex);
        }

    }

}
