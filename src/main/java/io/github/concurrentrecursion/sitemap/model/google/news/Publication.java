package io.github.concurrentrecursion.sitemap.model.google.news;

import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The publication that published a news article
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class Publication {
    /**
     * The name of the news publication. It must exactly match the name as it appears on your articles on
     * news.google.com, omitting anything in parentheses.
     * @param name the name of the publication
     * @return the name of the publication
     */
    @XmlElement(required = true)
    private String name;
    /**
     * The language of your publication. Use an ISO 639 language code (two or three letters).
     * @param language the ISO 639 language code. Must be one of: [ zh-ch, zh-tw, or a 2 to 3 character code]
     * @return the code
     */
    @XmlElement(required = true)
    @Pattern(regexp = "^(zh-cn|zh-tw|([a-z]{2,3}))$", groups = WriteValidation.class)
    private String language;

    /**
     * Represents a publication that publishes a news article.
     */
    public Publication(){}

    /**
     * Create a publication with the given name and language code
     * @param name The publication name
     * @param language The publication language
     */
    public Publication(String name, String language) {
        this.name = name;
        this.language = language;
    }
}
