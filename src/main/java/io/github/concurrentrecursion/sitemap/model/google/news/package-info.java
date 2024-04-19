/**
 * This package contains the model classes for the Google news sitemap extension
 */
@XmlSchema(
        namespace = "http://www.google.com/schemas/sitemap-news/0.9",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
            @XmlNs(prefix = "news",namespaceURI = "http://www.google.com/schemas/sitemap-news/0.9")
        }
)
package io.github.concurrentrecursion.sitemap.model.google.news;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
