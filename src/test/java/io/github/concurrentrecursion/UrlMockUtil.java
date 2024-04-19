package io.github.concurrentrecursion;

import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

public class UrlMockUtil {

    public static URL mockUrl(final int responseCode, Supplier<InputStream> inputStreamSupplier) throws IOException {
        HttpURLConnection mockHttpURLConnection = mock(HttpURLConnection.class);
        when(mockHttpURLConnection.getResponseCode()).thenReturn(responseCode);
        Answer<InputStream> robotInputStreamAnswer = invocationOnMock -> inputStreamSupplier.get();
        doAnswer(robotInputStreamAnswer).when(mockHttpURLConnection).getInputStream();
        URL robotsUrl = mock(URL.class);
        doReturn(mockHttpURLConnection).when(robotsUrl).openConnection();
        return robotsUrl;
    }
}
