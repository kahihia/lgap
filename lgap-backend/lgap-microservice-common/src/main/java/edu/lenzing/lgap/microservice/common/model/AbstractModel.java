package edu.lenzing.lgap.microservice.common.model;

import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public abstract class AbstractModel {

    private Long id;

    public AbstractModel() {
    }

    public AbstractModel(final JsonObject json) {
    }

    public abstract JsonObject toJson();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

}
