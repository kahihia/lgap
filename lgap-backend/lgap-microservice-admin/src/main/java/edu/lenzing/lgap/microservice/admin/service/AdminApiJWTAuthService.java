package edu.lenzing.lgap.microservice.admin.service;

import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface AdminApiJWTAuthService extends ApiJWTAuthService {

    String SERVICE_ADDRESS = "service-eb-admin-auth-jwt";

}
