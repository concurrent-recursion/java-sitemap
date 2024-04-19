package io.github.concurrentrecursion.sitemap.model.google.news;

import io.github.concurrentrecursion.sitemap.adapters.OffsetDateTimeAdapter;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/**
 * Represents a reference to a news article
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class News {
    /**
     * The publication that published the news article
     * @param publication the publication
     * @return the publication
     */
    @XmlElement(required = true)
    @Valid
    private Publication publication;

    /**
     * The article publication date
     * @param publicationDate the date of publication
     * @return the date of publication
     */
    @XmlElement(name = "publication_date",required = true)
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime publicationDate;

    /**
     * The title of the news article.
     * @param title the title of the news article
     * @return the title of the news article
     */
    @XmlElement(required = true)
    private String title;
}
