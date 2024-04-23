package io.github.concurrentrecursion.exception;

/**
 * An exception that occurs during the retrieval and unmarshalling of data
 */
public class DataAccessException extends RuntimeException{

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
