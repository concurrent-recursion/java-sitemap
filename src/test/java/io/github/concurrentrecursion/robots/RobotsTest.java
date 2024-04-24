package io.github.concurrentrecursion.robots;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@WireMockTest(proxyMode = true)
@Slf4j
class RobotsTest {

    @Test
    void testRobotsWithSingleSitemap() throws Exception {
        final String url = "http://www.example.com/robots.txt";
        stubFor(get("/robots.txt")
                .withHost(equalTo("www.example.com"))
                .willReturn(ok()
                        .withHeader("Content-Type", "text/plain")
                        .withBodyFile("robots/simple-robots.txt")));
        RobotsTxtReader robots = RobotsTxtReader.builder().build();
        assertEquals(1,robots.getSitemapUrls(URI.create(url).toURL()).count());
    }

    @Test
    void testRobotsWithMultipleSitemaps() throws Exception {
        final String url = "http://www.example.com/robots.txt";
        stubFor(get("/robots.txt").withHost(equalTo("www.example.com"))
                .willReturn(ok()
                        .withHeader("Content-Type", "text/plain")
                        .withBodyFile("robots/multi-sitemap-robots.txt")));
        RobotsTxtReader robots = RobotsTxtReader.builder().build();
        assertEquals(6,robots.getSitemapUrls(URI.create(url).toURL()).count());
    }

    @Test
    void testRobotsNotFound() throws Exception{
        stubFor(get("/robots.txt").withHost(equalTo("www.example.com")).willReturn(notFound()));
        RobotsTxtReader robots = RobotsTxtReader.builder().build();
        assertEquals(0, robots.getSitemapUrls(URI.create("http://www.example.com/robots.txt").toURL()).count());
    }

    @Test
    void testRobotsRedirect() throws Exception{
        final String url = "http://www.example.com/robots.txt";
        final String newUrl = "http://www.example.com/robots2.txt";

        stubFor(get("/robots.txt").withHost(equalTo("www.example.com"))
                .willReturn(temporaryRedirect(newUrl)));
        stubFor(get("/robots2.txt").withHost(equalTo("www.example.com"))
                .willReturn(ok()
                .withHeader("Content-Type", "text/plain")
                .withBodyFile("robots/multi-sitemap-robots.txt")));
        RobotsTxtReader robots = RobotsTxtReader.builder().build();
        assertNotEquals(0,robots.getSitemapUrls(URI.create(url).toURL()).count());
    }

    @Test
    void testNoSitemapsFound() throws Exception {
        final String url = "http://www.example.com/robots.txt";
        stubFor(get("/robots.txt").withHost(equalTo("www.example.com"))
                .willReturn(ok()
                        .withHeader("Content-Type", "text/plain")
                        .withBodyFile("robots/no-sitemap-robots.txt")));

        RobotsTxtReader robots = RobotsTxtReader.builder().build();
        assertEquals(0,robots.getSitemapUrls(URI.create(url).toURL()).count());
    }

}
