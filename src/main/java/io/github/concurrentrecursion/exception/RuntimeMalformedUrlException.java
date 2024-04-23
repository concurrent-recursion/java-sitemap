package io.github.concurrentrecursion.exception;

/**
 * An exception indicating that a URL is malformed, removes the need to declare exceptions for Malformed URLS.
 */
public class RuntimeMalformedUrlException extends RuntimeException{

    /**
     * Constructs a new RuntimeMalformedUrlException with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public RuntimeMalformedUrlException(Throwable cause) {
        super(cause);
    }
}
