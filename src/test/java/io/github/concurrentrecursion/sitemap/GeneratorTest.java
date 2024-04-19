package io.github.concurrentrecursion.sitemap;

import io.github.concurrentrecursion.exception.DataSerializationException;
import io.github.concurrentrecursion.sitemap.io.Generator;
import io.github.concurrentrecursion.sitemap.io.SitemapGenerator;
import io.github.concurrentrecursion.sitemap.model.ChangeFrequency;
import io.github.concurrentrecursion.sitemap.model.Url;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;
import io.github.concurrentrecursion.sitemap.model.google.image.Image;
import io.github.concurrentrecursion.sitemap.model.google.news.News;
import io.github.concurrentrecursion.sitemap.model.google.news.Publication;
import io.github.concurrentrecursion.sitemap.model.google.video.*;
import io.github.concurrentrecursion.sitemap.model.xhtml.Link;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class GeneratorTest {
    //Generated Namespaces that will appear in root urlset/siteindex elements
    private static final String XMLNS = "xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"";

    @Test
    void testUrlEncoding() throws Exception {
        final SitemapGenerator generator = new SitemapGenerator().setPrettyPrint(false);
        final String testUrl = "http://www.example.com/ümlat.php&q=name";
        final String expectedUrl = "<urlset " + XMLNS + "><url><loc>http://www.example.com/%C3%BCmlat.php&amp;q=name</loc></url></urlset>";

        UrlSetSitemap urlSet = UrlSetSitemap.fromUrls(Stream.of(new Url(testUrl)));
        assertEquals(expectedUrl, generator.writeToString(urlSet));
        assertEquals(testUrl, urlSet.getUrls().get(0).getLocation().toString());//verify url is only encoded for serialization

        urlSet = UrlSetSitemap.fromUrls(Stream.of(new Url(URI.create(testUrl).toURL())));
        assertEquals(expectedUrl, generator.writeToString(urlSet));
    }

    @Test
    void testNews() throws Exception {
        UrlSetSitemap urlSet = new UrlSetSitemap();
        urlSet.getUrls().add(new Url("http://www.example.org/business/article55.html").setNews(new News().setPublication(new Publication("The Example Times", "en")).setPublicationDate(OffsetDateTime.parse("2008-12-23T00:00:00.000Z")).setTitle("Companies A, B in Merger Talks")).setPriority(0.8));
        Generator generator = new SitemapGenerator();
        log.info(generator.writeToString(urlSet));
    }

    @Test
    void testImage() throws Exception {
        UrlSetSitemap urlSet = new UrlSetSitemap().addUrl(new Url("https://example.com/sample1.html").addImage(new Image("https://example.com/image.jpg")).addImage(new Image("https://example.com/photo.jpg"))).addUrl(new Url("https://example.com/sample2.html").addImage(new Image("https://example.com/picture.jpg")));
        Generator generator = new SitemapGenerator();
        log.info(generator.writeToString(urlSet));
    }

    @Test
    void testGzip() throws Exception {
        List<Url> urls = List.of(new Url("http://www.example.com/ümlat.php&q=name").setChangeFrequency(ChangeFrequency.NEVER), new Url(URI.create("https://www.example.com/stuff-and-things").toURL()).setPriority(0.1d), new Url("http://www.example.org/business/article55.html").setNews(new News().setTitle("Companies A, B in Merger Talks").setPublicationDate(OffsetDateTime.now()).setPublication(new Publication("The Example Times", "en"))));
        UrlSetSitemap urlSet = new UrlSetSitemap().setUrls(urls).setFilename("gzip-sitemap.xml");
        Generator generator = new SitemapGenerator().setUseGzip(true).setPrettyPrint(false);
        Path filePath = Paths.get("/vcs/sitemaps");
        generator.write(urlSet, filePath);
    }

    @Test
    void testTooBig() throws Exception {
        UrlSetSitemap big = new UrlSetSitemap();
        for (int i = 0; i < 10_000; i++) {
            big.addUrl(createBigUrl(i));
        }
        log.debug("Urls added");
        SitemapGenerator generator = new SitemapGenerator().setPrettyPrint(false);
        Path tempDir = Files.createTempDirectory("sitemap");
        assertThrows(DataSerializationException.class,() ->generator.write(big,tempDir));
    }

    private final SecureRandom random = new SecureRandom();
    private final List<String> langCodes = List.of("en","de","es","fr","tlh");
    private static final String LOREM_IPSUM_2048 = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenea";

    private Url createBigUrl(final int index){
        Url url = new Url("http://www.example.com/pages/" + random.nextInt(20_000) + ".html")
                .setChangeFrequency(ChangeFrequency.NEVER)
                .setLastModifiedDate(OffsetDateTime.now());
        if(index < 1000) {
            url.setNews(
                            new News().setPublicationDate(OffsetDateTime.now()).setPublication(
                                    new Publication("The Example Times", "en")
                            ).setTitle("Companies A, B in Merger Talks")
                    ).setPriority(0.5)

            ;
        }
        for(String langCode : langCodes){
            url.addLink(new Link(langCode,url.getLocation().toString() + "-" + langCode));
        }
        for(int i = 0 ; i < 20 ; i++){
            url.addImage(new Image("https://example.com/images/image" + i + ".jpg"));
        }
        for(int i = 0 ; i < 1 ; i++){
            url.addVideo(new Video().setFamilyFriendly(random.nextBoolean())
                    .setLive(random.nextBoolean())
                    .setExpirationDate(OffsetDateTime.now().plusDays(1))
                            .setPlatform(new Platform(Relationship.ALLOW,List.of(Platform.Type.TV, Platform.Type.WEB, Platform.Type.MOBILE)))
                            .setRestriction(new Restriction(Relationship.DENY,List.of("NK")))
                            .setRating(3.0)
                            .setTags(List.of("stuff","things","seo","sitemaps","guessing","pagerank","math","testing"))
                            .setUploader(new Uploader().setName("Rufus Barksalot").setUploaderInfoUrl("https://www.example.org/profiles/dogs/rufus"))
                            .setDuration(random.nextInt(28800))
                            .setContentUrl(url.getLocation().toString() + "/content/video" + i)
                            .setThumbnailUrl(url.getLocation().toString() + "/thumbnails/video" + i)
                            .setPlayerUrl(url.getLocation().toString() + "/player/video" + i)
                            .setDescription(LOREM_IPSUM_2048)
                );
        }
        return url;
    }




}
