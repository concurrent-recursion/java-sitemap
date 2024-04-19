package io.github.concurrentrecursion.sitemap;

import io.github.concurrentrecursion.UrlMockUtil;
import io.github.concurrentrecursion.sitemap.io.SitemapReader;
import io.github.concurrentrecursion.sitemap.model.*;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    void testReadUrlsetSitemap() throws Exception{
        SitemapReader reader = new SitemapReader();
        URL mockUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("minimal-sitemap.xml"));
        Sitemap sitemap = reader.readSitemap(mockUrl);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
        UrlSetSitemap urlSetSitemap = (UrlSetSitemap) sitemap;
        assertEquals(1,urlSetSitemap.getUrls().size());
        Url site = urlSetSitemap.getUrls().get(0);
        assertEquals(0.5,site.getPriority());
        assertNotNull(site.getLocation());
        assertEquals(ChangeFrequency.MONTHLY,site.getChangeFrequency());
        assertEquals(0,OffsetDateTime.parse("2022-06-04T00:00:00.000Z").compareTo(site.getLastModifiedDate()));
    }

    @Test
    void testReadIndexSitemap() throws Exception{
        SitemapReader reader = new SitemapReader();
        URL mockUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("index-sitemap.xml"));
        Sitemap sitemap = reader.readSitemap(mockUrl);
        assertInstanceOf(IndexSitemap.class, sitemap);
        IndexSitemap indexSitemap = (IndexSitemap) sitemap;
        assertEquals(2,indexSitemap.getSitemapReferences().size());
        SitemapReference site = indexSitemap.getSitemapReferences().get(0);
        assertEquals("http://www.example.com/sitemap1.xml.gz",site.getLocation().toString());
        assertEquals(0,OffsetDateTime.parse("2004-10-01T18:23:17.000Z").compareTo(site.getLastModifiedDate()));
    }

}
