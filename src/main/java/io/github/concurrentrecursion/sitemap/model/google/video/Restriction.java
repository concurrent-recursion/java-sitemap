package io.github.concurrentrecursion.sitemap.model.google.video;

import io.github.concurrentrecursion.sitemap.adapters.SpaceDelimitedStringListAdapter;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restriction on a video for specific country or countries
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class Restriction {
    /**
     * Creates a restriction on a video
     */
    public Restriction(){
    }

    /**
     * Creates a Restriction object representing a restriction on a video for specific country or countries.
     *
     * @param relationship the relationship of the platform or restriction to the given values
     * @param countries    a list of country codes in ISO 3166 format
     */
    public Restriction(@NotNull Relationship relationship,@NotNull @NotEmpty List<String> countries){
        this.relationship = relationship;
        this.countries = List.copyOf(countries);
    }

    /**
     * Whether the video is allowed or denied in search results in the specified countries
     * @param relationship the relationship
     * @return the relationship
     */
    @XmlAttribute
    @NotNull(groups = WriteValidation.class)
    private Relationship relationship;
    /**
     * A space-delimited list of country codes in ISO 3166 format
     * @param countries the list of country codes
     * @return the list of country codes
     */
    @XmlValue
    @XmlJavaTypeAdapter(SpaceDelimitedStringListAdapter.class)
    @NotEmpty(groups = WriteValidation.class)
    @NotNull(groups = WriteValidation.class)
    private List<String> countries = new ArrayList<>();


}
