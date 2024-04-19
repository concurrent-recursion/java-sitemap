package io.github.concurrentrecursion.sitemap.model;

/**
 * Represents either a UrlSet sitemap or a IndexSitemap
 */
public interface Sitemap {
    /**
     * Get the filename of the sitemap file.
     *
     * @return The filename of the sitemap file.
     */
    String getFilename();
}
