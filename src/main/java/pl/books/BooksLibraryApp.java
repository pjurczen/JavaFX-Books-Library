package pl.books;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BooksLibraryApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        String langCode = getParameters().getNamed().get("lang");
        if (langCode != null && !langCode.isEmpty()) {
            Locale.setDefault(Locale.forLanguageTag(langCode));
        }
        
        String fxmlFile = "/fxml/books.fxml";
        String bundlePath = "bundle/bundle";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile), ResourceBundle.getBundle(bundlePath));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Books library");
        stage.setScene(scene);
        stage.show();
    }
}
