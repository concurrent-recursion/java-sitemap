package io.github.concurrentrecursion.sitemap.model;

import io.github.concurrentrecursion.exception.RuntimeMalformedUrlException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlTests {


    @Test
    void testNullUrlString(){
        assertThrows(NullPointerException.class, () -> new Url((String) null));
    }

    @Test
    void testMalformedUrl() {
        assertThrows(RuntimeMalformedUrlException.class, () -> new Url("urn:isbn:999-1-23-4567890") );
    }

    @Test
    void testIllegalArgumentUrl() {
        assertThrows(RuntimeMalformedUrlException.class, () -> new Url("://example.com") );
    }

    @Test
    void testUrl(){
        assertDoesNotThrow(() -> new Url("http://test:user@www.example.com:4123/some-path?param=value&somother=thing#fragment"));
    }
}
