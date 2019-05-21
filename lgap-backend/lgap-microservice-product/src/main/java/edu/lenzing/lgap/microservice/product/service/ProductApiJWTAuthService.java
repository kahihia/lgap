package edu.lenzing.lgap.microservice.product.service;

import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface ProductApiJWTAuthService extends ApiJWTAuthService {

    String SERVICE_ADDRESS = "service-eb-product-auth-jwt";

}
