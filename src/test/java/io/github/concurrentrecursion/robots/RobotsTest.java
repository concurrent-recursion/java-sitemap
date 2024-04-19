package io.github.concurrentrecursion.robots;

import io.github.concurrentrecursion.UrlMockUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RobotsTest {

    @Test
    void testRobotsWithSingleSitemap() throws Exception {
        URL robotsUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("simple-robots.txt"));

        Robots robots = Robots.load(robotsUrl,100,1000);
        assertEquals(1,robots.getSitemapUrls().size());
    }

    @Test
    void testRobotsWithMultipleSitemaps() throws Exception {
        URL robotsUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("multi-sitemap-robots.txt"));

        Robots robots = Robots.load(robotsUrl,100,1000);
        assertEquals(6,robots.getSitemapUrls().size());
    }

    @Test
    void testRobotsNotFound() throws Exception{
        URL robotsUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_NOT_FOUND,() -> null);
        assertThrows(IOException.class,()-> Robots.load(robotsUrl,100,1000));
    }

    @Disabled("testRobotsRedirect() needs to be manually tested with a real url because its hard to mock")
    @Test
    void testRobotsRedirect() throws Exception{
        assertFalse(Robots.load(URI.create("some url that redirects").toURL(),100,1000).getSitemapUrls().isEmpty());
    }

    @Test
    void testNoSitemapsFound() throws Exception {
        URL robotsUrl = UrlMockUtil.mockUrl(HttpURLConnection.HTTP_OK,() -> Thread.currentThread().getContextClassLoader().getResourceAsStream("no-sitemap-robots.txt"));

        Robots robots = Robots.load(robotsUrl,100,1000);
        assertEquals(0,robots.getSitemapUrls().size());
    }

}
