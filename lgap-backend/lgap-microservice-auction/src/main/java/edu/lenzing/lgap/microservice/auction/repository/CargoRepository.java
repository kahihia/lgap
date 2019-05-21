package edu.lenzing.lgap.microservice.auction.repository;


import edu.lenzing.lgap.microservice.auction.model.Cargo;
import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import io.vertx.core.Future;

import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface CargoRepository extends BaseRepository<Cargo> {

//    Future<Optional<Cargo>> getByAuctionId(Long auctionId);

}
