package edu.lenzing.lgap.microservice.auction.repository;

import edu.lenzing.lgap.microservice.auction.model.AuctionPhase;
import edu.lenzing.lgap.microservice.auction.model.DutchAuction;
import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface DutchAuctionRepository extends BaseRepository<DutchAuction> {

    Future<List<DutchAuction>> getActiveAuctions();

    Future<DutchAuction> changePhase(DutchAuction auction, AuctionPhase newPhase);

}
