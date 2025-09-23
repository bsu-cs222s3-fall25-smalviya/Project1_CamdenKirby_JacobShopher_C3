package edu.bsu.wiki;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormatJsonData {

    public static void printFormattedJson(String rawJsonData) {
        JSONObject json = new JSONObject(rawJsonData);
        JSONObject query = json.getJSONObject("query");

        if (query.has("redirects")) {
            JSONArray redirects = query.getJSONArray("redirects");
            if (!redirects.isEmpty()) {
                String redirectedTo = redirects.getJSONObject(0).getString("to");
                System.out.println("Redirected to " + redirectedTo);
            }
        }

        JSONObject pages = query.getJSONObject("pages");
        String pageId = pages.keys().next();

        // Missing page check
        if (pageId.equals("-1")) {
            System.err.println("Error: No page found.");
            return;
        }

        JSONObject page = pages.getJSONObject(pageId);
        JSONArray revisions = page.optJSONArray("revisions");

        if (revisions == null || revisions.isEmpty()) {
            System.err.println("Error: No revisions found.");
            return;
        }

        // Print each revision
        for (int i = 0; i < revisions.length(); i++) {
            JSONObject rev = revisions.getJSONObject(i);
            String timestamp = rev.getString("timestamp");
            String user = rev.getString("user");
            System.out.printf("%d  %s  %s%n", i + 1, timestamp, user);
        }
    }
}