package edu.lenzing.lgap.microservice.auction.model.converter;

import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class DutchAuctionTurnConverter {

    public static JsonObject toJson(final DutchAuctionTurn dutchAuctionTurn) {

        final JsonObject json = new JsonObject();

        if (dutchAuctionTurn.getId() != null) {
            json.put("id", dutchAuctionTurn.getId());
        }

        if (dutchAuctionTurn.getTurnNumber() != null) {
            json.put("turnNumber", dutchAuctionTurn.getTurnNumber());
        }

        if (dutchAuctionTurn.getTurnDuration() != null) {
            json.put("turnDuration", dutchAuctionTurn.getTurnDuration());
        }

        if (dutchAuctionTurn.getStarted() != null) {
            json.put("started", dutchAuctionTurn.getStarted() ? 1 : 0);
        }

        if (dutchAuctionTurn.getStartDate() != null) {
            json.put("startDateTimestamp", dutchAuctionTurn.getStartTimestamp());
        }

        if (dutchAuctionTurn.getFinished() != null) {
            json.put("finished", dutchAuctionTurn.getFinished() ? 1 : 0);
        }

        if (dutchAuctionTurn.getEndDate() != null) {
            json.put("endDateTimestamp", dutchAuctionTurn.getEndTimestamp());
        }

        return json;

    }

    public static void fromJson(final JsonObject json, final DutchAuctionTurn dutchAuctionTurn) {

        final Boolean started = json.getInteger("started") == 1;
        final Boolean finished = json.getInteger("finished") == 1;

        dutchAuctionTurn.setId(json.getLong("id"));
        dutchAuctionTurn.setTurnNumber(json.getInteger("turnNumber"));
        dutchAuctionTurn.setTurnDuration(json.getLong("turnDuration"));
        dutchAuctionTurn.setStarted(started);
        dutchAuctionTurn.setStartTimestamp(json.getLong("startDateTimestamp"));
        dutchAuctionTurn.setFinished(finished);
        dutchAuctionTurn.setEndTimestamp(json.getLong("endDateTimestamp"));

    }
}
