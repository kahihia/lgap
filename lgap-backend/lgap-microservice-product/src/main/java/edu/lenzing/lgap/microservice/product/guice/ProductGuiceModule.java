package edu.lenzing.lgap.microservice.product.guice;

import edu.lenzing.lgap.microservice.common.guice.BaseVertxGuiceModule;
import edu.lenzing.lgap.microservice.product.repository.ProductRepository;
import edu.lenzing.lgap.microservice.product.repository.jdbc.JDBCProductRepository;
import edu.lenzing.lgap.microservice.product.service.ProductApiJWTAuthService;
import edu.lenzing.lgap.microservice.product.service.ProductService;
import edu.lenzing.lgap.microservice.product.service.impl.ProductApiJWTAuthServiceImpl;
import edu.lenzing.lgap.microservice.product.service.impl.ProductServiceImpl;
import io.vertx.core.Vertx;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ProductGuiceModule extends BaseVertxGuiceModule {

    public ProductGuiceModule(final Vertx vertx) {
        super(vertx);
    }

    @Override
    protected void configure() {

        // Repository
        bind(ProductRepository.class).to(JDBCProductRepository.class);

        // Service
        bind(ProductService.class).to(ProductServiceImpl.class);
        bind(ProductApiJWTAuthService.class).to(ProductApiJWTAuthServiceImpl.class);

    }

}
