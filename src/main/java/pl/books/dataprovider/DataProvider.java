package pl.books.dataprovider;

import java.util.Collection;

import pl.books.dataprovider.data.BookVO;
import pl.books.dataprovider.impl.DataProviderImpl;

public interface DataProvider {

    DataProvider INSTANCE = new DataProviderImpl();
    
    Collection<BookVO> findBooksByTitle(String prefix);
    
    void deleteBook(Long id);
    
    BookVO addBook(BookVO book);
}
