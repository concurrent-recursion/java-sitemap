/**
 * This package contains the model classes for generating XML sitemaps
 */
@XmlSchema(namespace = "http://www.sitemaps.org/schemas/sitemap/0.9",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
            @XmlNs(prefix = "",namespaceURI = "http://www.sitemaps.org/schemas/sitemap/0.9"),
                @XmlNs(prefix = "image",namespaceURI = "http://www.google.com/schemas/sitemap-image/1.1"),
                @XmlNs(prefix = "xhtml",namespaceURI = "http://www.w3.org/1999/xhtml"),
                @XmlNs(prefix = "news",namespaceURI = "http://www.google.com/schemas/sitemap-news/0.9"),
                @XmlNs(prefix = "video",namespaceURI = "http://www.google.com/schemas/sitemap-video/1.1")
        }
)
package io.github.concurrentrecursion.sitemap.model;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
