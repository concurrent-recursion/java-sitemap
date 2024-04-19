package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.sitemap.util.Streams;
import jakarta.xml.bind.annotation.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sitemapindex represents a sitemap index file. It is a container for a list of SitemapReference
 * objects.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sitemapindex")
@Data
@Accessors(chain = true)
public class IndexSitemap implements Sitemap {
    private static final int MAX_URLS = 50_000;

    /**
     * Create empty sitemapindex
     */
    public IndexSitemap(){}

    /**
     * Create sitemap index with the given base uri for the sitemap files
     * @param sitemapDirectoryUrl The URL for each SitemapReference object to be referenced by in the sitemap index
     * @param siteUrls The URLs to include in the sitemap
     */
    public IndexSitemap(URL sitemapDirectoryUrl, List<Url> siteUrls) {
        this(sitemapDirectoryUrl,siteUrls.stream());
    }

    /**
     * Create sitemap index with the given base uri for the sitemap files
     * @param sitemapDirectoryUrl The URL for each SitemapReference object to be referenced by in the sitemap index
     * @param siteUrls The URLs to include in the sitemap
     */
    public IndexSitemap(URL sitemapDirectoryUrl, Stream<Url> siteUrls) {
        AtomicInteger counter = new AtomicInteger();
        List<UrlSetSitemap> urlSets = Streams.batchStream(siteUrls,MAX_URLS).map(batch -> {
            UrlSetSitemap sitemap = new UrlSetSitemap().setFilename("sitemap-"+ counter.incrementAndGet() + ".xml");
            batch.forEach(url -> sitemap.getUrls().add(url));
            return sitemap;
        }).collect(Collectors.toList());
        this.sitemapReferences = urlSets.stream().map(urlSet -> new SitemapReference().setSitemap(urlSet).setLocation(sitemapDirectoryUrl)).collect(Collectors.toList());
    }

    /**
     * The filesystem filename for the index file
     * @param filename the filename
     * @return the filename
     */
    @XmlTransient
    private String filename = "sitemap-index.xml";

    /**
     * The SitemapReferences that make up this sitemap index
     * @param sitemapReferences the sitemap references
     * @return the sitemap references
     */
    @XmlElement(required = true, name = "sitemap")
    private List<SitemapReference> sitemapReferences = new ArrayList<>();

    /**
     * Returns a list of all the {@link UrlSetSitemap} objects in the {@link IndexSitemap}.
     *
     * @return A list of all the {@link UrlSetSitemap} objects in the {@link IndexSitemap}.
     */
    public List<UrlSetSitemap> getAllUrlSets(){
        return sitemapReferences.stream().map(SitemapReference::getSitemap).collect(Collectors.toList());
    }

    /**
     * Add the UrlSet to this index
     * @param urlSet The URLSetIndex to add
     * @param location The Url where the UrlSetIndex will be available
     * @param lastModified The lastmodifiedDate of the UrlSetIndex
     * @return this IndexSitemap with the UrlSet added
     */
    public IndexSitemap addUrlSet(UrlSetSitemap urlSet, URL location, OffsetDateTime lastModified) {
        this.sitemapReferences.add(new SitemapReference().setSitemap(urlSet).setLocation(location).setLastModifiedDate(lastModified));
        return this;
    }
}