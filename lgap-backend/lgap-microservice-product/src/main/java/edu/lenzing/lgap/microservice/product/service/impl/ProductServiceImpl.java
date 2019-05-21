package edu.lenzing.lgap.microservice.product.service.impl;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.common.service.exception.ServiceException;
import edu.lenzing.lgap.microservice.product.model.Product;
import edu.lenzing.lgap.microservice.product.repository.ProductRepository;
import edu.lenzing.lgap.microservice.product.service.ProductService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.List;
import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ProductServiceImpl implements ProductService {

    @Inject
    private ProductRepository productRepository;

    @Override
    public ProductService getProductById(final Long id, final Handler<AsyncResult<Product>> handler) {

        productRepository.getById(id).setHandler(repository -> {
            if (repository.succeeded()) {
                if (repository.result().isPresent()) {
                    handler.handle(Future.succeededFuture(repository.result().get()));
                } else {
                    handler.handle(Future.succeededFuture(null));
                }
            } else {
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }

    @Override
    public ProductService getAllProducts(final Handler<AsyncResult<List<Product>>> handler) {

        productRepository.getAll().setHandler(repository -> {
            if (repository.succeeded()) {
                handler.handle(Future.succeededFuture(repository.result()));
            } else {
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }

    @Override
    public ProductService saveProduct(final Product product, final Handler<AsyncResult<Product>> handler) {

        productRepository.save(product).setHandler(repository -> {
            if (repository.succeeded()) {
                handler.handle(Future.succeededFuture(repository.result()));
            } else {
                handler.handle(ServiceException.failedFuture(repository.cause()));
            }
        });

        return this;
    }
}
