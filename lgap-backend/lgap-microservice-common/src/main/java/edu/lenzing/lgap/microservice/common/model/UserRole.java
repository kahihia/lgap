package edu.lenzing.lgap.microservice.common.model;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public enum UserRole {

    ADMIN, CUSTOMER;

    private final String authrity;

    UserRole() {
        this.authrity = String.format("role:%s", this.toString().toLowerCase());
    }

    public String getAuthority() {
        return this.authrity;
    }
}
