package io.github.concurrentrecursion.sitemap.util;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class provides utility methods for working with URLs.
 */
@UtilityClass
public class UrlUtil {

    /**
     * Converts the given location string to a URL.
     *
     * @param location the location string to be converted
     * @return the converted URL
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    @NotNull
    public URL convertToUrl(@NotNull String location) throws RuntimeMalformedUrlException {
        try {
            return new URI(location).toURL();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new RuntimeMalformedUrlException(e);
        }
    }

    /**
     * Resolves a filename to a URL relative to a base URL.
     *
     * @param url       the base URL
     * @param filename  the filename to be resolved
     * @return the resolved URL
     * @throws RuntimeMalformedUrlException if the resolved URL is malformed
     */
    @NotNull
    public URL resolve(@NotNull URL url, @NotNull String filename) throws RuntimeMalformedUrlException {
        try {
            return new URL(url, filename);
        } catch (MalformedURLException e) {
            throw new RuntimeMalformedUrlException(e);
        }
    }
}
