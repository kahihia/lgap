package edu.lenzing.lgap.microservice.common.exception;


import io.vertx.core.Future;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class LGAPException extends RuntimeException {

    public static <T> Future<T> failedFuture(final String message) {
        return Future.failedFuture(new LGAPException(message));
    }

    public static <T> Future<T> failedFuture(final Throwable cause) {
        return Future.failedFuture(new LGAPException(cause));
    }

    public static <T> Future<T> failedFuture(final String message, final Throwable cause) {
        return Future.failedFuture(new LGAPException(message, cause));
    }

    public LGAPException() {
    }

    public LGAPException(final String message) {
        super(message);
    }

    public LGAPException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LGAPException(final Throwable cause) {
        super(cause);
    }

}
