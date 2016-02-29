package pl.books.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.books.dataprovider.DataProvider;
import pl.books.dataprovider.data.AuthorVO;
import pl.books.dataprovider.data.BookVO;
import pl.books.model.BookAdd;

public class BookAddController {

    private BookAdd model = new BookAdd();
    private final DataProvider dataProvider = DataProvider.INSTANCE;
    
    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField newBookTitleField;

    @FXML
    private TextField newAuthorField;

    @FXML
    private ListView<AuthorVO> authorsList;

    @FXML
    private Button authorAddButton;

    @FXML
    private Button authorRemoveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button submitBookButton;
    
    @FXML
    private void initialize() {
        newBookTitleField.textProperty().bindBidirectional(model.titleProperty());
        authorsList.itemsProperty().bind(model.authorsListProperty());
        authorRemoveButton.disableProperty().bind(authorsList.getSelectionModel().selectedItemProperty().isNull());
        submitBookButton.disableProperty().bind(Bindings.isEmpty(model.titleProperty()).or(Bindings.isEmpty(model.authorsListProperty())));
        initializeAuthorsList();
    }
    
    private void initializeAuthorsList() {
        authorsList.setCellFactory(new Callback<ListView<AuthorVO>, ListCell<AuthorVO>>() {
            public ListCell<AuthorVO> call(ListView<AuthorVO> param) {
                final ListCell<AuthorVO> cell = new ListCell<AuthorVO>() {
                    @Override
                    public void updateItem(AuthorVO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName());
                            if(item.getFirstName().isEmpty()) {
                                setText(item.getLastName());
                            }
                        }
                    }
                };
                return cell;
            }
        });
        authorsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AuthorVO>() {
            @Override
            public void changed(ObservableValue<? extends AuthorVO> observable, AuthorVO oldValue, AuthorVO newValue) {
                model.setSelected(newValue);
            }
        });
    }

    @FXML
    public void cancelAction() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addAuthorAction() {
        String firstName = "";
        String lastName = "";
        try {
            firstName = newAuthorField.getText().split(" ")[0];
            lastName = newAuthorField.getText().split(" ")[1];
        } catch (Exception e) {
            firstName = "";
            lastName = newAuthorField.getText().split(" ")[0];
        }
        model.addAuthor(new AuthorVO(null, firstName, lastName));
        newAuthorField.clear();
    }

    @FXML
    public void removeAuthorAction() {
        model.removeAuthor(model.getSelected());
    }

    @FXML
    public void submitAction() {
        Task<BookVO> addBookTask = new Task<BookVO>() {
            @Override
            protected BookVO call() throws Exception {
                return dataProvider.addBook(new BookVO(null, model.getTitle(), model.getAuthors()));
            }
            @Override
            protected void succeeded() {
                cancelAction();
            }
        };
        new Thread(addBookTask).start();
    }
}
