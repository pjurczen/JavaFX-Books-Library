package pl.books.dataprovider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import pl.books.dataprovider.data.BookVO;
import pl.books.dataprovider.impl.DataProviderImpl;

public interface DataProvider {

    DataProvider INSTANCE = new DataProviderImpl();
    
    Collection<BookVO> findBooksByTitle(String prefix) throws JsonParseException, JsonMappingException, IOException, URISyntaxException;
    
    void deleteBook(Long id) throws URISyntaxException, IOException;
    
    BookVO addBook(BookVO book) throws URISyntaxException, IOException;
}
