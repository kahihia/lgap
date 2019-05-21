package edu.lenzing.lgap.microservice.auction.model;

import edu.lenzing.lgap.microservice.auction.model.converter.AuctionManagerConverter;
import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject
public class AuctionManager extends AbstractModel {

    private String name;

    public AuctionManager() {}

    public AuctionManager(final JsonObject json) {
        AuctionManagerConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        return AuctionManagerConverter.toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
