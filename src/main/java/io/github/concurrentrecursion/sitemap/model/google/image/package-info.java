/**
 * This package contains the model classes for the Google image sitemap extension
 */
@XmlSchema(
        namespace = "http://www.google.com/schemas/sitemap-image/1.1",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "image",namespaceURI = "http://www.google.com/schemas/sitemap-image/1.1")
        }
)
package io.github.concurrentrecursion.sitemap.model.google.image;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
