package edu.lenzing.lgap.microservice.product.repository;

import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import edu.lenzing.lgap.microservice.product.model.Product;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface ProductRepository extends BaseRepository<Product> {

    Future<List<Product>> getAll();

}
