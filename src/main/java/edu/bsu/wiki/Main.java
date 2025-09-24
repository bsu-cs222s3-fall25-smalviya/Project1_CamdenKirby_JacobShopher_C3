package edu.bsu.wiki;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String pageTitle = getPageTitle();

        // Ensures an input was given
        if (pageTitle == null || pageTitle.isBlank()) {
            System.err.println("Error: No page title provided.");
            return;
        }
        try {
            URLConnection connection = GetJsonData.connectToWikipedia(pageTitle);
            String rawJsonData = GetJsonData.readJsonAsStringFrom(connection);
            FormatJsonData.printFormattedJson(rawJsonData);

        } catch (IOException e) {
            System.err.println("Error: Network Issue.");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPageTitle() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the Article Title: ");
        return scanner.nextLine();
    }
}
