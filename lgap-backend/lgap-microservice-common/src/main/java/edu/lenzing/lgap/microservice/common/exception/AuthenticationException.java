package edu.lenzing.lgap.microservice.common.exception;

import io.vertx.core.Future;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class AuthenticationException extends LGAPException {

    public static <T> Future<T> failedFuture(final String message) {
        return Future.failedFuture(new AuthenticationException(message));
    }

    public static <T> Future<T> failedFuture(final Throwable cause) {
        return Future.failedFuture(new AuthenticationException(cause));
    }

    public static <T> Future<T> failedFuture(final String message, final Throwable cause) {
        return Future.failedFuture(new AuthenticationException(message, cause));
    }

    public AuthenticationException() {
    }

    public AuthenticationException(final String message) {
        super(message);
    }

    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(final Throwable cause) {
        super(cause);
    }
}
