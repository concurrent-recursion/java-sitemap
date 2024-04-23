Java-Sitemap
============


Java Sitemap is a utility project to read and write sitemaps.

It supports reading and writing `<urlset>` and `<sitemapindex>` site maps, as well as locating sitemaps from a `robots.txt` file

**NOTE: java-sitemap is only compatible with Java 11+**

## Binaries/Download

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at http://search.maven.org.

Releases of enterprise-search-java are available in the Maven Central repository. Or alternatively see [Releases](https://github.com/concurrent-recursion/java-sitemap/releases).

### Maven

Add the following to your pom.xml

```xml
<dependency>
    <groupId>io.github.concurrentrecursion</groupId>
    <artifactId>java-sitemap</artifactId>
    <version>1.0.0</version>
</dependency>
```
### Gradle
Add the following dependency
```groovy
dependencies {
  implementation 'io.github.concurrent-recursion:java-sitemap:1.0.0'
}
```


## Usage

### Creating Url entries for your sitemap
Basic Url Example
```java
//Create a Url with only the location
new Url("http://www.example.com/about-us");
//Create a Url, setting the priority, change freq, and lastmodified date
new Url("https://www.example.com/search")
                .setPriority(0.3d)
                .setChangeFrequency(ChangeFrequency.ALWAYS)
                .setLastModifiedDate(OffsetDateTime.now()),
//Create a Url, setting the lastmodified date
new Url(URI.create("https://www.example.com/contact").toURL())
                .setLastModifiedDate(OffsetDateTime.parse("2024-04-01T08:23:12.000Z"))
```
Multi-Language Example

```java
new Url("https://www.example.com/sales")
    .addLink(new Link("de","https://www.example.com/ze-sales"))
    .addLink(new Link("es","https://www.example.com/el-sales"))
    .addLink(new Link("fr","https://www.example.com/le-sales"));
```


### Google News Url
Google recommends not to add more than 1,000 news urls in a sitemap. This is enforced when the sitemap is generated.
```java
new Url("https://example.com/news/x-announces-bankruptcy")
    .setLastModifiedDate(OffsetDateTime.parse("2025-03-02T02:11:00.000Z"))
    .setNews(
        new News()
            .setTitle("X Annouces Bankrupty")
            .setPublication(new Publication("The Daily Times", "en"))
            .setPublicationDate(OffsetDateTime.now())
    )
    .setChangeFrequency(ChangeFrequency.NEVER);
```

### Google Image Url

```java
new Url("https://example.com/about-us")
    .setLastModifiedDate(OffsetDateTime.parse("2024-03-02T02:11:00.000Z"))
    .addImage(new Image("https://example.com/profiles/robert.png"))
    .addImage(new Image("https://example.com/profiles/saul.png"))
    .setChangeFrequency(ChangeFrequency.YEARLY);
```
### Google Video Url
Example setting all of the metadata on a video
```java
new Url("https://example.com/v/play/3f22c1b")
    .setLastModifiedDate(OffsetDateTime.parse("2023-03-02T02:11:00.000Z"))
    .addVideo(
        new Video()
            .setContentUrl("https://example.com/v/play/3f22c1b.mp4")
            .setPlayerUrl("https://example.com/v/3f223c1b")
            .setDescription("How to grill steaks without burning your face off.")
            .setTitle("How To Grill Steaks")
            .setLive(false)
            .setTags(List.Of("steaks", "grilling"))
            .setDuration(360)
            .setRating(4.3d)
            .setPlatform(new Platform().setPlatforms(List.of(TV)).setRelationship(Relationship.DENY))
            .setUploader(new Uploader().setUploaderInfoUrl("https://www.youtube.com/grillerman").setName("Grill Erman"))
            .setRestriction(new Restriction().setRelationship(Relationship.DENY).setCountries(List.of("CA")))
            .setExpirationDate(OffsetDateTime.parse("2034-01-01T00:00:00.000Z"))
            .setFamilyFriendly(true)
    ).setChangeFrequency(ChangeFrequency.YEARLY);
```

## Saving Sitemaps

The Sitemap interface represents both UrlsetSitemap and IndexSitemap. The validations and restrictions are only applied 
when the SitemapGenerator saves the Sitemap. 

### Simple, UrlSet Sitemap
A UrlSet sitemap is useful when your site has < 50,00 URLs and when saved, would be less than 50MB.
### Write to Filesystem
```java
List<Url> urls = ...;

UrlSetSitemap sitemap = new UrlSetSitemap(urls);
SitemapGenerator generator = new SitemapGenerator().useGzipCompression(true);
Path sitemapDir = Paths.get("/var/www/example");
//This will validate and write the sitemap to /var/www/example/sitemap.xml.gz
generator.write(sitemap, sitemapDir);
```

### Write to String
```java
List<Url> urls = ...;

UrlSetSitemap sitemap = new UrlSetSitemap(urls);
//This will write the sitemap to the logger
log.info(new SitemapGenerator().setPrettyPrint(true).writeToString(sitemap));
```

### Large Sites, more than 50,000 URLs

```java
List<Url> urls = /*More than 50,000 URLs*/;
IndexSitemap sitemap = new IndexSitemap("https://example.com", urls);
SitemapGenerator generator = new SitemapGenerator().useGzipCompression(true);
Path dir = Paths.get("/var/www/example");
//This will write out the UrlSetSitemaps, and the IndexSitemap to /var/www/example
generator.write(sitemap, dir);
```

## Reading Sitemaps

### Reading urlset Sitemap directly from url

```java
SitemapReader reader = new SitemapReader();
//sitemap.xml is a <urlset> . If it isn't an exceptionn would be thrown
UrlSetSitemap sitemap = reader.readUrlSet("https://www.example.com/sitemap.xml");
```

### Reading from robots.txt
```java
Robots robotstxt = Robots.load(URI.create("https://www.example.com/robots.txt").toURL(), 2_000, 10_000);
SitemapReader reader = new SitemapReader();
List<Sitemap> sitemaps = reader.readSitemaps(robotstxt);
//Sitemaps will be an instance of either IndexSitemap or UrlSetSitemap
```
