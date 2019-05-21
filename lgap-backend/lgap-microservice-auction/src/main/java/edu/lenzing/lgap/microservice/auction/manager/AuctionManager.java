package edu.lenzing.lgap.microservice.auction.manager;

import edu.lenzing.lgap.microservice.auction.model.Auction;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface AuctionManager<T extends Auction> {

    void manage(T auction);

}
