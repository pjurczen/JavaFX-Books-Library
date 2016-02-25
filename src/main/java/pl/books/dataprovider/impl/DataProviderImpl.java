package pl.books.dataprovider.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
    public Collection<BookVO> findBooksByTitle(String titlePrefix) {
        HttpForm client = null;
        Collection<BookVO> books = new ArrayList<BookVO>();
        HttpResponse response = null;
        try {
            client = new HttpForm(new URI(requestPath + "/services/books/books-by-title"));
            client.putFieldValue("titlePrefix", titlePrefix);
            response = client.doGet();
            books = Arrays.asList(objectMapper.readValue(response.getData(), BookVO[].class));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void deleteBook(Long bookId) {
        HttpForm client = null;
        try {
            client = new HttpForm(new URI(requestPath + "/services/books/book/" + bookId));
            client.sendData(HTTP_METHOD.DELETE);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BookVO addBook(BookVO bookVO) {
        HttpForm client = null;
        HttpResponse response = null;
        String book = null;
        BookVO responseBook = null;
        try {
            client = new HttpForm(new URI(requestPath + "/services/books/book/"));
            book = objectMapper.writeValueAsString(bookVO);
            client.setContentType("Application/JSON");
            response = client.sendData(HTTP_METHOD.POST, book);
            responseBook = objectMapper.readValue(response.getData(), BookVO.class);
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
        return responseBook;
    }

}
