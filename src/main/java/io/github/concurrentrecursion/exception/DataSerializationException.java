package io.github.concurrentrecursion.exception;

/**
 * An exception that occurs during the writing and marshalling of data
 */
public class DataSerializationException extends RuntimeException{
    /**
     * Constructs a new DataSerializationException with {@code null} as its detail message.
     */
    public DataSerializationException() {
        super();
    }

    /**
     * Constructs a new DataSerializationException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public DataSerializationException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataSerializationException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DataSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DataSerializationException with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DataSerializationException(Throwable cause) {
        super(cause);
    }
}
