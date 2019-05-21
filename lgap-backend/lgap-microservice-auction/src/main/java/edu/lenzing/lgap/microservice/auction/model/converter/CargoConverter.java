package edu.lenzing.lgap.microservice.auction.model.converter;

import edu.lenzing.lgap.microservice.auction.model.Cargo;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class CargoConverter {

    public static JsonObject toJson(final Cargo cargo) {

        final JsonObject json = new JsonObject();

        if (cargo.getId() != null) {
            json.put("id", cargo.getId());
        }

        if (cargo.getExtProductId() != null) {
            json.put("extProductId", cargo.getExtProductId());
        }

        if (cargo.getQuantity() != null) {
            json.put("quantity", cargo.getQuantity());
        }

        if (cargo.getLocation() != null) {
            json.put("location", cargo.getLocation());
        }

        if (cargo.getExpectedDeliveryDays() != null) {
            json.put("expectedDeliveryDays", cargo.getExpectedDeliveryDays());
        }

        return json;

    }

    public static void fromJson(final JsonObject json, final Cargo cargo) {

        cargo.setId(json.getLong("id"));
        cargo.setExtProductId(json.getLong("extProductId"));
        cargo.setQuantity(json.getDouble("quantity"));
        cargo.setLocation(json.getString("location"));
        cargo.setExpectedDeliveryDays(json.getInteger("expectedDeliveryDays"));

    }
}
