package io.github.concurrentrecursion.sitemap.io;

import io.github.concurrentrecursion.robots.Robots;
import io.github.concurrentrecursion.sitemap.model.IndexSitemap;
import io.github.concurrentrecursion.sitemap.model.Sitemap;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * The Reader interface provides methods for reading sitemap files.
 */
public interface Reader {

    /**
     * Reads a sitemap file from the specified URL and returns a {@link Sitemap} object representing the sitemap.
     *
     * @param sitemapUrl The URL of the sitemap file.
     * @return The {@link Sitemap} object representing the sitemap file.
     */
    Sitemap readSitemap(URL sitemapUrl);

    /**
     * Reads the given sitemap, and resolves it to a concrete UrlSetSitemap.<br>
     * If it's a IndexSitemap it will read all of the SitemapReferences and return them.
     * @param index The sitemap index
     * @return A list of UrlSetSitemap
     */
    List<UrlSetSitemap> readUrlSets(IndexSitemap index);

    /**
     * Reads the sitemaps from the given Robots instance.
     *
     * @param robots The Robots instance containing the sitemap URLs.
     * @return A List of Sitemap objects representing the sitemaps.
     * @throws IOException If there is an error reading the sitemaps.
     */
    List<Sitemap> readSitemaps(Robots robots) throws IOException;

    /**
     * Reads a sitemap index file from the specified URL.
     *
     * @param url The URL of the sitemap index file.
     * @return The {@link IndexSitemap} object representing the sitemap index.
     */
    IndexSitemap readSitemapIndex(URL url);

    /**
     * Reads a sitemap file from the specified URL and returns a {@link Sitemap} object representing the sitemap.<br>
     * This is useful for when you don't know if the given URL is a &lt;urlset&gt; or a &lt;sitemapindex&gt;
     *
     * @param url The URL of the sitemap file.
     * @return The {@link Sitemap} object representing the sitemap file.
     */
    Sitemap read(final URL url);

    /**
     * Reads a sitemap file from the specified URL and returns a {@link UrlSetSitemap} object representing the sitemap.
     *
     * @param url The URL of the sitemap file.
     * @return The {@link UrlSetSitemap} object representing the sitemap file.
     */
    UrlSetSitemap readUrlSet(final URL url);
    /**
     * Sets the connection timeout value. This is the maximum time that the reader will attempt to establish a connection
     * before throwing an exception.
     *
     * @param connectionTimeout The connection timeout value in milliseconds.
     * @return Returns this reader object to allow for chaining of calls.
     */
    Reader setConnectionTimeout(final int connectionTimeout);

    /**
     * Sets the read timeout value. This is the maximum time that the reader will read from the URL before throwing an exception.
     *
     * @param readTimeout The read timeout value in milliseconds.
     * @return Returns this reader object to allow for chaining of calls.
     */
    Reader setReadTimeout(final int readTimeout);

    /**
     * Get the connection timeout value.
     *
     * @return The connection timeout value in milliseconds.
     */
    int getConnectionTimeout();

    /**
     * Get the read timeout value.
     *
     * @return The read timeout value in milliseconds.
     */
    int getReadTimeout();

}
