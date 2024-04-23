package io.github.concurrentrecursion.sitemap.model.google.video;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.adapters.UrlAdapter;
import io.github.concurrentrecursion.sitemap.model.validation.UrlLengthConstraint;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * Represents the uploader of a video
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class Uploader {
    /**
     * Specifies the URL of a web page with additional information about this uploader. This URL must be in the same domain
     * @param uploaderInfoUrl the url to additional information about the uploader
     * @return the uploader info url
     */
    @XmlAttribute(name="info")
    @XmlJavaTypeAdapter(UrlAdapter.class)
    @UrlLengthConstraint(groups = WriteValidation.class)
    private URL uploaderInfoUrl;

    /**
     * The video uploader name. The string value can be a maximum of 255 characters.
     * @param name The name of the uploader
     * @return the name of the uploader
     */
    @XmlValue
    @Size(max = 255, groups = WriteValidation.class)
    private String name;

    /**
     * Sets the URL of a web page with additional information about the uploader.
     * This URL must be in the same domain.
     *
     * @param uploaderInfoUrl the URL to the additional information about the uploader
     * @return the uploader instance with the updated uploaderInfoUrl
     */
    public Uploader setUploaderInfoUrl(final URL uploaderInfoUrl) {
        this.uploaderInfoUrl = uploaderInfoUrl;
        return this;
    }

    /**
     * Sets the URL of a web page with additional information about the uploader.
     * This URL must be in the same domain.
     *
     * @param info the URL string to the additional information about the uploader
     * @return the uploader instance with the updated uploaderInfoUrl
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Uploader setUploaderInfoUrl(final String info) throws RuntimeMalformedUrlException{
        this.uploaderInfoUrl = UrlUtil.convertToUrl(info);
        return this;
    }
}
