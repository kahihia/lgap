package edu.lenzing.lgap.microservice.admin.repository;

import edu.lenzing.lgap.microservice.admin.model.Admin;
import edu.lenzing.lgap.microservice.common.repository.BaseRepository;
import io.vertx.core.Future;

import java.util.Optional;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public interface AdminRepository extends BaseRepository<Admin> {

    Future<Optional<Admin>> getByUserNameAndPassword(String userName, String password);

}
