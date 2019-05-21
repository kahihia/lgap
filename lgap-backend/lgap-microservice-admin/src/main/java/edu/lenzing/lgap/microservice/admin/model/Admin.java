package edu.lenzing.lgap.microservice.admin.model;

import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject(generateConverter = true)
public class Admin extends AbstractModel {

    private String userName;
    private String password;

    private String firstName;
    private String lastName;
    private String email;


    public Admin() {
    }

    public Admin(final JsonObject json) {
        AdminConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        AdminConverter.toJson(this, json);
        return json;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
