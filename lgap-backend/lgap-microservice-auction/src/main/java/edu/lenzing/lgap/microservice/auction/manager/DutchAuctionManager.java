package edu.lenzing.lgap.microservice.auction.manager;

import edu.lenzing.lgap.microservice.auction.model.DutchAuction;
import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import io.vertx.core.Verticle;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface DutchAuctionManager extends Verticle, AuctionManager<DutchAuction> {

    String CURRENT_TURN_SERVICE_ADDRESS = "service-eb-auction-dutch-manager";

    DutchAuctionTurn getCurrentTurn();

}
