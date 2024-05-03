package io.github.concurrentrecursion.sitemap.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;
import java.net.URL;

/**
 * The UrlLengthConstraintValidator class is a custom constraint validator that checks if a given URL
 * does not exceed a specified maximum length.
 * <p>
 * This class implements the ConstraintValidator interface and is annotated with the @Constraint(validatedBy = ...)
 * annotation to specify that it is associated with the UrlLengthConstraint annotation.
 */
public class UrlLengthConstraintValidator implements ConstraintValidator<UrlLengthConstraint, URL> {
    private int max;

    @Override
    public boolean isValid(URL url, ConstraintValidatorContext constraintValidatorContext) {
        if(url == null || max <= 0) return true;
        String asciiUrl = URI.create(url.toString()).toASCIIString();
        return asciiUrl.length() <= max;
    }

    @Override
    public void initialize(UrlLengthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.max = constraintAnnotation.max();
    }
}
