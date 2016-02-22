package pl.books.dataprovider.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import net.sf.corn.httpclient.HttpClient.HTTP_METHOD;
import net.sf.corn.httpclient.HttpForm;
import net.sf.corn.httpclient.HttpResponse;
import pl.books.dataprovider.DataProvider;
import pl.books.dataprovider.data.BookVO;

public class DataProviderImpl implements DataProvider {
    
    private static String requestPath = "http://localhost:9721/workshop";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Collection<BookVO> findBooksByTitle(String titlePrefix) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
        HttpForm client = new HttpForm(new URI(requestPath + "/services/books/books-by-title"));
        Collection<BookVO> books = new ArrayList<BookVO>();
        client.putFieldValue("titlePrefix", titlePrefix);
        HttpResponse response = client.doGet();
        books = Arrays.asList(objectMapper.readValue(response.getData(), BookVO[].class));
        return books;
    }

    @Override
    public void deleteBook(Long bookId) throws URISyntaxException, IOException {
        HttpForm client = new HttpForm(new URI(requestPath + "/services/books/book/" + bookId));
        client.sendData(HTTP_METHOD.DELETE);
    }

    @Override
    public BookVO addBook(BookVO book) throws URISyntaxException, IOException {
        HttpForm client = new HttpForm(new URI(requestPath + "/services/books/book/"));
        String jsonBook = objectMapper.writeValueAsString(book);
        client.putFieldValue("book", jsonBook);
        HttpResponse response = client.doPost();
        BookVO responseBook = objectMapper.readValue(response.getData(), BookVO.class);
        return responseBook;
    }

}
