package io.github.concurrentrecursion.sitemap.io;

import io.github.concurrentrecursion.sitemap.model.IndexSitemap;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * The Generator interface provides methods to generate sitemap files and configure various settings.
 */
public interface Generator {
    /**
     * Converts a {@link IndexSitemap} object to a string representation.
     *
     * @param index the {@link IndexSitemap} object to convert
     * @return the string representation of the {@link IndexSitemap} object
     */
    String writeToString(IndexSitemap index);

    /**
     * Saves a {@link IndexSitemap} object to a file under a specified directory.
     *
     * @param index the {@link IndexSitemap} object to save
     * @param directory the directory path to save the file
     * @throws IOException in case of any I/O failure
     */
    void write(IndexSitemap index, Path directory) throws IOException;

    /**
     * Converts a {@link UrlSetSitemap} object to a string representation.
     *
     * @param urlSet the {@link UrlSetSitemap} to convert
     * @return the string representation of the {@link UrlSetSitemap} object
     */
    String writeToString(UrlSetSitemap urlSet);

    /**
     * Saves a {@link UrlSetSitemap} object to a file under a specified directory.
     *
     * @param urlSet the {@link UrlSetSitemap} to save
     * @param directory the directory path to save the file
     * @throws IOException in case of any I/O failure
     */
    void write(UrlSetSitemap urlSet, Path directory) throws IOException;

    /**
     * Sets whether the output should be pretty-printed.
     *
     * @param prettyPrint a flag indicating whether the output should be pretty-printed
     * @return the Generator instance
     */
    Generator setPrettyPrint(boolean prettyPrint);

    /**
     * Gets whether the output should be pretty-printed.
     *
     * @return a flag indicating whether the output should be pretty-printed
     */
    boolean getPrettyPrint();

    /**
     * Sets whether the output should be compressed using gzip.
     *
     * @param gzip a flag indicating whether the output should be compressed using gzip
     * @return the Generator instance
     */
    Generator useGzipCompression(boolean gzip);

    /**
     * Gets whether the output should be compressed using gzip.
     *
     * @return a flag indicating whether the output should be compressed using gzip
     */
    boolean getUseGzipCompression();

    /**
     * Sets the filename prefix for output files.
     *
     * @param filenamePrefix the filename prefix
     * @return the Generator instance
     */
    Generator setFilenamePrefix(String filenamePrefix);

    /**
     * Gets the filename prefix for output files.
     *
     * @return the filename prefix
     */
    String getFilenamePrefix();

    /**
     * Sets the base URL for all URLs in the sitemap.
     *
     * @param baseUrl the base URL as a string
     * @return the Generator instance
     */
    Generator setBaseUrl(String baseUrl);

    /**
     * Sets the base URL for all URLs in the sitemap.
     *
     * @param baseUrl the base URL as a URL object
     * @return the Generator instance
     */
    Generator setBaseUrl(URL baseUrl);

    /**
     * Gets the base URL for all URLs in the sitemap.
     *
     * @return the base URL
     */
    URL getBaseUrl();
}
