package pl.books.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.books.dataprovider.data.AuthorVO;

public class BookAdd {
    
    private final StringProperty title = new SimpleStringProperty("");
    private final ObservableList<AuthorVO> authors = FXCollections.observableList(new ArrayList<AuthorVO>());
    private final ObjectProperty<AuthorVO> selectedAuthor = new SimpleObjectProperty<>();
    private final ListProperty<AuthorVO> authorsListProperty = new SimpleListProperty<>(authors);
    
    public final String getTitle() {
        return title.get();
    }

    public final void setTitle(String value) {
        title.set(value);
    }
    
    public StringProperty titleProperty() {
        return title;
    }
    
    public ListProperty<AuthorVO> authorsListProperty() {
        return authorsListProperty;
    }
    
    public ObservableList<AuthorVO> getAuthors() {
        return authors;
    }
    
    public void addAuthor(AuthorVO value) {
        if(!authors.contains(value)) {
            authors.add(value);
        }
    }
    
    public void removeAuthor(AuthorVO value) {
        authors.remove(value);
    }

    public void setAuthors(List<AuthorVO> value) {
        authors.setAll(value);
    }
    
    public void setSelected(AuthorVO value) {
        selectedAuthor.set(value);
    }

    public AuthorVO getSelected() {
        return selectedAuthor.get();
    }

    public ObjectProperty<AuthorVO> selectedProperty() {
        return selectedAuthor;
    }
}
