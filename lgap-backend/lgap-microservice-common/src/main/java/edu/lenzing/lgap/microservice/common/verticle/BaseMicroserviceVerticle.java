package edu.lenzing.lgap.microservice.common.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class BaseMicroserviceVerticle extends AbstractVerticle {

    private final Set<MessageConsumer<JsonObject>> serviceProxyRegistry;
    private final Set<Record> serviceRegistry;

    private ServiceDiscovery serviceDiscovery;


    public BaseMicroserviceVerticle() {

        serviceProxyRegistry = new ConcurrentHashSet<>();
        serviceRegistry = new ConcurrentHashSet<>();

    }

    @Override
    public void start() throws Exception {
        serviceDiscovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));
    }

    @Override
    public void stop(final Future<Void> future) {

        if (!serviceProxyRegistry.isEmpty()) {
            for (final MessageConsumer<JsonObject> serviceProxy : serviceProxyRegistry) {
                ProxyHelper.unregisterService(serviceProxy);
            }
        }

        if (serviceRegistry.isEmpty()) {

            serviceDiscovery.close();
            future.complete();

        } else {

            final List<Future> futures = new ArrayList<>();

            // Try to un-publish all the registered services
            serviceRegistry.forEach(record -> {
                final Future<Void> unregistrationFuture = Future.future();
                futures.add(unregistrationFuture);
                serviceDiscovery.unpublish(record.getRegistration(), unregistrationFuture.completer());
            });

            // Assign a handler to the futures generated by un-publishing the registered services
            CompositeFuture.all(futures).setHandler(handler -> {
                serviceDiscovery.close();
                if (handler.succeeded()) {
                    future.complete();
                } else {
                    future.fail(handler.cause());
                }
            });

        }

    }

    protected <T> MessageConsumer<JsonObject> registerServiceProxy(final Class<T> serviceClass, final Vertx vertx, final T serviceInstance, final String serviceAddress) {
        final MessageConsumer<JsonObject> serviceProxy = ProxyHelper.registerService(serviceClass, vertx, serviceInstance, serviceAddress);
        serviceProxyRegistry.add(serviceProxy);
        return serviceProxy;
    }

    protected void unregisterServiceProxy(final MessageConsumer<JsonObject> serviceProxy) {
        serviceProxyRegistry.remove(serviceProxy);
        ProxyHelper.unregisterService(serviceProxy);
    }

    protected ServiceReference getServiceReference(final Record serviceRecord) {
        return serviceDiscovery.getReference(serviceRecord);
    }

    protected Future<List<Record>> getServiceRecords() {
        final Future<List<Record>> future = Future.future();
        serviceDiscovery.getRecords(new JsonObject().put("type", "*"), future.completer());
        return future;
    }

    protected Future<List<Record>> getServiceRecords(final String type) {
        final Future<List<Record>> future = Future.future();
        serviceDiscovery.getRecords(record -> record.getType().equals(type), future.completer());
        return future;
    }

    protected void releaseService(final Object service) {
        ServiceDiscovery.releaseServiceObject(serviceDiscovery, service);
    }

    protected Future<Void> publishHttpEndpoint(final JsonObject config) {

        final String  serviceName = config.getString("service.name");
        final String  httpHost    = config.getString("http.address");
        final Integer httpPort    = config.getInteger("http.port");
        final Boolean sslEnabled  = config.getBoolean("http.ssl");
        final JsonObject metadata = config.getJsonObject("metadata", new JsonObject());

        final Record serviceRecord = HttpEndpoint.createRecord(serviceName, sslEnabled, httpHost, httpPort, "/", metadata)
                .setType(HttpEndpoint.TYPE);

        return this.publishServiceRecord(serviceRecord);

    }

    private Future<Void> publishServiceRecord(final Record serviceRecord) {

        final Future<Void> future = Future.future();

        serviceDiscovery.publish(serviceRecord, publishHandler -> {
            if (publishHandler.succeeded()) {
                serviceRegistry.add(serviceRecord);
                future.complete();
            } else {
                future.fail(publishHandler.cause());
            }
        });

        return future;

    }
}