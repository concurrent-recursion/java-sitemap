package io.github.concurrentrecursion.robots;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a robots.txt file. Used to locate the sitemap URLs
 */
@Getter
@Slf4j
public class Robots {
    private static final int MAX_REDIRECTS = 20;
    /**
     * The domain of the robots.txt file
     * @return the domain
     */
    private final String domain;

    /**
     * The list of URLs representing the sitemaps defined in the robots.txt file
     * @return the list of urls
     */
    private final List<URL> sitemapUrls;


    private Robots(String domain, List<URL> sitemapUrls) {
        this.domain = domain;
        this.sitemapUrls = Collections.unmodifiableList(sitemapUrls);
    }


    private static Robots load(@NotNull final URL robotsTxtUrl, final int connectionTimeout, final int readTimeout, int numRedirects) throws IOException {
        if(numRedirects > MAX_REDIRECTS){
            throw new IOException("Too many redirects: " + numRedirects);
        }
        HttpURLConnection connection = (HttpURLConnection) robotsTxtUrl.openConnection();
        connection.setRequestProperty("User-Agent", "SitemapParser");
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        final int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectUrl = connection.getHeaderField("Location");
            URL newUrl = URI.create(redirectUrl).toURL();
            log.debug("Received {} redirect to {}", responseCode, newUrl);
            return load(newUrl, connectionTimeout, readTimeout, numRedirects + 1);
        }
        if(responseCode < 200 || responseCode >= 300) {
            throw new IOException("Invalid response code " + responseCode + " for " + robotsTxtUrl);
        }
        List<URL> urls = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            Pattern pattern = Pattern.compile("^Sitemap:\\s?(.*)");
            while((line = reader.readLine()) != null){
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()){
                    String sitemap = matcher.group(1).trim();
                    log.info("Found sitemap url: '{}'", sitemap);
                    urls.add(URI.create(sitemap).toURL());
                }
            }
        }
        return new Robots(robotsTxtUrl.toString(), urls);
    }

    /**
     * Loads a robots.txt file from the specified URL and extracts the sitemap URLs.
     *
     * @param robotsTxtUrl      the URL of the robots.txt file
     * @param connectionTimeout the connection timeout in milliseconds
     * @param readTimeout       the read timeout in milliseconds
     * @return the Robots object containing the domain and sitemap URLs
     * @throws IOException if an IO error occurs or too many redirects are encountered
     */
    public static Robots load(@NotNull final URL robotsTxtUrl, final int connectionTimeout, final int readTimeout) throws IOException {
        return load(robotsTxtUrl, connectionTimeout, readTimeout, 0);
    }

}
