package pl.books.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import pl.books.dataprovider.DataProvider;
import pl.books.dataprovider.data.AuthorVO;
import pl.books.dataprovider.data.BookVO;
import pl.books.model.BookSearch;

public class BooksSearchController {

    private final DataProvider dataProvider = DataProvider.INSTANCE;
    private final BookSearch model = new BookSearch();

    @FXML
    private ResourceBundle resources;

    @FXML
    private TextField titleField;

    @FXML
    private TableColumn<BookVO, Number> idColumn;

    @FXML
    private TableColumn<BookVO, String> titleColumn;

    @FXML
    private TableColumn<BookVO, String> authorsColumn;

    @FXML
    private TableView<BookVO> resultTable;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;
    
    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize() {
        initializeResultTable();
        titleField.textProperty().bindBidirectional(model.titleProperty());
        deleteButton.disableProperty().bind(resultTable.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void searchButtonAction(ActionEvent event) {
        searchBooks();
    }
    
    @FXML
    public void deleteSelectedAction() {
        if (ButtonType.OK.equals(confirmDeletionDialog()))
            deleteSelectedBook();
    }

    @FXML
    public void addButtonAction() throws IOException {
        String fxmlFile = "/fxml/bookAdd.fxml";
        String bundlePath = "bundle/bundle";
        
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile), ResourceBundle.getBundle(bundlePath));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");
        Stage stage = new Stage();
        stage.initOwner(mainPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Adding a book");
        stage.setScene(scene);
        stage.showAndWait();
        searchBooks();
    }
    
    private void initializeResultTable() {
        idColumn.setCellValueFactory(cellData -> new ReadOnlyLongWrapper(cellData.getValue().getId()));

        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));

        authorsColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<BookVO, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<BookVO, String> param) {
                        Function<AuthorVO, String> mapAuthor = a -> "" + a.getFirstName() + " " + a.getLastName();
                        String authors = param.getValue().getAuthors().stream().map(mapAuthor)
                                .collect(Collectors.joining(", "));
                        return new ReadOnlyStringWrapper(authors);
                    }
                });

        resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookVO>() {
            @Override
            public void changed(ObservableValue<? extends BookVO> observable, BookVO oldValue, BookVO newValue) {
                model.setSelected(newValue);
            }
        });
        
        resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));
        
        FilteredList<BookVO> filteredData = new FilteredList<>(model.resultProperty(), b -> true);

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<BookVO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(resultTable.comparatorProperty());
        resultTable.setItems(sortedData);
    }

    private void searchBooks() {
        Task<Collection<BookVO>> searchTask = new Task<Collection<BookVO>>() {
            @Override
            protected Collection<BookVO> call() throws Exception {
                return dataProvider.findBooksByTitle("");
            }

            @Override
            protected void succeeded() {
                model.setResult(new ArrayList<BookVO>(getValue()));
                resultTable.getSortOrder().clear();
            }
        };
        new Thread(searchTask).start();
    }

    private ButtonType confirmDeletionDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("delete.title"));
        alert.setHeaderText(resources.getString("delete.header") + model.getSelected().getTitle() + "\"");
        alert.setContentText(resources.getString("delete.content"));
        return alert.showAndWait().get();
    }

    private void deleteSelectedBook() {
        Task<Exception> deleteBookTask = new Task<Exception>() {
            @Override
            protected Exception call() {
                dataProvider.deleteBook(model.getSelected().getId());
                return null;
            }

            @Override
            protected void failed() {
                super.failed();
            }

            @Override
            protected void succeeded() {
                if (getValue() == null) {
                    searchBooks();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Connection Error");
                    alert.setHeaderText("Failure to connect with server.");
                    alert.setContentText("Please check if server works.");
                    alert.showAndWait();
                }
            }
        };
        new Thread(deleteBookTask).start();
    }
}
