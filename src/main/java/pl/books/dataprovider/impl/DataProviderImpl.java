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
        try {
            client = new HttpForm(new URI(requestPath + "/services/books/books-by-title"));
        } catch (URISyntaxException e2) {
            e2.printStackTrace();
        }
        Collection<BookVO> books = new ArrayList<BookVO>();
        client.putFieldValue("titlePrefix", titlePrefix);
        HttpResponse response = null;
        try {
            response = client.doGet();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            books = Arrays.asList(objectMapper.readValue(response.getData(), BookVO[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void deleteBook(Long bookId) {
        HttpForm client = null;
        try {
            client = new HttpForm(new URI(requestPath + "/services/books/book/" + bookId));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            client.sendData(HTTP_METHOD.DELETE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BookVO addBook(BookVO bookVO) {
        HttpForm client = null;
        try {
            client = new HttpForm(new URI(requestPath + "/services/books/book/"));
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        String book = null;
        try {
            book = objectMapper.writeValueAsString(bookVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.setContentType("Application/JSON");
        HttpResponse response = null;
        try {
            response = client.sendData(HTTP_METHOD.POST, book);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookVO responseBook = null;
        try {
            responseBook = objectMapper.readValue(response.getData(), BookVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBook;
    }

}
