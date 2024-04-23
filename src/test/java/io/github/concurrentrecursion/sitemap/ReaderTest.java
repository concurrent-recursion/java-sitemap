package io.github.concurrentrecursion.sitemap;

import io.github.concurrentrecursion.UrlMockUtil;
import io.github.concurrentrecursion.sitemap.io.SitemapReader;
import io.github.concurrentrecursion.sitemap.model.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    void testReadUrlsetSitemap() throws Exception{
        SitemapReader reader = new SitemapReader();
        URL mockUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("minimal-sitemap.xml"));
        Sitemap sitemap = reader.read(mockUrl);
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
        Sitemap sitemap = reader.read(mockUrl);
        assertInstanceOf(IndexSitemap.class, sitemap);
        IndexSitemap indexSitemap = (IndexSitemap) sitemap;
        assertEquals(2,indexSitemap.getSitemapReferences().size());
        SitemapReference site = indexSitemap.getSitemapReferences().get(0);
        assertEquals("http://www.example.com/sitemap1.xml.gz",site.getLocation().toString());
        assertEquals(0,OffsetDateTime.parse("2004-10-01T18:23:17.000Z").compareTo(site.getLastModifiedDate()));
    }

    @Test
    void testReadVideo() throws Exception{
        SitemapReader reader = new SitemapReader();
        URL mockUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("video-sitemap.xml"));
        Sitemap sitemap = reader.read(mockUrl);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
        UrlSetSitemap videoSitemap = (UrlSetSitemap) sitemap;
        assertEquals(3, videoSitemap.getUrls().size());
        assertEquals("Grilling steaks for summer", videoSitemap.getUrls().get(0).getVideos().get(0).getTitle());
        assertEquals("Grilling steaks for winter", videoSitemap.getUrls().get(0).getVideos().get(1).getTitle());
        assertEquals("Lizzi is painting the wall", videoSitemap.getUrls().get(1).getVideos().get(0).getTitle());
    }

    @Test
    void testReadDeprecated(){
        SitemapReader reader = new SitemapReader();
        UrlSetSitemap sitemap = reader.readUrlSet(Thread.currentThread().getContextClassLoader().getResourceAsStream("image-sitemap-withdeprecated.xml"));
        assertEquals(2,sitemap.getUrls().size());
        assertEquals(2,sitemap.getUrls().get(0).getImages().size());
    }

    @Test
    void testReadNews() throws Exception{
        SitemapReader reader = new SitemapReader();
        URL mockUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("news-sitemap.xml"));
        Sitemap sitemap = reader.read(mockUrl);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
        UrlSetSitemap videoSitemap = (UrlSetSitemap) sitemap;
        assertEquals(1, videoSitemap.getUrls().size());
        assertEquals("Companies A, B in Merger Talks", videoSitemap.getUrls().get(0).getNews().getTitle());
        assertEquals(0,OffsetDateTime.parse("2008-12-23T00:00:00.000Z").compareTo( videoSitemap.getUrls().get(0).getNews().getPublicationDate()));
    }

    @Test
    void testReadSitemapFromInputStream() throws Exception{
        SitemapReader reader = new SitemapReader();
        Sitemap sitemap = null;
        try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("minimal-sitemap.xml")) {
            sitemap = reader.read(is);
        }
        assertNotNull(sitemap);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
    }

    @Test
    void testReadIndexSitemapFromInputStream() throws Exception{
        SitemapReader reader = new SitemapReader();
        Sitemap sitemap = null;
        try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index-sitemap.xml")) {
            sitemap = reader.read(is);
        }
        assertNotNull(sitemap);
        assertInstanceOf(IndexSitemap.class, sitemap);
    }
}
