package io.github.concurrentrecursion.exception;

/**
 * An exception that occurs during the retrieval and unmarshalling of data
 */
public class DataAccessException extends RuntimeException{
    /**
     * Constructs a new DataAccessException with {@code null} as its detail message.
     */
    public DataAccessException() {
        super();
    }

    /**
     * Constructs a new DataAccessException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataAccessException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DataAccessException with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
