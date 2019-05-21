package edu.lenzing.lgap.microservice.auction.service;

import edu.lenzing.lgap.microservice.auction.model.Bid;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@ProxyGen
@VertxGen
public interface BidService {

    @Fluent
    BidService bid(Bid bid, Handler<AsyncResult<Bid>> handler);

}
