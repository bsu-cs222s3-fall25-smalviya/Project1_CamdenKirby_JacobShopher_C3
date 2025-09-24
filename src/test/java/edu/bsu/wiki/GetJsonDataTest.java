package edu.bsu.wiki;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetJsonDataTest {

    @Test
    public void testCreateURL() throws IOException, URISyntaxException {
        String pageTitle = "Zappa";

        URLConnection connection = GetJsonData.connectToWikipedia(pageTitle);
        URL url = connection.getURL();

        String expectedUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=revisions&titles=Zappa" +
                "&rvprop=timestamp%7Cuser&rvlimit=15&redirects";

        Assertions.assertEquals(expectedUrl, url.toString());

    }

    @Test
    void testRevisions() {
        String pageTitle = "Zappa";
        boolean testFail = true;

        try {
            URLConnection connection = GetJsonData.connectToWikipedia(pageTitle);
            String jsonData = GetJsonData.readJsonAsStringFrom(connection);

            // If JSON is not empty, then test passes
            if (!jsonData.isEmpty()) {
                testFail = false;
            }
        } catch (IOException e) {
            Assertions.fail("Network Error");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertFalse(testFail, "Failed to retrieve JSON data.");
    }
}
