package io.github.concurrentrecursion.sitemap.model;

import java.nio.file.Path;

/**
 * Represents either a UrlSet sitemap or a IndexSitemap
 */
public interface Sitemap {
    /**
     * Get the location of the sitemap file.
     *
     * @return The location of the sitemap file.
     */
    Path getFile();
}
