package edu.lenzing.lgap.microservice.customer.service;

import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface CustomerApiJWTAuthService extends ApiJWTAuthService {

    String SERVICE_ADDRESS = "service-eb-customer-auth-jwt";

}
