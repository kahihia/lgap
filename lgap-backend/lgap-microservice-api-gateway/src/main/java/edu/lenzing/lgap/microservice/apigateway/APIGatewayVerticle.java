package edu.lenzing.lgap.microservice.apigateway;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import edu.lenzing.lgap.microservice.apigateway.guice.GuiceModule;
import edu.lenzing.lgap.microservice.common.service.APIGatewayJWTAuthService;
import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;
import edu.lenzing.lgap.microservice.common.verticle.BaseHttpEndpointVerticle;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class APIGatewayVerticle extends BaseHttpEndpointVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(APIGatewayVerticle.class);

    private CircuitBreaker circuitBreaker;

    @Override
    public void start(final Future<Void> future) throws Exception {
        super.start();

        final Injector guice = Guice.createInjector(new GuiceModule(vertx));

        final JWTAuth apiAuthProvider = guice.getInstance(Key.get(JWTAuth.class, Names.named("ApiJWTAuthProvider")));
        final APIGatewayJWTAuthService apiAuthService = guice.getInstance(APIGatewayJWTAuthService.class);

        final Router router = Router.router(vertx);

        // Enable CORS
        router.route().handler(CorsHandler.create("*")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
        );

        router.route().handler(BodyHandler.create());
        router.route("/debug/services").handler(this::debugServices);
        router.routeWithRegex("^/api/[a-z]+/login$").handler(this::apiRequestDispatchHandler);
        router.route("/api/*").handler(JWTAuthHandler.create(apiAuthProvider));
        router.route("/api/*").handler(this::apiRequestDispatchHandler);
        router.route("/*").handler(this::httpRequestDispatchHandler);

        router.exceptionHandler(exception -> LOG.error(exception.getMessage()));

        registerServiceProxy(ApiJWTAuthService.class, vertx, apiAuthService, APIGatewayJWTAuthService.SERVICE_ADDRESS);

        this.circuitBreaker = this.createCircuitBreaker();

        this.createHttpServer(router).setHandler(httpServer -> {
            if (httpServer.succeeded()) {
                future.complete();
            } else {
                future.fail(httpServer.cause());
            }
        });

    }

    private void debugServices(final RoutingContext routingContext) {

        getServiceRecords().setHandler(handler -> {
            if (handler.succeeded()) {
                JsonArray response = new JsonArray();
                for (final Record record : handler.result()) {
                    response.add(record.toJson());
                }
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json")
                        .end(response.encodePrettily());
            } else {
                respondWithInternalError(routingContext, handler.cause());
            }
        });

    }

    private void apiRequestDispatchHandler(final RoutingContext routingContext) {

        final long dispatchID = System.currentTimeMillis();

        circuitBreaker.execute(circuitBreakerFuture -> getServiceRecords(HttpEndpoint.TYPE).setHandler(serviceRecordHandler -> {

            if (serviceRecordHandler.failed()) {

                respondWithInternalError(routingContext);
                LOG.error(serviceRecordHandler.cause().getMessage(), serviceRecordHandler.cause().getCause());
                circuitBreakerFuture.fail(serviceRecordHandler.cause().getCause());

            } else {

                // The URI should look like this: /api/servicename/resource
                final String uri = routingContext.request().uri();

                LOG.info(String.format("[%d] Routing request to %s", dispatchID, uri));

                if (uri.length() < "/api/".length()) {

                    LOG.error(String.format("[%d] No service name specified in the api request [%s]", dispatchID, uri));
                    respondWithBadRequest(routingContext, "No service name specified");
                    circuitBreakerFuture.complete();

                } else {

                    // Extract the service name and requested resource from the uri
                    final String[] uriFragments = uri.substring("/api/".length()).split("/", 2);

                    if (uriFragments.length < 2) {

                        respondWithBadRequest(routingContext, String.format("[%d] No resource specified in the api request [%s]", dispatchID, uri));
                        circuitBreakerFuture.complete();

                    } else {

                        final String serviceName = uriFragments[0];
                        final String requestedResource = uriFragments[1];

                        LOG.info(String.format("[%d] Identified service: [%s] and resource: [%s]", dispatchID, serviceName, requestedResource));

                        final Optional<Record> service = serviceRecordHandler.result().stream()
                                .filter(record -> record.getMetadata().getString("endpoint.type", "").equals("api"))
                                .filter(record -> record.getMetadata().getString("endpoint.name", "").equals(serviceName))
                                .findAny();

                        if (service.isPresent()) {

                            final Record serviceRecord = service.get();
                            final ServiceReference serviceRecordReference = getServiceReference(serviceRecord);
                            final HttpClient client = serviceRecordReference.get();

                            // Do not request token from the service, because the login resource is not protected
                            if (requestedResource.equals("login")) {

                                dispatchRequestToDownstreamService(routingContext, requestedResource, client, circuitBreakerFuture);

                            } else {

                                final String apiAuthServiceAddress = serviceRecord.getMetadata().getString("endpoint.auth.service.address", null);

                                if (apiAuthServiceAddress != null) {

                                    // Request a auth. token from the identified service
                                    final ApiJWTAuthService apiAuthService = ProxyHelper.createProxy(ApiJWTAuthService.class, vertx, apiAuthServiceAddress);

                                    // This will return the JWT information assigned by the API Gateway to the user performing this request
                                    final JsonObject authInfo = routingContext.user().principal();

                                    LOG.info(String.format("[%d] User principal: %s", dispatchID, authInfo.encode()));

                                    apiAuthService.generateToken(authInfo, handler -> {
                                        if (handler.succeeded()) {
                                            final String token = handler.result();
                                            routingContext.request().headers().add("Authorization", String.format("Bearer %s", token));
                                            dispatchRequestToDownstreamService(routingContext, requestedResource, client, circuitBreakerFuture);
                                        } else {
                                            LOG.error(String.format("[%d] Failed to acquire authentication token from service: %s", dispatchID, serviceName), handler.cause());
                                            respondWithInternalError(routingContext);
                                        }
                                    });

                                } else {
                                    LOG.error(String.format("[%d] The service [%s] did not publish its API authentication service address", dispatchID, service.get().getName()));
                                    respondWithInternalError(routingContext);
                                }

                            }

                        } else {

                            LOG.error(String.format("[%d] No service found to complete the request for uri: [%s]", dispatchID, uri));
                            respondWithNotFound(routingContext, "No service found");
                            circuitBreakerFuture.complete();

                        }

                    }

                }

            }

        })).setHandler(execution -> {

            if (execution.failed()) {
                LOG.error(String.format("[%d] Failed to execute service through the circuit breaker [%s]", dispatchID, routingContext.request().uri()), execution.cause());
                respondWithBadGateway(routingContext);
            }

        });

    }

    private void httpRequestDispatchHandler(final RoutingContext routingContext) {

        circuitBreaker.execute(circuitBreakerFuture -> getServiceRecords(HttpEndpoint.TYPE).setHandler(serviceRecordHandler -> {

            if (serviceRecordHandler.failed()) {

                respondWithInternalError(routingContext);
                LOG.error(serviceRecordHandler.cause().getMessage(), serviceRecordHandler.cause().getCause());
                circuitBreakerFuture.fail(serviceRecordHandler.cause().getCause());

            } else {

                // The URI should look like this: /servicename/resource
                final String uri = routingContext.request().uri();

                // Extract the service name from the uri
                final String[] uriFragments = uri.substring("/".length()).split("/", 2);

                if (uriFragments.length < 1) {

                    respondWithBadRequest(routingContext, String.format("No resource specified in the api request [%s]", uri));
                    circuitBreakerFuture.complete();

                } else {

                    final String serviceName = uriFragments[0];

                    final Optional<Record> service = serviceRecordHandler.result().stream()
                            .filter(record -> record.getMetadata().getString("endpoint.type", "").equals("web"))
                            .filter(record -> record.getMetadata().getString("endpoint.name", "").equals(serviceName))
                            .findAny();

                    if (service.isPresent()) {

                        final Record serviceRecord = service.get();
                        final ServiceReference serviceRecordReference = getServiceReference(serviceRecord);
                        final HttpClient client = serviceRecordReference.get();

                        dispatchRequestToDownstreamService(routingContext, uri, client, circuitBreakerFuture);

                    } else {

                        LOG.error(String.format("No service found to complete the request [%s]", uri));
                        respondWithNotFound(routingContext, "No service found");
                        circuitBreakerFuture.complete();

                    }

                }

            }

        })).setHandler(execution -> {

            if (execution.failed()) {
                LOG.error(String.format("Failed to execute service through the circuit breaker [%s]", routingContext.request().uri()), execution.cause());
                respondWithBadGateway(routingContext);
            }

        });

    }

    /**
     * Loads the configuration for the circuit breaker and creates a new {@link CircuitBreaker} instance with the
     * specified configuration.
     *
     * @return Configured {@link CircuitBreaker} instance.
     */
    private CircuitBreaker createCircuitBreaker() {

        final JsonObject circuitBreakerConfig = config().getJsonObject("circuit.breaker");

        final String circuitBreakerName = circuitBreakerConfig.getString("name");

        final CircuitBreakerOptions circuitBreakerOptions = new CircuitBreakerOptions();
        circuitBreakerOptions.setMaxFailures(circuitBreakerConfig.getInteger("max.failures"));
        circuitBreakerOptions.setTimeout(circuitBreakerConfig.getLong("timeout"));
        circuitBreakerOptions.setResetTimeout(circuitBreakerConfig.getLong("reset.timeout"));
        circuitBreakerOptions.setFallbackOnFailure(circuitBreakerConfig.getBoolean("fallback.on.failure"));

        return CircuitBreaker.create(circuitBreakerName, vertx, circuitBreakerOptions);

    }

    /**
     * Loads the configuration for the http server and creates a new {@link HttpServer} instance with the
     * specified configuration.
     *
     * @param router {@link Router} object to be used by the http server.
     * @return {@link Future} object signaling the outcome of the http server launch.
     */
    private Future<Void> createHttpServer(final Router router) {

        final Future<Void> future = Future.future();

        final JsonObject serverConfig = config().getJsonObject("api");
        final JsonObject sslConfig = serverConfig.getJsonObject("http.ssl.certificate");

        final HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setSsl(true)
                .setKeyStoreOptions(new JksOptions()
                        .setPath(sslConfig.getString("jks.file"))
                        .setPassword(sslConfig.getString("jks.password"))
                );

        vertx.createHttpServer(httpServerOptions)
                .requestHandler(router::accept)
                .listen(serverConfig.getInteger("http.port"), serverConfig.getString("http.address"), handler -> {
                    if (handler.succeeded()) {
                        publishHttpEndpoint(serverConfig).setHandler(publishHandler -> {
                            if (publishHandler.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(publishHandler.cause());
                            }
                        });
                    } else {
                        future.fail(handler.cause());
                    }
                });

        return future;

    }

    /**
     * Constructs and sends the request to the selected downstream service provider.
     *
     * @param routingContext       Routing context.
     * @param path                 Path to the downstream service.
     * @param downstreamService    Downstream service provider.
     * @param circuitBreakerFuture {@link Future} object originating from the circuit breaker dealing with this request.
     */
    private void dispatchRequestToDownstreamService(final RoutingContext routingContext, final String path, final HttpClient downstreamService, final Future<Object> circuitBreakerFuture) {

        HttpClientRequest serviceRequest = downstreamService.request(
                routingContext.request().method(),
                path.startsWith("/") ? path : "/" + path, // Make sure that the requested path starts with /
                this.downstreamServiceResponseDelegateHandler(routingContext, circuitBreakerFuture, downstreamService)
        );

        // Copy the request headers
        routingContext.request().headers().forEach(header -> serviceRequest.putHeader(header.getKey(), header.getValue()));

        // Send the request to the downstream service
        if (routingContext.getBody() == null) {
            serviceRequest.end();
        } else {
            serviceRequest.end(routingContext.getBody());
        }

    }

    /**
     * Provides a handler to transmit the response of a downstream service towards the user who made the initial request
     * to the api gateway.
     *
     * @param routingContext       Routing context.
     * @param circuitBreakerFuture {@link Future} object originating from the circuit breaker dealing with this request.
     * @param service              Service provider object to be released here.
     * @return Downstream service response delegate handler.
     */
    private Handler<HttpClientResponse> downstreamServiceResponseDelegateHandler(final RoutingContext routingContext, final Future<Object> circuitBreakerFuture, final Object service) {

        return responseHandler -> responseHandler.bodyHandler(bodyHandler -> {

            // In case the downstream service returned with status code 500 or higher, the circuit breaker should fail
            if (responseHandler.statusCode() >= 500) {
                circuitBreakerFuture.fail(responseHandler.statusCode() + ": " + bodyHandler.toString());
            }

            // Otherwise the response from the downstream service is delegated to the user
            else {
                final HttpServerResponse response = routingContext.response().setStatusCode(responseHandler.statusCode());
                responseHandler.headers().forEach(header -> response.putHeader(header.getKey(), header.getValue()));
                response.end(bodyHandler);
                circuitBreakerFuture.complete();
            }

            // This is the point where the previously acquired service reference is no longer used,
            // therefore it is safe to release it here
            releaseService(service);

        });

    }

}
