package edu.bsu.wiki;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        URLConnection connection = connectToWikipedia();
        String jsonData = readJsonAsStringFrom(connection);
        printRawJson(jsonData);
    }

    private static URLConnection connectToWikipedia() throws IOException, URISyntaxException {
        String encodedUrlString = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=revisions&titles=" +
                URLEncoder.encode("Einstein", Charset.defaultCharset()) +
                "&rvprop=timestamp" + URLEncoder.encode("|",Charset.defaultCharset()) + "user&rvlimit=4&redirects";
        URI uri = new URI(encodedUrlString);
        URLConnection connection = uri.toURL().openConnection();
        connection.setRequestProperty("User-Agent",
                "FirstProject/0.1 (academic use; https://example.com)");
        connection.connect();
        return connection;
    }

    private static String readJsonAsStringFrom(URLConnection connection) throws IOException {
        return new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
    }

    private static void printRawJson(String jsonData) {
        System.out.println(jsonData);
    }

}