package io.github.concurrentrecursion.sitemap.io;

import io.github.concurrentrecursion.exception.DataAccessException;
import io.github.concurrentrecursion.robots.Robots;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The SitemapReader class implements the Reader interface and provides methods for reading sitemap files.
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class SitemapReader implements Reader{
    private static final JAXBContext JAXB_CONTEXT;

    private int connectionTimeout;
    private int readTimeout;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(UrlSetSitemap.class, IndexSitemap.class);
        } catch (JAXBException e) {
            throw new DataAccessException(e);
        }
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(connectionTimeout);
        urlConnection.setReadTimeout(readTimeout);
        urlConnection.setRequestProperty("User-Agent", "SitemapReader");
        return urlConnection;
    }


    public List<Sitemap> readSitemaps(Robots robots) throws IOException {
        return robots.getSitemapUrls().stream().map(this::readSitemap).collect(Collectors.toList());
    }

    private <T> T unmarshal(URL url, Class<T> clazz){
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            URLConnection urlConnection = getConnection(url);
            try (InputStream inputStream = urlConnection.getInputStream()) {
                return unmarshaller.unmarshal(new StreamSource(inputStream), clazz).getValue();
            }
        }catch (IOException | JAXBException e){
            throw new DataAccessException(e);
        }
    }

    private Object unmarshal(URL url){
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            URLConnection urlConnection = getConnection(url);
            try (InputStream inputStream = urlConnection.getInputStream()) {
                return unmarshaller.unmarshal(new StreamSource(inputStream));
            }
        }catch (IOException | JAXBException e){
            throw new DataAccessException(e);
        }
    }

    @Override
    public Sitemap readSitemap(URL sitemapUrl){
        return (Sitemap) unmarshal(sitemapUrl);
    }

    @Override
    public IndexSitemap readSitemapIndex(final URL url) {
        return unmarshal(url, IndexSitemap.class);
    }

    @Override
    public Sitemap read(final URL url) {
        return unmarshal(url, Sitemap.class);
    }

    @Override
    public UrlSetSitemap readUrlSet(final URL url) {
        return unmarshal(url, UrlSetSitemap.class);
    }

    @Override
    public List<UrlSetSitemap> readUrlSets(IndexSitemap index) {
        return index.getSitemapReferences().stream().map(ref -> readUrlSet(ref.getLocation())).collect(Collectors.toList());
    }
}
