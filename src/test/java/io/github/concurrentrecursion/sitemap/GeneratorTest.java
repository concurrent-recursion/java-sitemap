package io.github.concurrentrecursion.sitemap;

import io.github.concurrentrecursion.exception.DataSerializationException;
import io.github.concurrentrecursion.sitemap.io.Writer;
import io.github.concurrentrecursion.sitemap.io.SitemapWriter;
import io.github.concurrentrecursion.sitemap.model.*;
import io.github.concurrentrecursion.sitemap.model.google.image.Image;
import io.github.concurrentrecursion.sitemap.model.google.news.News;
import io.github.concurrentrecursion.sitemap.model.google.news.Publication;
import io.github.concurrentrecursion.sitemap.model.google.video.*;
import io.github.concurrentrecursion.sitemap.model.xhtml.Link;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.xmlunit.matchers.CompareMatcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GeneratorTest {
    //Generated Namespaces that will appear in root urlset/siteindex elements
    private static final String XMLNS = "xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"";
    private static final String LOREM_IPSUM_2048 = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenea";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final List<String> langCodes = List.of("en","de","es","fr","tlh");


    @Test
    void testUrlEncoding() throws Exception {
        final SitemapWriter generator = new SitemapWriter().setPrettyPrint(false);
        final String testUrl = "http://www.example.com/ümlat.php&q=name";
        final String expectedUrl = "<urlset " + XMLNS + "><url><loc>http://www.example.com/%C3%BCmlat.php&amp;q=name</loc></url></urlset>";

        UrlSetSitemap urlSet = UrlSetSitemap.fromUrls(Stream.of(new Url(testUrl)));
        assertEquals(expectedUrl, generator.writeToString(urlSet));
        assertEquals(testUrl, urlSet.getUrls().get(0).getLocation().toString());//verify url is only encoded for serialization

        urlSet = UrlSetSitemap.fromUrls(Stream.of(new Url(URI.create(testUrl).toURL())));
        assertThat(generator.writeToString(urlSet), CompareMatcher.isIdenticalTo(expectedUrl).ignoreWhitespace());
    }

    @Test
    void testUrlTooLong() {
        final SitemapWriter generator = new SitemapWriter().setPrettyPrint(false);
        final String testUrl = "http://www.example.com/pages/a-really-long-url-that-is-not-a-good-idea-but/we-are-doing-it-anyway/for-no-real-reason/lorem-ipsum-dolor-sit-amet,-consectetuer-adipiscing-elit-aenean-commodo-ligula-eget-dolor-aenean-massa-cum-sociis-natoque-penatibus-et-magnis-dis-parturient-montes,-nascetur-ridiculus-mus-donec-quam-felis,-ultricies-nec,-pellentesque-eu,-pretium-quis,-sem-nulla-consequat-massa-quis-enim-donec-pede-justo,-fringilla-vel,-aliquet-nec,-vulputate-eget,-arcu-in-enim-justo,-rhoncus-ut,-imperdiet-a,-venenatis-vitae,-justo-nullam-dictum-felis-eu-pede-mollis-pretium-integer-tincidunt-cras-dapibus-vivamus-elementum-semper-nisi-aenean-vulputate-eleifend-tellus-aenean-leo-ligula,-porttitor-eu,-consequat-vitae,-eleifend-ac,-enim-aliquam-lorem-ante,-dapibus-in,-viverra-quis,-feugiat-a,-tellus-phasellus-viverra-nulla-ut-metus-varius-laoreet-quisque-rutrum-aenean-imperdiet-etiam-ultricies-nisi-vel-augue-curabitur-ullamcorper-ultricies-nisi-nam-eget-dui-etiam-rhoncus-maecenas-tempus,-tellus-eget-condimentum-rhoncus,-sem-quam-semper-libero,-sit-amet-adipiscing-sem-neque-sed-ipsum-nam-quam-nunc,-blandit-vel,-luctus-pulvinar,-hendrerit-id,-lorem-maecenas-nec-odio-et-ante-tincidunt-tempus-donec-vitae-sapien-ut-libero-venenatis-faucibus-nullam-quis-ante-etiam-sit-amet-orci-eget-eros-faucibus-tincidunt-duis-leo-sed-fringilla-mauris-sit-amet-nibh-donec-sodales-sagittis-magna-sed-consequat,-leo-eget-bibendum-sodales,-augue-velit-cursus-nunc,-quis-gravida-magna-mi-a-libero-fusce-vulputate-eleifend-sapien-vestibulum-purus-quam,-scelerisque-ut,-mollis-sed,-nonummy-id,-metus-nullam-accumsan-lorem-in-dui-cras-ultricies-mi-eu-turpis-hendrerit-fringilla-vestibulum-ante-ipsum-primis-in-faucibus-orci-luctus-et-ultrices-posuere-cubilia-curae;-in-ac-dui-quis-mi-consectetuer-lacinia-nam-pretium-turpis-et-arcu-duis-arcu-tortor,-suscipit-eget,-imperdiet-nec,-imperdiet-iaculis,-ipsum-sed-aliquam-ultrices-mauris-integer-ante-arcu,-accumsan-a,-consectetuer-eget,-posuere-ut,-mauris-praesent-adipiscing-phasellus-ullamcorper-ipsum-rutrum-nunc-nunc-nonummy-metus-vestibulum-volutpat-pretium-libero-cras-id-dui-aenea";
        final UrlSetSitemap urlSet = UrlSetSitemap.fromUrls(new Url(testUrl));
        assertThrows(ConstraintViolationException.class, () -> generator.writeToString(urlSet));

        final String testUrl1 = "http://www.example.com/pages/this-is-ünder-2048-character-limit-but-encoding-the-special-characters-will-put-it-over-2048/because-encoding-urls-is-fun/lorem-ipsum-dolor-sit-amet,-consectetuer-adipiscing-elit-aenean-commodo-ligula-eget-dolor-aenean-massa-cum-sociis-natoque-penatibus-et-magnis-dis-parturient-montes,-nascetur-ridiculus-mus-donec-quam-felis,-ultricies-nec,-pellentesque-eu,-pretium-quis,-sem-nulla-consequat-massa-quis-enim-donec-pede-justo,-fringilla-vel,-aliquet-nec,-vulputate-eget,-arcu-in-enim-justo,-rhoncus-ut,-imperdiet-a,-venenatis-vitae,-justo-nullam-dictum-felis-eu-pede-mollis-pretium-integer-tincidunt-cras-dapibus-vivamus-elementum-semper-nisi-aenean-vulputate-eleifend-tellus-aenean-leo-ligula,-porttitor-eu,-consequat-vitae,-eleifend-ac,-enim-aliquam-lorem-ante,-dapibus-in,-viverra-quis,-feugiat-a,-tellus-phasellus-viverra-nulla-ut-metus-varius-laoreet-quisque-rutrum-aenean-imperdiet-etiam-ultricies-nisi-vel-augue-curabitur-ullamcorper-ultricies-nisi-nam-eget-dui-etiam-rhoncus-maecenas-tempus,-tellus-eget-condimentum-rhoncus,-sem-quam-semper-libero,-sit-amet-adipiscing-sem-neque-sed-ipsum-nam-quam-nunc,-blandit-vel,-luctus-pulvinar,-hendrerit-id,-lorem-maecenas-nec-odio-et-ante-tincidunt-tempus-donec-vitae-sapien-ut-libero-venenatis-faucibus-nullam-quis-ante-etiam-sit-amet-orci-eget-eros-faucibus-tincidunt-duis-leo-sed-fringilla-mauris-sit-amet-nibh-donec-sodales-sagittis-magna-sed-consequat,-leo-eget-bibendum-sodales,-augue-velit-cursus-nunc,-quis-gravida-magna-mi-a-libero-fusce-vulputate-eleifend-sapien-vestibulum-purus-quam,-scelerisque-ut,-mollis-sed,-nonummy-id,-metus-nullam-accumsan-lorem-in-dui-cras-ultricies-mi-eu-turpis-hendrerit-fringilla-vestibulum-ante-ipsum-primis-in-faucibus-orci-luctus-et-ultrices-posuere-cubilia-curae;-in-ac-dui-quis-mi-consectetuer-lacinia-nam-pretium-turpis-et-arcu-duis-arcu-tortor,-suscipit-eget,-imperdiet-nec,-imperdiet-iaculis,-ipsum-sed-aliquam-ultrices-mauris-integer-ante-arcu,-accumsan-a,-consectetuer-eget,-posuere-ut,-mauris-praesent-adipiscing-phasel";
        final UrlSetSitemap urlSet1 = UrlSetSitemap.fromUrls(new Url(testUrl1));
        assertEquals(2048,urlSet1.getUrls().get(0).getLocation().toString().length());
        assertThrows(ConstraintViolationException.class, () -> generator.writeToString(urlSet1));

    }

    @Test
    void testNews() throws Exception {
        UrlSetSitemap urlSet = new UrlSetSitemap();
        urlSet.getUrls().add(new Url("http://www.example.org/business/article55.html")
                .setNews(
                    new News()
                        .setPublication(new Publication("The Example Times", "en"))
                        .setPublicationDate(OffsetDateTime.parse("2008-12-23T00:00:00.000Z"))
                        .setTitle("Companies A, B in Merger Talks")
                )
        );
        Writer generator = new SitemapWriter();
        String result = generator.writeToString(urlSet);
        String expected =
                "<urlset " + XMLNS + ">" +
                "<url>" +
                "<loc>http://www.example.org/business/article55.html</loc>" +
                "<news:news>" +
                "<news:publication>" +
                "<news:name>The Example Times</news:name>" +
                "<news:language>en</news:language>" +
                "</news:publication>" +
                "<news:publication_date>2008-12-23</news:publication_date>" +
                "<news:title>Companies A, B in Merger Talks</news:title>" +
                "</news:news>" +
                "</url>" +
                "</urlset>";
        assertThat(result, CompareMatcher.isIdenticalTo(expected).ignoreWhitespace());
    }

    @Test
    void testImage() throws Exception {
        UrlSetSitemap urlSet = new UrlSetSitemap()
                .addUrl(
                        new Url("https://example.com/sample1.html")
                                .addImage(new Image("https://example.com/image.jpg"))
                                .addImage(new Image("https://example.com/photo.jpg")))
                .addUrl(new Url("https://example.com/sample2.html")
                        .addImage(new Image("https://example.com/picture.jpg")));
        Writer generator = new SitemapWriter();
        String expected = "<urlset " + XMLNS + "><url><loc>https://example.com/sample1.html</loc><image:image><image:loc>https://example.com/image.jpg</image:loc></image:image><image:image><image:loc>https://example.com/photo.jpg</image:loc></image:image></url><url><loc>https://example.com/sample2.html</loc><image:image><image:loc>https://example.com/picture.jpg</image:loc></image:image></url></urlset>";
        assertThat(generator.writeToString(urlSet),CompareMatcher.isIdenticalTo(expected).ignoreWhitespace());
    }

    @Test
    void testGzip() throws Exception {
        List<Url> urls = List.of(
                new Url("http://www.example.com/")
                        .setChangeFrequency(ChangeFrequency.MONTHLY)
                        .setLastModifiedDate(OffsetDateTime.parse("2005-01-01T00:00:00.000Z"))
                        .setPriority(0.8)
        );
        final Path filePath = Files.createTempDirectory("sitemaps-test");
        UrlSetSitemap urlSet = new UrlSetSitemap().setUrls(urls).setFile(filePath.resolve("gzip-sitemap.xml"));
        Writer generator = new SitemapWriter().setUseGzip(true).setPrettyPrint(false);

        generator.write(urlSet, filePath);
        StringBuilder result = new StringBuilder();
        try(GZIPInputStream in = new GZIPInputStream(Files.newInputStream(urlSet.getFile())); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while((line = reader.readLine()) != null){
                result.append(line);
            }
        }
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><urlset " + XMLNS + "><url><loc>http://www.example.com/</loc><lastmod>2005-01-01</lastmod><changefreq>monthly</changefreq><priority>0.8</priority></url></urlset> ";
        assertThat(result.toString(),CompareMatcher.isIdenticalTo(expected).ignoreWhitespace());
    }


    @Test
    void testTooBig() throws Exception {
        UrlSetSitemap big = new UrlSetSitemap();
        for (int i = 0; i < 10_000; i++) {
            big.addUrl(createBigUrl(i));
        }
        log.debug("Urls added");
        SitemapWriter generator = new SitemapWriter().setPrettyPrint(false);
        Path tempDir = Files.createTempDirectory("sitemap");
        assertThrows(DataSerializationException.class,() ->generator.write(big,tempDir));
    }


    private Url createBigUrl(final int index){
        Url url = new Url("http://www.example.com/pages/" + RANDOM.nextInt(20_000) + ".html")
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
            url.addVideo(new Video().setFamilyFriendly(RANDOM.nextBoolean())
                    .setLive(RANDOM.nextBoolean())
                    .setExpirationDate(OffsetDateTime.now().plusDays(1))
                            .setPlatform(new Platform(Relationship.ALLOW,List.of(Platform.Type.TV, Platform.Type.WEB, Platform.Type.MOBILE)))
                            .setRestriction(new Restriction(Relationship.DENY,List.of("CA")))
                            .setRating(3.0)
                            .setTags(List.of("stuff","things","seo","sitemaps","guessing","pagerank","math","testing"))
                            .setUploader(new Uploader().setName("Rufus Barksalot").setUploaderInfoUrl("https://www.example.org/profiles/dogs/rufus"))
                            .setDuration(RANDOM.nextInt(28800))
                            .setContentUrl(url.getLocation().toString() + "/content/video" + i)
                            .setThumbnailUrl(url.getLocation().toString() + "/thumbnails/video" + i)
                            .setPlayerUrl(url.getLocation().toString() + "/player/video" + i)
                            .setDescription(LOREM_IPSUM_2048)
                );
        }
        return url;
    }

    @Test
    void testSitemapIndex() throws Exception {
        IndexSitemap index = new IndexSitemap();
        List<SitemapReference> references = index.getSitemapReferences();
        SitemapReference ref = new SitemapReference().setLocation(URI.create("https://www.example.com/sitemap-1.xml").toURL()).setLastModifiedDate(OffsetDateTime.now());
        ref.setSitemap(new UrlSetSitemap().addUrl(new Url("https://www.example.com/page/page1.html")));
        references.add(ref);

        SitemapWriter generator = new SitemapWriter().setPrettyPrint(false);
        final Path tempDir = Files.createTempDirectory("sitemap").resolve("sitemap.xml");
        generator.write(index,tempDir);
        assertTrue(Files.exists(tempDir));
    }


}
