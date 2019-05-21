package edu.lenzing.lgap.microservice.auction.model;

import edu.lenzing.lgap.microservice.auction.model.converter.DutchAuctionConverter;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject
public class DutchAuction extends Auction {

    private Cargo cargo;

    private Double basePrice;
    private Double priceModifier;
    private List<DutchAuctionTurn> turns;

    public DutchAuction() {}

    public DutchAuction(final JsonObject json) {
        DutchAuctionConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        return DutchAuctionConverter.toJson(this);
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getPriceModifier() {
        return priceModifier;
    }

    public void setPriceModifier(Double priceModifier) {
        this.priceModifier = priceModifier;
    }

    public List<DutchAuctionTurn> getTurns() {
        return turns;
    }

    public void setTurns(List<DutchAuctionTurn> turns) {
        this.turns = turns;
    }
}
