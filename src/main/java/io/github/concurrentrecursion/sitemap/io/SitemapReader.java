package io.github.concurrentrecursion.sitemap.io;

import io.github.concurrentrecursion.exception.DataAccessException;
import io.github.concurrentrecursion.robots.RobotsTxtReader;
import io.github.concurrentrecursion.sitemap.model.IndexSitemap;
import io.github.concurrentrecursion.sitemap.model.Sitemap;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The SitemapReader class implements the Reader interface and provides methods for reading sitemap files.
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class SitemapReader implements Reader {
    private static final JAXBContext JAXB_CONTEXT;

    private Duration connectionTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(5);

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(UrlSetSitemap.class, IndexSitemap.class);
        } catch (JAXBException e) {
            throw new DataAccessException(e);
        }
    }

    private HttpResponse<InputStream> getConnection(URL url) throws IOException, URISyntaxException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(connectionTimeout).build();
        HttpRequest request = HttpRequest.newBuilder(url.toURI()).timeout(readTimeout).header("User-Agent", "SitemapReader").build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
    }



    private Sitemap unmarshal(final InputStream inputStream){
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (Sitemap) unmarshaller.unmarshal(new StreamSource(inputStream));
        }catch (JAXBException e){
            throw new DataAccessException(e);
        }
    }

    private <T> T unmarshal(final InputStream inputStream, final Class<T> clazz){
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return unmarshaller.unmarshal(new StreamSource(inputStream), clazz).getValue();
        }catch (JAXBException e){
            throw new DataAccessException(e);
        }
    }
    private <T> T unmarshal(URL url, Class<T> clazz){
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            try (InputStream inputStream = getConnection(url).body()) {
                return unmarshaller.unmarshal(new StreamSource(inputStream), clazz).getValue();
            }
        }catch (IOException | JAXBException | URISyntaxException e){
            throw new DataAccessException(e);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new DataAccessException(e);
        }
    }

    private Sitemap unmarshal(URL url){
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            try (InputStream inputStream = getConnection(url).body()) {
                return (Sitemap) unmarshaller.unmarshal(new StreamSource(inputStream));
            }
        }catch (IOException | JAXBException | URISyntaxException e){
            throw new DataAccessException(e);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new DataAccessException(e);
        }
    }

    @Override
    public IndexSitemap readSitemapIndex(final URL url) {
        return unmarshal(url, IndexSitemap.class);
    }

    @Override
    public IndexSitemap readSitemapIndex(InputStream inputStream) {
        return unmarshal(inputStream,IndexSitemap.class);
    }

    @Override
    public Sitemap read(final URL url) {
        return unmarshal(url);
    }

    @Override
    public Sitemap read(InputStream inputStream) {
        return unmarshal(inputStream);
    }

    @Override
    public UrlSetSitemap readUrlSet(final URL url) {
        return unmarshal(url, UrlSetSitemap.class);
    }

    @Override
    public UrlSetSitemap readUrlSet(InputStream inputStream) {
        return unmarshal(inputStream, UrlSetSitemap.class);
    }

    @Override
    public List<UrlSetSitemap> readUrlSets(IndexSitemap index) {
        return index.getSitemapReferences().stream().map(ref -> readUrlSet(ref.getLocation())).collect(Collectors.toList());
    }
}
