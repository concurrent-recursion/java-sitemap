package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.sitemap.io.SitemapReader;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UrlSetSitemapTests {
    @Test
    void testFromUrls(){
        Url one = new Url("http://example.com").setChangeFrequency(ChangeFrequency.NEVER);
        Url two = new Url("http://example.com/page-two").setLastModifiedDate(OffsetDateTime.now());
        UrlSetSitemap sitemap = UrlSetSitemap.fromUrls(one,two);
        assertEquals(2, sitemap.getUrls().size());
        assertEquals(ChangeFrequency.NEVER, sitemap.getUrls().get(0).getChangeFrequency());
        assertNull(sitemap.getUrls().get(1).getChangeFrequency());
    }

    @Test
    void testAddUrls(){
        Url one = new Url("http://example.com").setChangeFrequency(ChangeFrequency.NEVER);
        UrlSetSitemap sitemap = UrlSetSitemap.fromUrls(one);
        assertEquals(1, sitemap.getUrls().size());
        assertEquals(ChangeFrequency.NEVER, sitemap.getUrls().get(0).getChangeFrequency());

        Url two = new Url("http://example.com/page-two").setLastModifiedDate(OffsetDateTime.now());
        sitemap.addUrl(two);
        assertEquals(2, sitemap.getUrls().size());
        assertNull(sitemap.getUrls().get(1).getChangeFrequency());

    }
}
