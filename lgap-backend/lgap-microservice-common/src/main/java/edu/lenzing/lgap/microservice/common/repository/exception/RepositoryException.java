package edu.lenzing.lgap.microservice.common.repository.exception;

import edu.lenzing.lgap.microservice.common.exception.LGAPException;
import io.vertx.core.Future;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class RepositoryException extends LGAPException {

    public static <T> Future<T> failedFuture(final String message) {
        return Future.failedFuture(new RepositoryException(message));
    }

    public static <T> Future<T> failedFuture(final Throwable cause) {
        return Future.failedFuture(new RepositoryException(cause));
    }

    public static <T> Future<T> failedFuture(final String message, final Throwable cause) {
        return Future.failedFuture(new RepositoryException(message, cause));
    }

    public RepositoryException() {
    }

    public RepositoryException(final String message) {
        super(message);
    }

    public RepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(final Throwable cause) {
        super(cause);
    }

}
