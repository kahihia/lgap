package edu.lenzing.lgap.microservice.product.model;

import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject(generateConverter = true)
public class Product extends AbstractModel {

    private String name;
    private String description;
    private String imageUrl;

    public Product() {
    }

    public Product(final JsonObject json) {
        ProductConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        ProductConverter.toJson(this, json);
        return json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
