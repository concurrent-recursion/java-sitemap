package io.github.concurrentrecursion.sitemap.model.xhtml;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.adapters.UrlAdapter;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * Represents a link with a relationship, language, and URL.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class Link {
    /**
     * Represents the "rel" attribute of a link. Default value is alternate
     * @param relationship the relationship of the link to the main url
     * @return the relationship
     */
    @XmlAttribute(name = "rel")
    private String relationship = "alternate";
    /**
     * The ISO 639 language code for the linked page
     * @param language A ISO 639 language code
     * @return the language code
     */
    @XmlAttribute(name = "hreflang")
    private String language;
    /**
     * The link
     * @return the link
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL href;

    /**
     * Create a link with the given language and href
     * @param language The ISO 639 language code
     * @param href The URL to the alternate site
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Link(String language, String href) throws RuntimeMalformedUrlException {
        this.href = UrlUtil.convertToUrl(href);
        this.language = language;
    }
    /**
     * Create a link with the given language and href
     * @param language The ISO 639 language code
     * @param href The URL to the alternate site
     */
    public Link(String language,URL href){
        this.href = href;
        this.language = language;
    }

    /**
     * Create a new empty Link
     */
    public Link(){}

    /**
     * Set the href of the link
     * @param href the href to set
     * @return the Link with the given href set
     */
    public Link setHref(final URL href) {
        this.href = href;
        return this;
    }

    /**
     * Set the href of the link
     * @param href the href to set
     * @return the Link with the given href set
     */
    public Link setHref(final String href) {
        this.href = UrlUtil.convertToUrl(href);
        return this;
    }
}
