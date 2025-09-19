package edu.bsu.wiki;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        String articleTitle = getArticleTitle();
        URLConnection connection = GetJsonData.connectToWikipedia(articleTitle);
        String rawJsonData = GetJsonData.readJsonAsStringFrom(connection);
        FormatJsonData.printFormattedJson(rawJsonData);


    }


    public static String getArticleTitle() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the Article Title: ");
        String articleTitle = scanner.nextLine();
        return articleTitle;
    }
}
