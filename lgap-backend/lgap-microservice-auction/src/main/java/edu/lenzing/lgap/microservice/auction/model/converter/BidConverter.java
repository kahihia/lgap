package edu.lenzing.lgap.microservice.auction.model.converter;

import edu.lenzing.lgap.microservice.auction.model.Bid;
import edu.lenzing.lgap.microservice.auction.model.Cargo;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class BidConverter {

    public static JsonObject toJson(final Bid bid) {

        final JsonObject json = new JsonObject();

        if (bid.getId() != null) {
            json.put("id", bid.getId());
        }

        if (bid.getAuctionId() != null ) {
            json.put("auctionId", bid.getAuctionId());
        }

        if (bid.getExtCustomerId() != null) {
            json.put("extCustomerId", bid.getExtCustomerId());
        }

        if (bid.getBid() != null) {
            json.put("value", bid.getBid());
        }

        if (bid.getBidTimestamp() != null) {
            json.put("timestamp", bid.getBidTimestamp());
        }

        return json;

    }

    public static void fromJson(final JsonObject json, final Bid bid) {

        bid.setId(json.getLong("id"));
        bid.setAuctionId(json.getLong("auctionId"));
        bid.setExtCustomerId(json.getLong("extCustomerId"));
        bid.setBid(json.getDouble("value"));
        bid.setBidTimestamp(json.getLong("timestamp"));

    }
}
