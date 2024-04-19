/**
 * This package contains the model classes for the xhtml models
 */
@XmlSchema(
        namespace = "http://www.w3.org/1999/xhtml",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "xhtml",namespaceURI = "http://www.w3.org/1999/xhtml")
        }
)
package io.github.concurrentrecursion.sitemap.model.xhtml;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
