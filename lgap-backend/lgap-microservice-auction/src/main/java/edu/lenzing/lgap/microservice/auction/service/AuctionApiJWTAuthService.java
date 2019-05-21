package edu.lenzing.lgap.microservice.auction.service;

import edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface AuctionApiJWTAuthService extends ApiJWTAuthService {

    String SERVICE_ADDRESS = "service-eb-auction-auth-jwt";

}
