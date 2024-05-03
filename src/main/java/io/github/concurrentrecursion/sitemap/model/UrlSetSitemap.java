package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.sitemap.model.validation.MaxNewsConstraint;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  Represents the sitemap file, and contains a list of url elements.<br>
 *  In the sitemap schema, this represents a "urlset"
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "urlset")
@Data
@Accessors(chain = true)
@Valid
public class UrlSetSitemap implements Sitemap {
    /**
     * The filename for this file, this is used when the generator saves the UrlSet
     * @param filename the filename this Sitemap will be saved as
     * @return the filename
     */
    @XmlTransient
    private Path file = Paths.get("sitemap.xml");

    /**
     * The urls contained in this UrlSet
     * @param urls the urls
     * @return the urls
     */
    @XmlElement(required = true, name = "url")
    @Size(max = 50_000,groups = WriteValidation.class,message = "Maximum number of urls in a urlset is 50,000. Use IndexSitemap for more than 50,000 urls")
    @MaxNewsConstraint(groups = WriteValidation.class)
    private List<@Valid Url> urls = new ArrayList<>();

    /**
     * Adds a Url to the Sitemap.
     *
     * @param url the Url to be added
     * @return the updated Sitemap
     */
    public UrlSetSitemap addUrl(Url url){
        this.urls.add(url);
        return this;
    }


    /**
     * Build a UrlSetSitemap from the given urls
     * @param urls A stream of Urls
     * @return the urlsetsitemap
     */
    public static UrlSetSitemap fromUrls(Stream<Url> urls){
        return new UrlSetSitemap().setUrls(urls.collect(Collectors.toList()));
    }
    /**
     * Build a UrlSetSitemap from the given urls
     * @param urls the urls
     * @return the urlsetsitemap
     */
    public static UrlSetSitemap fromUrls(Url... urls){
        return fromUrls(Arrays.stream(urls));
    }

}
