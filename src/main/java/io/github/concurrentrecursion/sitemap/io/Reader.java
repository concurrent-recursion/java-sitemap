package io.github.concurrentrecursion.sitemap.io;

import io.github.concurrentrecursion.robots.RobotsTxtReader;
import io.github.concurrentrecursion.sitemap.model.IndexSitemap;
import io.github.concurrentrecursion.sitemap.model.Sitemap;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;

/**
 * The Reader interface provides methods for reading sitemap files.
 */
public interface Reader {



    /**
     * Reads the given sitemap, and resolves it to a concrete UrlSetSitemap.<br>
     * If it's a IndexSitemap it will read all of the SitemapReferences and return them.
     * @param index The sitemap index
     * @return A list of UrlSetSitemap
     */
    List<UrlSetSitemap> readUrlSets(IndexSitemap index);

    /**
     * Reads a sitemap index file from the specified URL.
     *
     * @param url The URL of the sitemap index file.
     * @return The {@link IndexSitemap} object representing the sitemap index.
     */
    IndexSitemap readSitemapIndex(URL url);

    /**
     * Reads a sitemap index file from an inputstream.
     *
     * @param inputStream An inputstream of the sitemap index file.
     * @return The {@link IndexSitemap} object representing the sitemap index.
     */
    IndexSitemap readSitemapIndex(InputStream inputStream);

    /**
     * Reads a sitemap file from the specified URL and returns a {@link Sitemap} object representing the sitemap.<br>
     * This is useful for when you don't know if the given URL is a &lt;urlset&gt; or a &lt;sitemapindex&gt;
     *
     * @param url The URL of the sitemap file.
     * @return The {@link Sitemap} object representing the sitemap file.
     */
    Sitemap read(final URL url);


    /**
     * Reads a sitemap from the InputStream and returns a {@link Sitemap} object representing the sitemap.<br>
     * This is useful for when you don't know if the given URL is a &lt;urlset&gt; or a &lt;sitemapindex&gt;
     *
     * @param inputStream an inputstream to the sitemap
     * @return The {@link Sitemap} object representing the sitemap file.
     */
    Sitemap read(final InputStream inputStream);

    /**
     * Reads a sitemap file from the specified URL and returns a {@link UrlSetSitemap} object representing the sitemap.
     *
     * @param url The URL of the sitemap file.
     * @return The {@link UrlSetSitemap} object representing the sitemap file.
     */
    UrlSetSitemap readUrlSet(@NotNull final URL url);

    /**
     * Reads a sitemap file from the specified InputStream and returns a {@link UrlSetSitemap} object representing the sitemap.
     *
     * @param inputStream An inputstream to the urlset
     * @return The {@link UrlSetSitemap} object representing the sitemap file.
     */
    UrlSetSitemap readUrlSet(@NotNull final InputStream inputStream);
    /**
     * Sets the connection timeout value. This is the maximum time that the reader will attempt to establish a connection
     * before throwing an exception.
     *
     * @param connectionTimeout The connection timeout value
     * @return Returns this reader object to allow for chaining of calls.
     */
    Reader setConnectionTimeout(final Duration connectionTimeout);

    /**
     * Sets the read timeout value. This is the maximum time that the reader will read from the URL before throwing an exception.
     *
     * @param readTimeout The read timeout value
     * @return Returns this reader object to allow for chaining of calls.
     */
    Reader setReadTimeout(final Duration readTimeout);

    /**
     * Get the connection timeout value.
     *
     * @return The connection timeout value
     */
    Duration getConnectionTimeout();

    /**
     * Get the read timeout value.
     *
     * @return The read timeout value
     */
    Duration getReadTimeout();

}
