package io.github.concurrentrecursion.sitemap;

import io.github.concurrentrecursion.sitemap.model.Url;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;
import io.github.concurrentrecursion.sitemap.model.google.news.News;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlSetSitemapTest {

    @Test
    void testValidation(){
        UrlSetSitemap urlSetSitemap = new UrlSetSitemap();
        urlSetSitemap.addUrl(new Url("http://www.google.com").setPriority(40d));
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UrlSetSitemap>> violations = validator.validate(urlSetSitemap, WriteValidation.class);
            assertEquals(1, violations.size());
        }
    }

    @Test
    void testMaxUrls(){
        UrlSetSitemap urlSetSitemap = new UrlSetSitemap();
        for(int i=0;i<50_001;i++){
            urlSetSitemap.addUrl(new Url("http://www.google.com/page" + i));
        }

        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UrlSetSitemap>> violations = validator.validate(urlSetSitemap, WriteValidation.class);
            assertEquals(1, violations.size());
        }
    }

    @Test
    void testMaxNews(){
        UrlSetSitemap urlSetSitemap = new UrlSetSitemap();
        for(int i=0;i<1_001;i++){
            urlSetSitemap.addUrl(new Url("http://www.google.com/page" + i).setNews(new News().setTitle("Breaking News!")));
        }

        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UrlSetSitemap>> violations = validator.validate(urlSetSitemap, WriteValidation.class);
            assertEquals(1, violations.size());
        }
    }
}
