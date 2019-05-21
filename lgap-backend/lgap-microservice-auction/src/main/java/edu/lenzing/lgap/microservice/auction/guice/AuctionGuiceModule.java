package edu.lenzing.lgap.microservice.auction.guice;


import com.google.inject.Injector;
import edu.lenzing.lgap.microservice.auction.manager.DutchAuctionManager;
import edu.lenzing.lgap.microservice.auction.manager.verticle.DutchAuctionManagerVerticle;
import edu.lenzing.lgap.microservice.auction.repository.*;
import edu.lenzing.lgap.microservice.auction.repository.jdbc.*;
import edu.lenzing.lgap.microservice.auction.service.*;
import edu.lenzing.lgap.microservice.auction.service.impl.*;
import edu.lenzing.lgap.microservice.common.guice.BaseVertxGuiceModule;
import io.vertx.core.Vertx;

import javax.inject.Provider;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionGuiceModule extends BaseVertxGuiceModule {

    private Injector injector;

    public AuctionGuiceModule(final Vertx vertx) {
        super(vertx);
    }

    @Override
    protected void configure() {

        bind(DutchAuctionManager.class).to(DutchAuctionManagerVerticle.class);

        // Service
        bind(AuctionApiJWTAuthService.class).to(AuctionApiJWTAuthServiceImpl.class);
        bind(AuctionBroadcastService.class).to(AuctionBroadcastServiceImpl.class);
        bind(BidService.class).to(BidServiceImpl.class);
        bind(AuctionService.class).to(AuctionServiceImpl.class);
        bind(DutchAuctionService.class).to(DutchAuctionServiceImpl.class);
        bind(DutchAuctionTurnService.class).to(DutchAuctionTurnServiceImpl.class);

        // Repository
        bind(AuctionRepository.class).to(JDBCAuctionRepository.class);
        bind(AuctionManagerRepository.class).to(JDBCAuctionManagerRepository.class);
        bind(BidRepository.class).to(JDBCBidRepository.class);
        bind(CargoRepository.class).to(JDBCCargoRepository.class);
        bind(DutchAuctionRepository.class).to(JDBCDutchAuctionRepository.class);
        bind(DutchAuctionTurnRepository.class).to(JDBCDutchAuctionTurnRepository.class);

    }
}
