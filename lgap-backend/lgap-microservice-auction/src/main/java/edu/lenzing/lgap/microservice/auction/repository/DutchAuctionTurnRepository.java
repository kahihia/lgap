package edu.lenzing.lgap.microservice.auction.repository;

import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface DutchAuctionTurnRepository extends BaseRepository<DutchAuctionTurn> {

    Future<List<DutchAuctionTurn>> saveAll(List<DutchAuctionTurn> turns, SQLConnection transaction);

    Future<List<DutchAuctionTurn>> getAllByAuctionId(Long id);

    Future<DutchAuctionTurn> markAsStarted(DutchAuctionTurn turn);

    Future<DutchAuctionTurn> markAsFinished(DutchAuctionTurn turn);
    Future<List<DutchAuctionTurn>> markAllAsFinished(List<DutchAuctionTurn> turns);

    Future<Void> rollNextTurn(DutchAuctionTurn currentTurn, DutchAuctionTurn nextTurn);

}
