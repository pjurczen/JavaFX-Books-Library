package pl.books.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import pl.books.dataprovider.data.AuthorVO;
import pl.books.dataprovider.data.BookVO;

public class BookSearch {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final ListProperty<AuthorVO> authors = new SimpleListProperty<>();
    private final ObjectProperty<BookVO> selected = new SimpleObjectProperty<>();
    private final ListProperty<BookVO> result = new SimpleListProperty<>(
            FXCollections.observableList(new ArrayList<>()));

    public final Long getId() {
        return id.get();
    }

    public final void setId(Long value) {
        id.set(value);
    }

    public LongProperty idProperty() {
        return id;
    }

    public final String getTitle() {
        return title.get();
    }

    public final void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public final List<AuthorVO> getAuthors() {
        return authors.get();
    }

    public final void setAuthors(List<AuthorVO> value) {
        authors.setAll(value);
    }

    public ListProperty<AuthorVO> authorListProperty() {
        return authors;
    }

    public final List<BookVO> getResult() {
        return result.get();
    }

    public final void setResult(List<BookVO> value) {
        result.setAll(value);
    }

    public ListProperty<BookVO> resultProperty() {
        return result;
    }

    public final void setSelected(BookVO value) {
        selected.set(value);
    }

    public final BookVO getSelected() {
        return selected.get();
    }

    public ObjectProperty<BookVO> selectedProperty() {
        return selected;
    }
}
