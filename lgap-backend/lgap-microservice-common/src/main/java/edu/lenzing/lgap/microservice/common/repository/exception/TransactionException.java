package edu.lenzing.lgap.microservice.common.repository.exception;

import io.vertx.core.Future;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class TransactionException extends RepositoryException {

    public static <T> Future<T> failedFuture(final String message) {
        return Future.failedFuture(new TransactionException(message));
    }

    public static <T> Future<T> failedFuture(final Throwable cause) {
        return Future.failedFuture(new TransactionException(cause));
    }

    public static <T> Future<T> failedFuture(final String message, final Throwable cause) {
        return Future.failedFuture(new TransactionException(message, cause));
    }

    public TransactionException() {
    }

    public TransactionException(final String message) {
        super(message);
    }

    public TransactionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionException(final Throwable cause) {
        super(cause);
    }
}
