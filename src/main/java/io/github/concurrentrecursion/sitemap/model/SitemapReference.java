package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.adapters.OffsetDateTimeAdapter;
import io.github.concurrentrecursion.sitemap.adapters.UrlAdapter;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;
import java.time.OffsetDateTime;

/**
 * Represents a sitemap entry contained in a sitemapindex file.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "location",
    "lastModifiedDate"
})
@Data
@Accessors(chain = true)
public class SitemapReference {

    /**
     * A reference to the UrlSet that this file is associated with. This is not automatically populated.
     * @param sitemap the actual sitemap containing all of the urls in this sitemap reference
     * @return the sitemap
     */
    @XmlTransient
    private UrlSetSitemap sitemap;

    /**
     * The location of the UrlSet file
     * @param location the URL location of the sitemap
     * @return the URL
     */
    @XmlElement(required = true, name = "loc")
    @XmlSchemaType(name = "anyURI")
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL location;

    /**
     * The date of last modification of the UrlSet file
     * @param lastModifiedDate the date/time the urlset was last modified
     * @return the datetime
     */
    @XmlElement(name = "lastmod")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime lastModifiedDate;

    /**
     * Set the URL for the sitemap file
     * @param location the URL where the sitemap xml file is located
     * @return the sitemap URL
     */
    public SitemapReference setLocation(URL location){
        this.location = location;
        return this;
    }

    /**
     * Set the URL for the sitemap file
     * @param location the URL where the sitemap xml file is located
     * @return the sitemap URL
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public SitemapReference setLocation(String location) throws RuntimeMalformedUrlException {
        this.location = UrlUtil.convertToUrl(location);
        return this;
    }

}
