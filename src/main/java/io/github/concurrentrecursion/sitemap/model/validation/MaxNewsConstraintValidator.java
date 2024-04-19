package io.github.concurrentrecursion.sitemap.model.validation;

import io.github.concurrentrecursion.sitemap.model.Url;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

/**
 * Validates a list of Urls to ensure that there are not more than "max" news entries
 */
public class MaxNewsConstraintValidator implements ConstraintValidator<MaxNewsConstraint, List<Url>> {
    private int max;

    @Override
    public void initialize(MaxNewsConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<Url> urls, ConstraintValidatorContext constraintValidatorContext) {
        if(urls == null || urls.isEmpty() || max < 0) return true;
        return urls.stream().filter(u -> u.getNews() != null).count() <= max;
    }
}
