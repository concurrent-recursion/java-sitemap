package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.adapters.OffsetDateTimeAdapter;
import io.github.concurrentrecursion.sitemap.adapters.UrlAdapter;
import io.github.concurrentrecursion.sitemap.model.google.image.Image;
import io.github.concurrentrecursion.sitemap.model.google.news.News;
import io.github.concurrentrecursion.sitemap.model.google.video.Video;
import io.github.concurrentrecursion.sitemap.model.validation.UrlLengthConstraint;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import io.github.concurrentrecursion.sitemap.model.xhtml.Link;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Parent tag for each URL entry.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "location",
        "lastModifiedDate",
        "changeFrequency",
        "priority",
        "images",
        "links",
        "news",
        "videos"
})
@Data
@Accessors(chain = true)
public class Url {
    /**
     * URL of the page. This URL must begin with the protocol (such as http) and end with a trailing slash,
     * if your web server requires it. This value must be less than 2,048 characters.
     * @param location the URL to add to the sitemap
     * @return the URL
     */
    @XmlElement(required = true, name = "loc")
    @XmlSchemaType(name = "anyURI")
    @XmlJavaTypeAdapter(UrlAdapter.class)
    @UrlLengthConstraint(groups = WriteValidation.class)
    private URL location;

    /**
     * The date of last modification of the page.<br>
     * Note that the date must be set to the date the linked page was last modified, not when the sitemap is generated.<br>
     * Note also that this tag is separate from the If-Modified-Since (304) header the server can return, and search
     * engines may use the information from both sources differently.
     *
     * @param lastModifiedDate the last modified date
     * @return the last modified date
     */
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    @XmlElement(name = "lastmod")
    private OffsetDateTime lastModifiedDate;

    /**
     * How frequently the page is likely to change. This value provides general information to search engines and may
     * not correlate exactly to how often they crawl the page.<br>
     * Please note that the value of this tag is considered a hint and not a command. Even though search engine
     * crawlers may consider this information when making decisions, they may crawl pages marked "hourly" less
     * frequently than that, and they may crawl pages marked "yearly" more frequently than that. Crawlers may
     * periodically crawl pages marked "never" so that they can handle unexpected changes to those pages.
     *
     * @param changeFrequency the frequency
     * @return the frequency
     */
    @XmlSchemaType(name = "string")
    @XmlElement(name = "changefreq")
    private ChangeFrequency changeFrequency;

    /**
     * The priority of this URL relative to other URLs on your site. Valid values range from 0.0 to 1.0. This value
     * does not affect how your pages are compared to pages on other sites—it only lets the search engines know which
     * pages you deem most important for the crawlers.<br>
     * The default priority of a page is 0.5.<br>
     * Please note that the priority you assign to a page is not likely to influence the position of your URLs in a
     * search engine's result pages. Search engines may use this information when selecting between URLs on the same
     * site, so you can use this tag to increase the likelihood that your most important pages are present in a search
     * index.<br>
     * Also, please note that assigning a high priority to all the URLs on your site is not likely to help you.
     * Since the priority is relative, it is only used to select between URLs on your site.
     *
     * @param priority the priority number, 0.0 to 1.0
     * @return the priority
     *
     */
    @DecimalMin(value = "0.0", groups = WriteValidation.class)
    @DecimalMax(value = "1.0", groups = WriteValidation.class)
    private Double priority;

    /**
     * Images for this site that should be indexed by a crawler
     *
     * @param images the images to index
     * @return the images
     */
    @XmlElement(name = "image", namespace = "http://www.google.com/schemas/sitemap-image/1.1")
    @Size(max = 1_000, groups = WriteValidation.class)
    private List<@Valid Image> images = new ArrayList<>();

    /**
     * A list of links of for alternate versions of this site
     * @param links a list of links
     * @return the list of links
     */
    @XmlElement(name = "link", namespace = "http://www.w3.org/1999/xhtml")
    private List<@Valid Link> links = new ArrayList<>();

    /**
     * News information about this site. Should be no more than 2 days old.
     * @param news news metadata
     * @return the news metadata
     */
    @XmlElement(name = "news", namespace = "http://www.google.com/schemas/sitemap-news/0.9")
    @Valid
    private News news;

    /**
     * Videos for this site that should be indexed by a crawler
     *
     * @param videos the images to index
     * @return the videos
     */
    @XmlElement(name = "video", namespace = "http://www.google.com/schemas/sitemap-video/1.1")
    private List<@Valid Video> videos = new ArrayList<>();

    /**
     * Create a new Url
     */
    Url() {
    }

    /**
     * Create a new Url with the given URL
     * @param location the location of the page
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Url(@NotNull String location) throws RuntimeMalformedUrlException {
        this.location = UrlUtil.convertToUrl(location);
    }

    /**
     * Create a new Url with the given URL
     * @param location the location of the page
     */
    public Url(@NotNull URL location) {
        this.location = location;
    }

    /**
     * Add the given image to this Url entry
     * @param image the image to add
     * @return this Url with the given image added to it
     */
    public Url addImage(@NotNull Image image) {
        this.images.add(image);
        return this;
    }

    /**
     * Add the given link to this Url entry
     * @param link the link to add
     * @return this Url with the given link added to it
     */
    public Url addLink(@NotNull Link link) {
        this.links.add(link);
        return this;
    }

    /**
     * Add the given video to this Url entry
     * @param video the video to add
     * @return this Url with the given video added to it
     */
    public Url addVideo(@NotNull Video video) {
        this.videos.add(video);
        return this;
    }



}
