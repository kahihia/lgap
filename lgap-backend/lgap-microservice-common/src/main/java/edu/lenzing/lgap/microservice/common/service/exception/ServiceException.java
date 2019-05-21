package edu.lenzing.lgap.microservice.common.service.exception;

import edu.lenzing.lgap.microservice.common.exception.LGAPException;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.Future;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ServiceException extends LGAPException {

    public static <T> Future<T> failedFuture(final String message) {
        return Future.failedFuture(new ServiceException(message));
    }

    public static <T> Future<T> failedFuture(final Throwable cause) {
        return Future.failedFuture(new ServiceException(cause));
    }

    public static <T> Future<T> failedFuture(final String message, final Throwable cause) {
        return Future.failedFuture(new ServiceException(message, cause));
    }

    public ServiceException() {
    }

    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }

}
