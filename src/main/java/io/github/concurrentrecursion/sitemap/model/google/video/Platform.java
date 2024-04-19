package io.github.concurrentrecursion.sitemap.model.google.video;

import io.github.concurrentrecursion.sitemap.adapters.SpaceDelimitedPlatformTypeAdapter;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Represents a platform for the video
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class Platform {

    /**
     * Create a platform with the given relationship and platforms
     * @param relationship the relationship of the video to the platforms
     * @param platforms the platforms
     */
    public Platform(@NotNull Relationship relationship,@NotNull @NotEmpty List<Type> platforms){
        this.relationship = relationship;
        this.platforms = List.copyOf(new LinkedHashSet<>(platforms));
    }
    /**
     * The relationship with the platform
     * @param relationship Whether to allow or deny access to the given platform(s)
     * @return the relationship
     */
    @XmlAttribute
    @NotNull(groups = WriteValidation.class)
    private Relationship relationship;

    /**
     * A list of platform types
     * @param platforms A list of platform types
     * @return A list of platform type
     */
    @XmlValue
    @NotEmpty(groups = WriteValidation.class)
    @NotNull(groups = WriteValidation.class)
    @XmlJavaTypeAdapter(SpaceDelimitedPlatformTypeAdapter.class)
    private List<Type> platforms;

    /**
     * The platforms the relationship applies to
     */
    @XmlEnum
    @Getter
    public enum Type {
        /**
         * Computer browsers on desktops and laptops.
         */
        @XmlEnumValue("web")
        WEB("web"),
        /**
         * Mobile browsers, such as those on cellular phones or tablets.
         */
        @XmlEnumValue("mobile")
        MOBILE("mobile"),
        /**
         * TV browsers, such as those available through GoogleTV devices and game consoles.
         */
        @XmlEnumValue("tv")
        TV("tv");

        private final String value;

        Type(String value){
            this.value = value;
        }

        /**
         * The string value of the type
         * @return the type
         */
        public String getValue(){
            return value;
        }

        /**
         * Converts a string value to an enum constant of the Type enum.
         *
         * @param value the string value to be converted
         * @return the enum constant that corresponds to the provided value
         * @throws IllegalArgumentException if the value does not match any enum constant
         */
        public static Type fromValue(String value){
            for (Type type : Type.values()) {
                if (type.getValue().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }

    }
}
