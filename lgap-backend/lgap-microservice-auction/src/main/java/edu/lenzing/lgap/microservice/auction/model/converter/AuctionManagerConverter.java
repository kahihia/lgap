package edu.lenzing.lgap.microservice.auction.model.converter;

import edu.lenzing.lgap.microservice.auction.model.Auction;
import edu.lenzing.lgap.microservice.auction.model.AuctionManager;
import edu.lenzing.lgap.microservice.auction.model.AuctionPhase;
import edu.lenzing.lgap.microservice.auction.model.AuctionType;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionManagerConverter {

    public static JsonObject toJson(final AuctionManager auctionManager) {

        final JsonObject json = new JsonObject();

        if (auctionManager.getId() != null) {
            json.put("id", auctionManager.getId());
        }

        if (auctionManager.getName() != null) {
            json.put("name", auctionManager.getName());
        }

        return json;

    }

    public static void fromJson(final JsonObject json, final AuctionManager auctionManager) {

        auctionManager.setId(json.getLong("id"));
        auctionManager.setName(json.getString("name"));

    }

}
