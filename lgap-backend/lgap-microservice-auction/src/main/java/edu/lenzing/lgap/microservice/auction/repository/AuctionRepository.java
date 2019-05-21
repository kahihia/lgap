package edu.lenzing.lgap.microservice.auction.repository;

import edu.lenzing.lgap.microservice.auction.model.Auction;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Set;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface AuctionRepository extends BaseRepository<Auction> {

    Future<Set<UserRegion>> getRegionsByAuctionId(Long auctionId);

    Future<List<Auction>> search(final JsonObject filter);

}
