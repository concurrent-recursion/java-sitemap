/**
 * This package contains the model classes for the Google video sitemap extension
 */
@XmlSchema(
        namespace = "http://www.google.com/schemas/sitemap-video/1.1",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "news",namespaceURI = "http://www.google.com/schemas/sitemap-video/1.1")
        }
)
package io.github.concurrentrecursion.sitemap.model.google.video;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;