package io.github.concurrentrecursion.sitemap.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

/**
 * Adapter class for converting between String and URL objects.
 */
@Slf4j
public class UrlAdapter extends XmlAdapter<String, URL> {
    @Override
    public URL unmarshal(String s) throws Exception {
        if(s == null) return null;
        return URI.create(s).toURL();
    }

    @Override
    public String marshal(URL url) throws Exception {
        if(url == null) return null;
        //Percent Encode
        String asciiUrl = URI.create(url.toString()).toASCIIString();
        if(asciiUrl.length() > 2048){
            log.warn("url loc is longer than 2048 characters. '{}'",asciiUrl);
        }
        return URI.create(asciiUrl).toURL().toString();
    }
}
