package io.github.concurrentrecursion.sitemap.model.google.image;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.adapters.UrlAdapter;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * Represents an image with a URL location.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain=true)
public class Image {
    /**
     * The URL of the image
     * @param location The image url
     * @return the image url
     */
    @XmlElement(name = "loc",required = true)
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL location;

    /**
     * Create a new Image
     */
    public Image (){}

    /**
     * Create a new Image with the given location
     * @param location the image url
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Image(String location) throws RuntimeMalformedUrlException {
        this.location = UrlUtil.convertToUrl(location);
    }

    /**
     * Create a new Image with the given location
     * @param location the image url
     */
    public Image(URL location){
        this.location = location;
    }
}
