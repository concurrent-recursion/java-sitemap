package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import io.github.concurrentrecursion.sitemap.model.google.image.Image;
import io.github.concurrentrecursion.sitemap.model.google.news.News;
import io.github.concurrentrecursion.sitemap.model.google.news.Publication;
import io.github.concurrentrecursion.sitemap.model.google.video.*;
import io.github.concurrentrecursion.sitemap.model.xhtml.Link;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UrlTests {


    @Test
    void testNullUrlString() {
        assertThrows(NullPointerException.class, () -> new Url((String) null));
    }

    @Test
    void testMalformedUrl() {
        assertThrows(RuntimeMalformedUrlException.class, () -> new Url("urn:isbn:999-1-23-4567890"));
    }

    @Test
    void testIllegalArgumentUrl() {
        assertThrows(RuntimeMalformedUrlException.class, () -> new Url("://example.com"));
    }

    @Test
    void testUrl() {
        assertDoesNotThrow(() -> new Url("http://test:user@www.example.com:4123/some-path?param=value&somother=thing#fragment"));
    }

    @Test
    void testImage() throws Exception {
        final String expectedUrl1 = "http://www.example.com/images/photo.jpg";
        final String expectedUrl2 = "http://www.example.com/images/picture.png";
        Url images = new Url("http://www.example.com/about");
        images.addImage(new Image(URI.create(expectedUrl1).toURL()));
        Image image2 = new Image();
        image2.setLocation(URI.create(expectedUrl2).toURL());
        images.addImage(image2);

        assertEquals(expectedUrl1, images.getImages().get(0).getLocation().toString());
        assertEquals(expectedUrl2, images.getImages().get(1).getLocation().toString());
    }

    @Test
    void testUploader() throws Exception {
        Uploader uploader = new Uploader();
        uploader.setUploaderInfoUrl("http://www.example.com")
                .setName("Bob Loblaw");
        assertEquals("http://www.example.com", uploader.getUploaderInfoUrl().toString());
        assertEquals("Bob Loblaw", uploader.getName());

        uploader.setUploaderInfoUrl(URI.create("http://www.example.com/bob").toURL());
        assertEquals("http://www.example.com/bob", uploader.getUploaderInfoUrl().toString());
    }

    @Test
    void testRestriction() {
        Restriction restriction = new Restriction();

        Relationship relationship = Relationship.ALLOW;
        List<String> countries = List.of("US", "CA", "MX");

        restriction.setRelationship(relationship);
        restriction.setCountries(countries);

        assertEquals(relationship, restriction.getRelationship());
        assertEquals(countries, restriction.getCountries());
    }

    @Test
    void testRelationship(){
        Relationship relationship = Relationship.ALLOW;
        assertEquals("allow", relationship.getValue());
        assertEquals("deny",Relationship.DENY.getValue());
        assertThrows(IllegalArgumentException.class,() -> Relationship.fromValue(null));
        assertThrows(IllegalArgumentException.class,() -> Relationship.fromValue(""));
        assertThrows(IllegalArgumentException.class,() -> Relationship.fromValue("asdfas"));
        assertEquals(Relationship.DENY,Relationship.fromValue("deny"));
        assertEquals(Relationship.ALLOW,Relationship.fromValue("allow"));
    }

    @Test
    void testPlatform(){
        Platform platform = new Platform();

        Relationship relationship1 = Relationship.ALLOW;
        List<Platform.Type> platforms1 = List.of(Platform.Type.WEB, Platform.Type.MOBILE);

        platform.setRelationship(relationship1);
        platform.setPlatforms(platforms1);

        assertEquals(relationship1, platform.getRelationship());
        assertEquals(platforms1, platform.getPlatforms());

        Relationship relationship2 = Relationship.DENY;
        List<Platform.Type> platforms2 = List.of(Platform.Type.WEB, Platform.Type.MOBILE, Platform.Type.TV);

        platform.setRelationship(relationship2);
        platform.setPlatforms(platforms2);

        assertEquals(relationship2, platform.getRelationship());
        assertEquals(platforms2, platform.getPlatforms());

        assertEquals(Platform.Type.TV, Platform.Type.fromValue("tv"));
        assertThrows(IllegalArgumentException.class,() -> Platform.Type.fromValue(null));
        assertThrows(IllegalArgumentException.class,() -> Platform.Type.fromValue(""));
    }

    @Test
    void testVideo() throws Exception {

        Uploader uploader = new Uploader()
                .setUploaderInfoUrl(URI.create("https://www.example.com/bob").toURL())
                .setName("Bob Loblaw");

        Platform platform = new Platform(Relationship.DENY, List.of(Platform.Type.TV));

        Restriction restriction = new Restriction()
                .setRelationship(Relationship.ALLOW)
                .setCountries(List.of("US CA MX"));

        final OffsetDateTime pubDate = OffsetDateTime.now();
        final OffsetDateTime expDate = pubDate.plusDays(90);

        Video video = new Video()
                .setDescription("Description")
                .setTitle("Title")
                //Using String setters
                .setPlayerUrl("http://www.example.com/player/vid123")
                .setThumbnailUrl("http://www.example.com/thumb/vid123")
                .setContentUrl("http://www.example.com/content/vid123")
                .setTags(List.of("tag1", "tag2"))
                .setRating(1.2)
                .setLive(true)
                .setDuration(300)
                .setUploader(uploader)
                .setPlatform(platform)
                .setRestriction(restriction)
                .setFamilyFriendly(true)
                .setPublicationDate(pubDate)
                .setExpirationDate(expDate)
                .setRequiresSubscription(true)
                .setViewCount(123456789);

        assertEquals("Description", video.getDescription());
        assertEquals("Title", video.getTitle());
        assertEquals("http://www.example.com/player/vid123", video.getPlayerUrl().toString());
        assertEquals("http://www.example.com/thumb/vid123", video.getThumbnailUrl().toString());
        assertEquals("http://www.example.com/content/vid123", video.getContentUrl().toString());
        assertEquals(List.of("tag1", "tag2"), video.getTags());
        assertEquals(1.2, video.getRating());
        assertEquals(true, video.getLive());
        assertEquals(300, video.getDuration());
        assertEquals(uploader, video.getUploader());
        assertEquals(platform, video.getPlatform());
        assertEquals(restriction, video.getRestriction());
        assertTrue(video.getFamilyFriendly());
        assertEquals(pubDate, video.getPublicationDate());
        assertEquals(expDate, video.getExpirationDate());
        assertTrue(video.getRequiresSubscription());
        assertEquals(123456789, video.getViewCount());

        //Using URL setters
        video.setPlayerUrl(URI.create("http://www.example.com/player/new123").toURL())
                .setContentUrl(URI.create("http://www.example.com/content/new123").toURL())
                .setThumbnailUrl(URI.create("http://www.example.com/thumb/new123").toURL());

        assertEquals("http://www.example.com/player/new123", video.getPlayerUrl().toString());
        assertEquals("http://www.example.com/content/new123", video.getContentUrl().toString());
        assertEquals("http://www.example.com/thumb/new123", video.getThumbnailUrl().toString());
    }

    @Test
    void testPublication(){
        Publication pulication = new Publication().setLanguage("en").setName("Name");
        assertEquals("Name", pulication.getName());
        assertEquals("en", pulication.getLanguage());
    }

    @Test
    void testNews(){
        final OffsetDateTime expectedDate = OffsetDateTime.now();
        final Publication publication = new Publication().setLanguage("fr").setName("Le Paper");

        News news = new News().setTitle("Title").setPublicationDate(expectedDate).setPublication(publication);
        assertEquals("Title", news.getTitle());
        assertEquals(expectedDate, news.getPublicationDate());
        assertEquals(publication, news.getPublication());
    }

    @Test
    void testLink() throws Exception {
        final String expected = "http://www.example.com/en/about";
        Link link = new Link("en",expected);
        assertEquals("en", link.getLanguage());
        assertEquals(expected,link.getHref().toString());
        assertEquals("alternate",link.getRelationship());

        link.setRelationship("relationship");
        assertEquals("relationship", link.getRelationship());

        Link link1 = new Link("es",URI.create(expected).toURL());
        assertEquals(expected,link1.getHref().toString());

        Link link2 = new Link();
        final String expected2 = "http://www.example.com/es/about";
        link2.setHref(URI.create(expected2).toURL()).setLanguage("es");
        assertEquals(expected2,link2.getHref().toString());

        Link link3 = new Link();
        final String expected3 = "http://www.example.com/fr/about";
        link3.setHref(expected3).setLanguage("fr");
        assertEquals(expected3,link3.getHref().toString());
    }

    @Test
    void testChangeFrequency(){
        assertEquals("yearly",ChangeFrequency.YEARLY.getValue());
        assertEquals("monthly",ChangeFrequency.MONTHLY.getValue());
        assertEquals("weekly",ChangeFrequency.WEEKLY.getValue());
        assertEquals("daily",ChangeFrequency.DAILY.getValue());
        assertEquals("always",ChangeFrequency.ALWAYS.getValue());
        assertEquals("never",ChangeFrequency.NEVER.getValue());

        assertEquals(ChangeFrequency.YEARLY,ChangeFrequency.fromValue("yearly"));
        assertThrows(IllegalArgumentException.class,() -> ChangeFrequency.fromValue(""));
        assertThrows(IllegalArgumentException.class,() -> ChangeFrequency.fromValue(null));
    }
}
