package io.github.concurrentrecursion.sitemap.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;
import java.net.URL;

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
