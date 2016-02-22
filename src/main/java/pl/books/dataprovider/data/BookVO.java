package pl.books.dataprovider.data;

import java.util.List;

public class BookVO {
    private Long id;
    private String title;
    private List<AuthorVO> authors;

    protected BookVO() {
    }

    public BookVO(Long id, String title, List<AuthorVO> authors) {
        this.id = id;
        this.title = title;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AuthorVO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorVO> authors) {
        this.authors = authors;
    }
}
