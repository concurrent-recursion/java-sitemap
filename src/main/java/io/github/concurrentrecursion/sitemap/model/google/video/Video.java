package io.github.concurrentrecursion.sitemap.model.google.video;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.adapters.BooleanYesNoAdapter;
import io.github.concurrentrecursion.sitemap.adapters.OffsetDateTimeAdapter;
import io.github.concurrentrecursion.sitemap.adapters.UrlAdapter;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a video entity.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "thumbnailUrl",
        "title",
        "description",
        "contentUrl",
        "playerUrl",
        "duration",
        "expirationDate",
        "rating",
        "viewCount",
        "publicationDate",
        "familyFriendly",
        "restriction",
        "platform",
        "requiresSubscription",
        "uploader",
        "live",
        "tags"
})
@Data
@Accessors(chain = true)
public class Video {
    /**
     * A URL pointing to the video thumbnail image file
     * @param thumbnailUrl the url to the video thumbnail
     * @return the url to the thumbnail
     */
    @XmlElement(name = "thumbnail_loc")
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL thumbnailUrl;
    /**
     * The title of the video. It is recommended that this match the video title displayed on the web page where the
     * video is embedded.
     * @param title the title of the video
     * @return the title of the video
     */
    private String title;
    /**
     * A description of the video. Maximum 2048 characters.
     * It must match the description displayed on the web page where the video is embedded, but it doesn't need to be a
     * word-for-word match.
     * @param description the video description
     * @return the video description
     */
    @Size(max = 2048, groups = WriteValidation.class)
    private String description;
    /**
     * A URL pointing to the actual video media file
     * @param contentUrl the url to the video file
     * @return the url to the video file
     */
    @XmlElement(name = "content_loc")
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL contentUrl;
    /**
     * A URL pointing to a player for a specific video. Usually this is the information in the src attribute of an {@code <embed>} tag.
     * @param playerUrl the url to the player
     * @return the url to the player
     */
    @XmlElement(name = "player_loc")
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL playerUrl;
    /**
     * The duration of the video, in seconds. Value must be from 1 to 28800 (8 hours)
     * @param duration the duration in seconds
     * @return the duration in seconds
     */
    @Min(value = 1, groups = WriteValidation.class)
    @Max(value = 28800 , groups = WriteValidation.class)
    private Integer duration;
    /**
     * The date after which the video is no longer be available. Omit this tag if your video does not expire
     * @param expirationDate the after which the video is no longer available
     * @return the expiration date
     */
    @XmlElement(name = "expiration_date")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime expirationDate;
    /**
     * The rating of the video. Supported values are float numbers in the range 0.0 (low) to 5.0 (high).
     * @param rating A decimal rating between 0.0 and 5.0
     * @return the rating
     */
    @DecimalMin(value = "0.0", groups = WriteValidation.class)
    @DecimalMax(value = "5.0", groups = WriteValidation.class)
    private Double rating;
    /**
     * The number of times the video has been viewed.
     * @param viewCount the view count
     * @return the view count
     */
    @XmlElement(name = "view_count")
    private Integer viewCount;
    /**
     * The date and time the video is published
     * @param publicationDate the video publication date
     * @return the date
     */
    @XmlElement(name = "publication_date")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime publicationDate;
    /**
     *  Whether the video is available with SafeSearch. If you omit this tag, the video is available when SafeSearch is
     *  turned on.
     * @param familyFriendly whether the video should be available in safesearch
     * @return whether the video is family friendly
     */
    @XmlElement(name = "family_friendly")
    @XmlJavaTypeAdapter(BooleanYesNoAdapter.class)
    private Boolean familyFriendly;
    /**
     * Whether to show or hide your video in search results from specific countries. If there is no Restriction set,
     * Google assumes that the video can be shown in all locations.
     * @param restriction the restriction specification
     * @return the restriction specification
     */
    @XmlElement(name = "restriction")
    private Restriction restriction;
    /**
     * Whether to show or hide your video in search results on specified platform types.If there is no Platform set,
     * Google assumes that the video can be played on all platforms
     * @param platform the platform specification
     * @return the platform specification
     */
    private Platform platform;
    /**
     * Indicates whether a subscription is required to view the video
     * @param requiresSubscription whether the video requires a subscription
     * @return whether the video requires a subscription
     */
    @XmlElement(name = "requires_subscription")
    @XmlJavaTypeAdapter(BooleanYesNoAdapter.class)
    private Boolean requiresSubscription;
    /**
     * The video uploader
     * @param uploader The video uploader
     * @return the video uploader
     */
    private Uploader uploader;
    /**
     * Indicates whether the video is a live stream.
     * @param live whether the video is a live stream
     * @return whether the video is a live stream
     */
    @XmlJavaTypeAdapter(BooleanYesNoAdapter.class)
    private Boolean live;
    /**
     * An arbitrary string tag describing the video. Tags are generally very short descriptions of key concepts
     * associated with a video or piece of content. A single video could have several tags, although it might belong to
     * only one category. For example, a video about grilling food may belong in the "grilling" category, but could be
     * tagged "steak", "meat", "summer", and "outdoor"<br>
     * Create a new tag for each tag associated with a video.<br>
     * A maximum of 32 tags is permitted.
     * @param tags a list of tags
     * @return a list of tags
     */
    @XmlElement(name = "tag")
    @Size(max = 32, groups = WriteValidation.class)
    private List<String> tags = new ArrayList<>();


    /**
     * Set the video thumbnail URL
     * @param thumbnailUrl the URL to the video thumbnail
     * @return the Video object with the given thumbnail URL set
     */
    public Video setThumbnailUrl(URL thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    /**
     * Set the video thumbnail URL
     * @param thumbnailUrl the URL to the video thumbnail
     * @return the Video object with the given thumbnail URL set
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Video setThumbnailUrl(String thumbnailUrl) throws RuntimeMalformedUrlException {
        this.thumbnailUrl = UrlUtil.convertToUrl(thumbnailUrl);
        return this;
    }

    /**
     * Set the video content URL
     * @param contentUrl the URL to the video file
     * @return the Video object with the given content URL set
     */
    public Video setContentUrl(URL contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }
    /**
     * Set the video content URL
     * @param contentUrl the URL to the video file
     * @return the Video object with the given content URL set
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Video setContentUrl(String contentUrl) throws RuntimeMalformedUrlException {
        this.contentUrl = UrlUtil.convertToUrl(contentUrl);
        return this;
    }

    /**
     * Set the video player URL
     * @param playerUrl the URL to the video player
     * @return the Video object with the given player URL set
     */
    public Video setPlayerUrl(URL playerUrl) {
        this.playerUrl = playerUrl;
        return this;
    }
    /**
     * Set the video player URL
     * @param playerUrl the URL to the video player
     * @return the Video object with the given player URL set
     * @throws RuntimeMalformedUrlException if the url is malformed
     */
    public Video setPlayerUrl(String playerUrl) throws RuntimeMalformedUrlException {
        this.playerUrl = UrlUtil.convertToUrl(playerUrl);
        return this;
    }
}
