package edu.lenzing.lgap.microservice.auction.repository;

import edu.lenzing.lgap.microservice.auction.model.AuctionManager;
import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface AuctionManagerRepository extends BaseRepository<AuctionManager> {

    Future<List<AuctionManager>> getAll();

}
