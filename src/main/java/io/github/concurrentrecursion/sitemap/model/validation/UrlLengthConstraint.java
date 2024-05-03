package io.github.concurrentrecursion.sitemap.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The UrlLengthConstraint annotation is used to apply a maximum length constraint on a URL
 * field. It can be applied to any field with a URL data type.
 */
@Target(ElementType.FIELD)
@Constraint(validatedBy = UrlLengthConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlLengthConstraint {
    /**
     * The error message if the validation fails
     * @return the message
     */
    String message() default "Url cannot be longer than 2048 characters";

    /**
     * The validation groups
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * The payload
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * The maximum length of a URL. -1 disables max validation
     * @return the max length
     */
    int max() default 2048;
}
