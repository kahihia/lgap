package edu.lenzing.lgap.microservice.auction.model.converter;

import edu.lenzing.lgap.microservice.auction.model.*;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class DutchAuctionConverter {

    public static JsonObject toJson(final DutchAuction dutchAuction) {

        final JsonObject json = AuctionConverter.toJson(dutchAuction);

        if (dutchAuction.getCargo() != null) {
            json.put("cargo", dutchAuction.getCargo().toJson());
        }

        if (dutchAuction.getBasePrice() != null) {
            json.put("basePrice", dutchAuction.getBasePrice());
        }

        if (dutchAuction.getPriceModifier() != null) {
            json.put("priceModifier", dutchAuction.getPriceModifier());
        }

        if (dutchAuction.getTurns() != null) {
            final JsonArray turns = new JsonArray();
            dutchAuction.getTurns().forEach(turn -> turns.add(turn.toJson()));
            json.put("turns", turns);
        }

        return json;

    }

    public static void fromJson(final JsonObject json, final DutchAuction dutchAuction) {

        AuctionConverter.fromJson(json, dutchAuction);

        if (json.getJsonObject("cargo") != null) {
            final JsonObject cargoJson = json.getJsonObject("cargo");
            dutchAuction.setCargo(new Cargo(cargoJson));
        }

        dutchAuction.setBasePrice(json.getDouble("basePrice"));
        dutchAuction.setPriceModifier(json.getDouble("priceModifier"));

        if (json.getJsonArray("turns") != null) {
            final JsonArray jsonTurns = json.getJsonArray("turns");
            final List<DutchAuctionTurn> turns = new ArrayList<>();
            for (int i = 0; i < jsonTurns.size(); i++) {
                turns.add(new DutchAuctionTurn(jsonTurns.getJsonObject(i)));
            }
            dutchAuction.setTurns(turns);
        }

    }

}
