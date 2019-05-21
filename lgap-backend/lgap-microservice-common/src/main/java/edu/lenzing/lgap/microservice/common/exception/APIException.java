package edu.lenzing.lgap.microservice.common.exception;

import io.vertx.core.Future;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class ApiException extends LGAPException {

    public static <T> Future<T> failedFuture(final String message) {
        return Future.failedFuture(new ApiException(message));
    }

    public static <T> Future<T> failedFuture(final Throwable cause) {
        return Future.failedFuture(new ApiException(cause));
    }

    public static <T> Future<T> failedFuture(final String message, final Throwable cause) {
        return Future.failedFuture(new ApiException(message, cause));
    }

    public ApiException() {
    }

    public ApiException(final String message) {
        super(message);
    }

    public ApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ApiException(final Throwable cause) {
        super(cause);
    }

}
