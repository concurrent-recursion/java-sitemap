package io.github.concurrentrecursion.sitemap.model;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import lombok.Getter;

/**
 *  How frequently the page is likely to change. This value provides general information to search engines and may not
 *  correlate exactly to how often they crawl the page.<br>
 *  Please note that the value of this tag is considered a hint and not a command. Even though search engine crawlers
 *  may consider this information when making decisions, they may crawl pages marked "hourly" less frequently than that,
 *  and they may crawl pages marked "yearly" more frequently than that. Crawlers may periodically crawl pages marked
 *  "never" so that they can handle unexpected changes to those pages.
 */
@XmlEnum
@Getter
public enum ChangeFrequency {
    /**
     * Documents that change each time they are accessed
     */
    @XmlEnumValue("always")
    ALWAYS("always"),
    /**
     * Documents that change hourly
     */
    @XmlEnumValue("hourly")
    HOURLY("hourly"),
    /**
     * Documents that change daily
     */
    @XmlEnumValue("daily")
    DAILY("daily"),
    /**
     * Documents that change weekly
     */
    @XmlEnumValue("weekly")
    WEEKLY("weekly"),
    /**
     * Documents that change monthly
     */
    @XmlEnumValue("monthly")
    MONTHLY("monthly"),
    /**
     * Documents that change yearly
     */
    @XmlEnumValue("yearly")
    YEARLY("yearly"),
    /**
     * Documents that are archived
     */
    @XmlEnumValue("never")
    NEVER("never");

    /**
     * The string representation
     * @return the value
     */
    private final String value;

    ChangeFrequency(String v) {
        value = v;
    }

    /**
     * Converts the given string value to a ChangeFrequency enum value.
     *
     * @param v the string value to be converted
     * @return the corresponding ChangeFrequency enum value
     * @throws IllegalArgumentException if the given string value does not match any ChangeFrequency enum value
     */
    public static ChangeFrequency fromValue(String v) {
        for (ChangeFrequency c: ChangeFrequency.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
