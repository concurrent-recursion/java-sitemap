package io.github.concurrentrecursion.sitemap.model.google.video;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import lombok.Getter;

/**
 * The relationship of the platform or restriction to the given values
 */
@XmlEnum
@Getter
public enum Relationship {
    /**
     * Any omitted platforms will be denied
     */
    @XmlEnumValue("allow")
    ALLOW("allow"),
    /**
     * Any omitted platforms will be allowed.
     */
    @XmlEnumValue("deny")
    DENY("deny");

    /**
     * Get the relationship value
     * @return the value
     */
    private final String value;

    Relationship(String value) {
        this.value = value;
    }

    /**
     * Returns the Relationship enum constant from the given value.
     *
     * @param value the value to match against the enum constants' values
     * @return the Relationship enum constant that matches the given value
     * @throws IllegalArgumentException if the given value does not match any enum constant's value
     */
    @NotNull
    public static Relationship fromValue(String value) {
        for (Relationship relationship : values()) {
            if (relationship.value.equalsIgnoreCase(value)) {
                return relationship;
            }
        }
        throw new IllegalArgumentException("Invalid relationship value " + value);
    }
}
