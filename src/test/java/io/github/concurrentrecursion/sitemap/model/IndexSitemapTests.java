package io.github.concurrentrecursion.sitemap.model;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexSitemapTests {
    private static final SecureRandom random = new SecureRandom();
    @Test
    void test50kAutomaticList() throws Exception{
        String base = "https://example.com/pages/";
        List<Url> siteUrls = new ArrayList<>();
        for(int i = 0; i < 60_000; i++){
            siteUrls.add(new Url(base + "page" + i + ".html"));
        }
        IndexSitemap indexSitemap = new IndexSitemap(URI.create("https://www.example.com/").toURL(),siteUrls);
        assertEquals(2,indexSitemap.getAllUrlSets().size());
        assertEquals(50_000,indexSitemap.getAllUrlSets().get(0).getUrls().size());
        assertEquals(10_000,indexSitemap.getAllUrlSets().get(1).getUrls().size());
    }
    @Test
    void test50kAutomaticStream() throws Exception{
        String base = "https://example.com/pages/";
        List<Url> siteUrls = new ArrayList<>();
        for(int i = 0; i < 60_000; i++){
            siteUrls.add(new Url(base + "page" + i + ".html"));
        }
        IndexSitemap indexSitemap = new IndexSitemap(URI.create("https://www.example.com/").toURL(),siteUrls.stream());
        assertEquals(2,indexSitemap.getAllUrlSets().size());
        assertEquals(50_000,indexSitemap.getAllUrlSets().get(0).getUrls().size());
        assertEquals(10_000,indexSitemap.getAllUrlSets().get(1).getUrls().size());
    }
}
