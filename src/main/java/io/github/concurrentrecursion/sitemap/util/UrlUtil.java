package io.github.concurrentrecursion.sitemap.util;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URI;
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
    public URL convertToUrl(String location) throws RuntimeMalformedUrlException{
        try {
            return URI.create(location).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeMalformedUrlException(e);
        }
    }
}
