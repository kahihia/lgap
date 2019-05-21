package edu.lenzing.lgap.microservice.product.service;

import edu.lenzing.lgap.microservice.product.model.Product;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface ProductService {

    String SERVICE_ADDRESS = "service-eb-product";

    @Fluent
    ProductService getProductById(Long id, Handler<AsyncResult<Product>> handler);

    @Fluent
    ProductService getAllProducts(Handler<AsyncResult<List<Product>>> handler);

    @Fluent
    ProductService saveProduct(Product product, Handler<AsyncResult<Product>> handler);
}
