package io.github.concurrentrecursion.robots;

import io.github.concurrentrecursion.sitemap.io.SitemapReader;
import io.github.concurrentrecursion.sitemap.model.IndexSitemap;
import io.github.concurrentrecursion.sitemap.model.Sitemap;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Represents a robots.txt file. Used to locate the sitemap URLs
 */
@Getter
@Slf4j
@Builder
public class RobotsTxtReader {
    private static final int MAX_REDIRECTS = 20;

    /**
     * The connectionTimeout variable represents the duration of the connection timeout.
     * By default, it is set to 3 seconds.
     * @param connectionTimeout the timeout duration
     * @return the timeout duration
     */
    @Builder.Default
    private Duration connectionTimeout = Duration.ofSeconds(3);
    /**
     * The duration for the read timeout.
     * <p>
     * This variable specifies the duration after which a read operation will timeout.
     * By default, the read timeout is set to 30 seconds.
     * </p>
     * @param readTimeout the read timeout
     * @return the read timeout
     */
    @Builder.Default
    private Duration readTimeout = Duration.ofSeconds(30);

    @Builder
    private RobotsTxtReader(@NotNull Duration connectionTimeout, @NotNull Duration readTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    private HttpResponse<Stream<String>> doRequest(final URI url) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(connectionTimeout).build();
        HttpRequest request = HttpRequest.newBuilder(url).timeout(readTimeout).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofLines());
    }

    /**
     * Retrieves a stream of URL objects representing the sitemap URLs found in the provided robots.txt URL.
     *
     * @param robotsTxtUrl The URL of the robots.txt file to parse for sitemap URLs.
     * @return A stream of URL objects representing the sitemap URLs found in the robots.txt file.
     * @throws IOException If an I/O error occurs while retrieving or parsing the robots.txt file.
     * @throws URISyntaxException If the provided URL is not a valid URI.
     * @throws InterruptedException If the thread is interrupted while waiting for the request to complete.
     */
    public Stream<URL> getSitemapUrls(@NotNull final URL robotsTxtUrl) throws IOException, URISyntaxException, InterruptedException {
        final Pattern pattern = Pattern.compile("^Sitemap:\\s?(.*)");

        return doRequest(robotsTxtUrl.toURI()).body()
                .filter(Objects::nonNull)
                .filter(line -> pattern.matcher(line).matches()).map(line -> {
                    Matcher matcher = pattern.matcher(line);
                    if (!matcher.find()) {
                        log.error("Invalid Sitemap URL: {}", line);
                        return null;
                    }
                    String sitemap = matcher.group(1).trim();
                    log.info("Found sitemap url: '{}'", sitemap);
                    return UrlUtil.convertToUrl(sitemap);
                }).filter(Objects::nonNull);
    }

    /**
     * Retrieves all sitemaps from a given robots.txt URL.
     *
     * @param robotsTxtUrl the URL of the robots.txt file
     * @return a stream of UrlSetSitemap objects representing the sitemaps
     * @throws IOException              if an I/O error occurs
     * @throws URISyntaxException       if the URI syntax is incorrect
     * @throws InterruptedException    if the thread is interrupted
     */
    public Stream<UrlSetSitemap> getAllSitemaps(@NotNull final URL robotsTxtUrl) throws IOException, URISyntaxException, InterruptedException {
        final SitemapReader reader = new SitemapReader();
        return getSitemapUrls(robotsTxtUrl).flatMap(surl -> {
            final Stream<UrlSetSitemap> sitemaps;
            Sitemap sitemap = reader.read(surl);
            if (sitemap instanceof UrlSetSitemap) {
                sitemaps = Stream.of((UrlSetSitemap) sitemap);
            } else {
                IndexSitemap index = (IndexSitemap) sitemap;
                sitemaps = index.getSitemapReferences().stream().map(refUrl ->
                        reader.readUrlSet(refUrl.getLocation())
                );
            }
            return sitemaps;
        });

    }

}
