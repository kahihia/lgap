package edu.lenzing.lgap.microservice.auction.model;

import edu.lenzing.lgap.microservice.auction.model.converter.CargoConverter;
import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject
public class Cargo extends AbstractModel {

    private Long    extProductId;
    private Double  quantity;
    private String  location;
    private Integer expectedDeliveryDays;

    public Cargo() {}

    public Cargo(final JsonObject json) {
        CargoConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        return CargoConverter.toJson(this);
    }

    public Long getExtProductId() {
        return extProductId;
    }

    public void setExtProductId(Long extProductId) {
        this.extProductId = extProductId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getExpectedDeliveryDays() {
        return expectedDeliveryDays;
    }

    public void setExpectedDeliveryDays(Integer expectedDeliveryDays) {
        this.expectedDeliveryDays = expectedDeliveryDays;
    }
}
