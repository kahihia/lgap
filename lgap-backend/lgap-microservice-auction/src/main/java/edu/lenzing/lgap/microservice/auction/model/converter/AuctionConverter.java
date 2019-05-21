package edu.lenzing.lgap.microservice.auction.model.converter;

import edu.lenzing.lgap.microservice.auction.model.*;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuctionConverter {

    public static JsonObject toJson(final Auction auction) {

        final JsonObject json = new JsonObject();

        if (auction.getId() != null) {
            json.put("id", auction.getId());
        }

        if (auction.getName() != null) {
            json.put("name", auction.getName());
        }

        if (auction.getDescription() != null) {
            json.put("description", auction.getDescription());
        }

        if (auction.getStartDate() != null) {
            json.put("startDate", auction.getStartTimestamp());
        }

        if (auction.getEndDate() != null) {
            json.put("endDate", auction.getEndTimestamp());
        }

        if (auction.getReservePrice() != null) {
            json.put("reservePrice", auction.getReservePrice());
        }

        if (auction.getAuctionType() != null) {
            json.put("auctionType", auction.getAuctionType());
        }

        if (auction.getAuctionPhase() != null) {
            json.put("auctionPhase", auction.getAuctionPhase());
        }

        if (auction.getRegions() != null) {
            final JsonArray regions = new JsonArray();
            auction.getRegions().forEach(regions::add);
            json.put("regions", regions);
        }

        return json;

    }

    public static void fromJson(final JsonObject json, final Auction auction) {

        auction.setId(json.getLong("id"));
        auction.setName(json.getString("name"));
        auction.setDescription(json.getString("description"));

        if (json.getLong("startDateTimestamp") != null) {
            auction.setStartTimestamp(json.getLong("startDateTimestamp"));
        }

        if (json.getLong("endDateTimestamp") != null) {
            auction.setEndTimestamp(json.getLong("endDateTimestamp"));
        }

        auction.setReservePrice(json.getDouble("reservePrice"));
        auction.setAuctionType(AuctionType.valueOf(json.getString("auctionType")));
        auction.setAuctionPhase(AuctionPhase.valueOf(json.getString("auctionPhase")));

        if (json.getJsonArray("regions") != null) {
            final JsonArray jsonRegions = json.getJsonArray("regions");
            final Set<UserRegion> regions = new HashSet<>();
            for (int i = 0; i < jsonRegions.size(); i++) {
                regions.add(UserRegion.valueOf(jsonRegions.getString(i)));
            }
            auction.setRegions(regions);
        }

    }

}
