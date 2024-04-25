package io.github.concurrentrecursion.sitemap;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.concurrentrecursion.exception.DataAccessException;
import io.github.concurrentrecursion.sitemap.io.SitemapReader;
import io.github.concurrentrecursion.sitemap.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(proxyMode = true)
@Slf4j
class ReaderTest {

    private static final URL SITEMAP_URL;

    static {
        try {
            SITEMAP_URL = URI.create("http://www.example.com/sitemap.xml").toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void testReadUrlsetSitemap() {
        stubFor(get("/sitemap.xml").withHost(equalTo("www.example.com")).willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8").withBodyFile("sitemaps/minimal-sitemap.xml")));
        SitemapReader reader = new SitemapReader();
        Sitemap sitemap = reader.read(SITEMAP_URL);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
        UrlSetSitemap urlSetSitemap = (UrlSetSitemap) sitemap;
        assertEquals(1, urlSetSitemap.getUrls().size());
        Url site = urlSetSitemap.getUrls().get(0);
        assertEquals(0.5, site.getPriority());
        assertNotNull(site.getLocation());
        assertEquals(ChangeFrequency.MONTHLY, site.getChangeFrequency());
        assertEquals(0, OffsetDateTime.parse("2022-06-04T00:00:00.000Z").compareTo(site.getLastModifiedDate()));
    }

    @Test
    void testReadUrlsetSitemapDirect() {
        stubFor(get("/sitemap.xml").withHost(equalTo("www.example.com")).willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8").withBodyFile("sitemaps/minimal-sitemap.xml")));

        SitemapReader reader = new SitemapReader();

        UrlSetSitemap urlSetSitemap = reader.readUrlSet(SITEMAP_URL);
        assertEquals(1, urlSetSitemap.getUrls().size());
        Url site = urlSetSitemap.getUrls().get(0);
        assertEquals(0.5, site.getPriority());
        assertNotNull(site.getLocation());
        assertEquals(ChangeFrequency.MONTHLY, site.getChangeFrequency());
        assertEquals(0, OffsetDateTime.parse("2022-06-04T00:00:00.000Z").compareTo(site.getLastModifiedDate()));
    }

    @Test
    void testReadIndexSitemap() throws Exception {
        stubFor(get("/sitemap.xml").withHost(equalTo("www.example.com")).willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8").withBodyFile("sitemaps/index-sitemap.xml")));

        SitemapReader reader = new SitemapReader();
        Sitemap sitemap = reader.read(SITEMAP_URL);
        assertInstanceOf(IndexSitemap.class, sitemap);
        IndexSitemap indexSitemap = (IndexSitemap) sitemap;
        assertEquals(2, indexSitemap.getSitemapReferences().size());
        SitemapReference site = indexSitemap.getSitemapReferences().get(0);
        assertEquals("http://www.example.com/sitemap1.xml.gz", site.getLocation().toString());
        assertEquals(0, OffsetDateTime.parse("2004-10-01T18:23:17.000Z").compareTo(site.getLastModifiedDate()));
    }

    @Test
    void testReadIndexSitemapDirect() throws Exception {
        stubFor(get("/sitemap.xml")
                .withHost(equalTo("www.example.com"))
                .willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8")
                        .withBodyFile("sitemaps/index-sitemap.xml")));

        SitemapReader reader = new SitemapReader();
        IndexSitemap indexSitemap = reader.readSitemapIndex(SITEMAP_URL);
        assertEquals(2, indexSitemap.getSitemapReferences().size());
        SitemapReference site = indexSitemap.getSitemapReferences().get(0);
        assertEquals("http://www.example.com/sitemap1.xml.gz", site.getLocation().toString());
        assertEquals(0, OffsetDateTime.parse("2004-10-01T18:23:17.000Z").compareTo(site.getLastModifiedDate()));
    }

    @Test
    void testReadTimeout() throws Exception {
        stubFor(get("/sitemap.xml")
                .withHost(equalTo("www.example.com"))

                .willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8")
                        .withFixedDelay(30_000)
                        .withBodyFile("sitemaps/index-sitemap.xml")));

        SitemapReader reader = new SitemapReader().setReadTimeout(Duration.ofSeconds(2));
        assertThrows(DataAccessException.class, () -> reader.readSitemapIndex(SITEMAP_URL));
    }

    @Test
    void testReadVideo() throws Exception {
        stubFor(get("/sitemap.xml").withHost(equalTo("www.example.com")).willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8").withBodyFile("sitemaps/video-sitemap.xml")));

        SitemapReader reader = new SitemapReader();
        Sitemap sitemap = reader.read(SITEMAP_URL);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
        UrlSetSitemap videoSitemap = (UrlSetSitemap) sitemap;
        assertEquals(3, videoSitemap.getUrls().size());
        assertEquals("Grilling steaks for summer", videoSitemap.getUrls().get(0).getVideos().get(0).getTitle());
        assertEquals("Grilling steaks for winter", videoSitemap.getUrls().get(0).getVideos().get(1).getTitle());
        assertEquals("Lizzi is painting the wall", videoSitemap.getUrls().get(1).getVideos().get(0).getTitle());
    }

    @Test
    void testReadDeprecated() {
        SitemapReader reader = new SitemapReader();
        UrlSetSitemap sitemap = reader.readUrlSet(Thread.currentThread().getContextClassLoader().getResourceAsStream("__files/sitemaps/image-sitemap-withdeprecated.xml"));
        assertEquals(2, sitemap.getUrls().size());
        assertEquals(2, sitemap.getUrls().get(0).getImages().size());
    }

    @Test
    void testReadNews() throws Exception {
        stubFor(get("/sitemap.xml").withHost(equalTo("www.example.com")).willReturn(ok().withHeader("Content-Type", "text/xml;charset=utf-8").withBodyFile("sitemaps/news-sitemap.xml")));

        SitemapReader reader = new SitemapReader();
        Sitemap sitemap = reader.read(SITEMAP_URL);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
        UrlSetSitemap videoSitemap = (UrlSetSitemap) sitemap;
        assertEquals(1, videoSitemap.getUrls().size());
        assertEquals("Companies A, B in Merger Talks", videoSitemap.getUrls().get(0).getNews().getTitle());
        assertEquals(0, OffsetDateTime.parse("2008-12-23T00:00:00.000Z").compareTo(videoSitemap.getUrls().get(0).getNews().getPublicationDate()));
    }

    @Test
    void testReadSitemapFromInputStream() throws Exception {
        SitemapReader reader = new SitemapReader();
        Sitemap sitemap;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("__files/sitemaps/minimal-sitemap.xml")) {
            sitemap = reader.read(is);
        }
        assertNotNull(sitemap);
        assertInstanceOf(UrlSetSitemap.class, sitemap);
    }

    @Test
    void testReadIndexSitemapFromInputStream() throws Exception {
        SitemapReader reader = new SitemapReader();
        Sitemap sitemap;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("__files/sitemaps/index-sitemap.xml")) {
            sitemap = reader.read(is);
        }
        assertNotNull(sitemap);
        assertInstanceOf(IndexSitemap.class, sitemap);
    }


}
