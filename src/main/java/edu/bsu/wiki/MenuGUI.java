package edu.bsu.wiki;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLConnection;

public class MenuGUI extends Application {

    private TextField articleField;
    private Button searchButton;
    private ListView<String> resultList;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        articleField = new TextField();
        articleField.setPromptText("Enter Wikipedia article title");
        searchButton = new Button("Search");
        resultList = new ListView<>();
        statusLabel = new Label();

        searchButton.setOnAction(e -> fetchRevisions());

        VBox root = new VBox(10,
                new Label("Wikipedia Revision Getter"),
                articleField,
                searchButton,
                statusLabel,
                resultList
        );

        stage.setScene(new Scene(root, 500, 400));
        stage.setTitle("Wikipedia Revision Tool");
        stage.show();
    }

    private void fetchRevisions() {
        String article = articleField.getText().trim();
        if (article.isEmpty()) {
            statusLabel.setText("Error: No article name provided.");
            return;
        }

        // disable UI while request is running
        articleField.setDisable(true);
        searchButton.setDisable(true);
        statusLabel.setText("Loading...");
        resultList.getItems().clear();

        Thread worker = new Thread(() -> {
            try {
                URLConnection conn = GetJsonData.connectToWikipedia(article);
                String json = GetJsonData.readJsonAsStringFrom(conn);
                Platform.runLater(() -> processJson(json));
            } catch (IOException ex) {
                Platform.runLater(() -> statusLabel.setText("Error: Network issue."));
            } catch (URISyntaxException ex) {
                Platform.runLater(() -> statusLabel.setText("Error: Invalid Wikipedia request."));
            } finally {
                Platform.runLater(() -> {
                    articleField.setDisable(false);
                    searchButton.setDisable(false);
                    if (statusLabel.getText().equals("Loading...")) {
                        statusLabel.setText(""); // clear if nothing went wrong
                    }
                });
            }
        });

        worker.setDaemon(true); // so it won't block app exit
        worker.start();
    }

    private void processJson(String rawJson) {
        JSONObject json = new JSONObject(rawJson);
        JSONObject query = json.getJSONObject("query");

        // Redirect check
        if (query.has("redirects")) {
            JSONArray redirects = query.getJSONArray("redirects");
            if (!redirects.isEmpty()) {
                String redirectedTo = redirects.getJSONObject(0).getString("to");
                statusLabel.setText("Redirected to " + redirectedTo);
            }
        }

        JSONObject pages = query.getJSONObject("pages");
        String pageId = pages.keys().next();

        if (pageId.equals("-1")) {
            statusLabel.setText("Error: No page found.");
            return;
        }

        JSONArray revisions = pages.getJSONObject(pageId).optJSONArray("revisions");
        if (revisions == null || revisions.isEmpty()) {
            statusLabel.setText("Error: No revisions found.");
            return;
        }

        for (int i = 0; i < revisions.length(); i++) {
            JSONObject rev = revisions.getJSONObject(i);
            String timestamp = rev.getString("timestamp");
            String user = rev.getString("user");
            resultList.getItems().add((i + 1) + ". " + user + " — " + timestamp);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}