package io.github.concurrentrecursion.sitemap.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Validates a list of Urls to ensure that there are not more than "max" news entries
 */
@Constraint(validatedBy = MaxNewsConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxNewsConstraint {
    /**
     * The error message if the validation fails
     * @return the message
     */
    String message() default "UrlSet cannot contain more than 1000 news items";

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
     * The maximum number of news items in the list. -1 disables max validation
     * @return the max
     */
    int max() default 1000;
}
