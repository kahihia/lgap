package edu.lenzing.lgap.microservice.customer.model;

import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject(generateConverter = true)
public class Customer extends AbstractModel {

    private String portalId;

    public Customer() {}
    public Customer(final JsonObject json) {
        CustomerConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        CustomerConverter.toJson(this, json);
        return json;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }
}
